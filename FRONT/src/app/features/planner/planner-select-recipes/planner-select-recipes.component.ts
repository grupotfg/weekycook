import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';

import { RecipeService } from '../../../core/services/recipe.service';
import { PlanService } from '../../../core/services/plan.service';
import { Recipe } from '../../../core/models/recipe.model';
import { DayOfWeek } from '../../../core/enums/day.enum';
import { MealTurn } from '../../../core/enums/meal-turn.enum';
import { PlanItemPayload } from '../../../core/models/plan.model';

@Component({
  selector: 'app-planner-select-recipes',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './planner-select-recipes.component.html',
  styleUrl: './planner-select-recipes.component.css',
})
export class PlannerSelectRecipesComponent implements OnInit {

  // cabecera igual que planner
  subtitle = 'Organiza los platos de lunes a domingo';
  viewLabel = 'Visto de frente';

  // contexto de la celda
  planId: number | null = null;
  day!: DayOfWeek;
  turn!: MealTurn;

  readonly days = Object.values(DayOfWeek);
  readonly turns = Object.values(MealTurn);

  // recetas
  recipes: Recipe[] = [];
  filteredRecipes: Recipe[] = [];
  searchTerm = '';

  loading = true;
  message = '';
  saving = false;


  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private planService: PlanService
  ) {}

  ngOnInit(): void {
    // Leer parámetros de la URL (?planId=..&dia=..&turno=..)
    const params = this.route.snapshot.queryParamMap;

    this.planId = Number(params.get('planId'));
    this.day = params.get('dia') as DayOfWeek;
    this.turn = params.get('turno') as MealTurn;

    this.loadRecipes();
  }

  // cargar recetas
  loadRecipes(): void {
    this.loading = true;

    this.recipeService.getAll().subscribe({
      next: (data) => {
        this.recipes = data ?? [];
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.recipes = [];
        this.filteredRecipes = [];
        this.loading = false;
      }
    });
  }

  // filtro
  applyFilter(): void {
    const term = this.searchTerm.toLowerCase().trim();

    this.filteredRecipes = !term
      ? this.recipes
      : this.recipes.filter(r =>
          r.titulo.toLowerCase().includes(term) ||
          (r.descripcionCorta ?? '').toLowerCase().includes(term)
        );
  }

  // asignar receta
  selectRecipe(recipe: Recipe): void {
    if (!this.planId || !recipe.id) return;

    const payload: PlanItemPayload = {
      recetaId: recipe.id,
      dia: this.day,
      turno: this.turn,
      notas: ''
    };

    this.planService.addOrUpdateItem(this.planId, payload).subscribe({
      next: () => {
        this.message = `Receta asignada a ${this.day} - ${this.turn}`;
      },
      error: () => {
        this.message = 'No se pudo asignar la receta';
      }
    });
  }

  // eliminar receta de la celda
  removeRecipe(): void {
    if (!this.planId) return;

    this.planService.deleteItem(this.planId, this.day, this.turn).subscribe({
      next: () => {
        this.message = `Receta eliminada de ${this.day} - ${this.turn}`;
      },
      error: () => {
        this.message = 'No se pudo eliminar la receta';
      }
    });
  }
}
