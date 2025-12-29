import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Category } from '../../core/models/category.model';
import { Recipe } from '../../core/models/recipe.model';
import { CategoryService } from '../../core/services/category.service';
import { RecipeService } from '../../core/services/recipe.service';

@Component({
  selector: 'app-recipes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './recipes.component.html',
  styleUrl: './recipes.component.css',
})
export class RecipesComponent implements OnInit {
  recipes: Recipe[] = [];
  filteredRecipes: Recipe[] = [];
  categories: Category[] = [];

  searchTerm = '';
  selectedCategory = 'all';
  selectedDifficulty = 'all';
  selectedTime = 'all';

  loading = true;
  errorMessage = '';

  readonly difficultyLabels: Record<string, string> = {
    facil: 'Fácil',
    media: 'Media',
    dificil: 'Difícil',
  };

  readonly timeOptions = [
    { value: 'all', label: 'Cualquier tiempo' },
    { value: '20', label: '≤ 20 min' },
    { value: '40', label: '≤ 40 min' },
    { value: '60', label: '≤ 60 min' },
  ];

  constructor(
    private recipeService: RecipeService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.loadRecipes();
    this.loadCategories();
  }

  private loadRecipes(): void {
    this.loading = true;
    this.recipeService.getAll().subscribe({
      next: (data) => {
        this.recipes = data ?? [];
        this.applyFilters();
        this.loading = false;
      },
      error: () => {
        this.errorMessage =
          'No se pudieron cargar las recetas. Revisa que el backend esté disponible.';
        this.recipes = [];
        this.filteredRecipes = [];
        this.loading = false;
      },
    });
  }

  private loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories = data ?? [];
      },
      error: () => {
        this.categories = [];
      },
    });
  }

  applyFilters(): void {
    const term = this.searchTerm.trim().toLowerCase();

    this.filteredRecipes = this.recipes.filter((recipe) => {
      const matchesTerm =
        !term ||
        recipe.titulo.toLowerCase().includes(term) ||
        (recipe.descripcionCorta ?? '').toLowerCase().includes(term);

      const matchesCategory =
        this.selectedCategory === 'all' ||
        recipe.categoriaId === Number(this.selectedCategory);

      const matchesDifficulty =
        this.selectedDifficulty === 'all' ||
        this.getDifficulty(recipe) === this.selectedDifficulty;

      const matchesTime =
        this.selectedTime === 'all' ||
        (recipe.tiempoPreparacionMin ?? 0) <= Number(this.selectedTime);

      return matchesTerm && matchesCategory && matchesDifficulty && matchesTime;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = 'all';
    this.selectedDifficulty = 'all';
    this.selectedTime = 'all';
    this.applyFilters();
  }

  getDifficulty(recipe: Recipe): 'facil' | 'media' | 'dificil' {
    const time = recipe.tiempoPreparacionMin ?? 0;

    if (time <= 25) {
      return 'facil';
    }

    if (time <= 45) {
      return 'media';
    }

    return 'dificil';
  }

  getDifficultyLabel(recipe: Recipe): string {
    return this.difficultyLabels[this.getDifficulty(recipe)];
  }

  getDifficultyBadge(recipe: Recipe): string {
    switch (this.getDifficulty(recipe)) {
      case 'facil':
        return 'badge-easy';
      case 'media':
        return 'badge-medium';
      default:
        return 'badge-hard';
    }
  }

  getRecipeImage(recipe: Recipe): string {
    return recipe.fotoUrl?.trim()
      ? recipe.fotoUrl
      : 'https://images.unsplash.com/photo-1466637574441-749b8f19452f?auto=format&fit=crop&w=900&q=60';
  }

  getCreatorInitial(recipe: Recipe): string {
    const source = recipe.creadorNombre ?? recipe.titulo ?? '?';
    return source.charAt(0).toUpperCase();
  }
}
