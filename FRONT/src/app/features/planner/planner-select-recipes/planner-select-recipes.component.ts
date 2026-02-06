import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RecipeService } from '../../../core/services/recipe.service';
import { PlanService } from '../../../core/services/plan.service';
import { Recipe } from '../../../core/models/recipe.model';
import { PlanItemPayload } from '../../../core/models/plan.model';
import { DayOfWeek } from '../../../core/enums/day.enum';
import { MealTurn } from '../../../core/enums/meal-turn.enum';

@Component({
  selector: 'app-planner-select-recipes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './planner-select-recipes.component.html',
  // He metido unos estilos rápidos aquí para no complicar el CSS global
  styles: [`
    .recipe-card { cursor: pointer; transition: all 0.2s ease-in-out; border-radius: 15px; overflow: hidden; }
    .recipe-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important; border-color: var(--primary); }
    .img-container { height: 160px; background-color: #f8f9fa; display: flex; align-items: center; justify-content: center; }
    .badge-info { background-color: var(--primary-light); color: var(--primary-dark); font-weight: 600; }
  `]
})
export class PlannerSelectRecipesComponent implements OnInit {
  // Inyectamos las herramientas que necesitamos
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recipeService = inject(RecipeService);
  private planService = inject(PlanService);

  // Variables para la lógica
  recetas: Recipe[] = [];
  filtroTexto: string = '';
  loading = true;

  // Estos datos vienen de la URL al hacer clic en el hueco del planner
  planId: number = 0;
  dia!: DayOfWeek; // Usamos ! porque sabemos que llegarán
  turno!: MealTurn;

  ngOnInit() {
    // 1. Escuchamos lo que viene por la URL (query params)
    this.route.queryParams.subscribe(params => {
      this.planId = Number(params['planId']);
      
      // TRUCO: Usamos 'as' para decirle a TS que el string de la URL 
      // es realmente un valor válido de nuestros Enums
      this.dia = params['dia'] as DayOfWeek;
      this.turno = params['turno'] as MealTurn;

      // Si por lo que sea entran aquí sin plan o día, los echamos fuera por seguridad
      if (!this.planId || !this.dia || !this.turno) {
        this.volver();
      }
    });

    // 2. Traemos todas las recetas de la base de datos
    this.cargarRecetas();
  }

  cargarRecetas() {
    this.loading = true;
    this.recipeService.getAll().subscribe({
      next: (res) => {
        this.recetas = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Vaya, no hemos podido cargar las recetas:', err);
        this.loading = false;
      }
    });
  }

  // Función para filtrar recetas por nombre mientras escribes
  get recetasFiltradas() {
    const busqueda = this.filtroTexto.toLowerCase().trim();
    if (!busqueda) return this.recetas;
    return this.recetas.filter(r => r.titulo.toLowerCase().includes(busqueda));
  }

  // ESTA ES LA FUNCIÓN CLAVE: Cuando el usuario elige su plato
  seleccionarReceta(receta: Recipe) {
    if (!receta.id) return;

    // Preparamos el paquete para el servidor
    const nuevoItem: PlanItemPayload = {
      dia: this.dia,
      turno: this.turno,
      recetaId: receta.id
    };

    // Llamamos al servicio para que guarde la receta en ese hueco
    this.planService.addOrUpdateItem(this.planId, nuevoItem).subscribe({
      next: () => {
        // Si todo sale bien, volvemos al calendario para ver el cambio
        this.volver();
      },
      error: (err) => {
        console.error('Error al guardar el plato en el plan:', err);
        alert('Lo sentimos, no se ha podido guardar la receta en el planificador.');
      }
    });
  }

volver() {
  // paso el planId pata que sepa que tiene que cargar el detalle y no la lista
  this.router.navigate(['/planner'], { queryParams: { planId: this.planId } });
}
}