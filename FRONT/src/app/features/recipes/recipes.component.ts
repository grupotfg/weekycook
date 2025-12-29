import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Category } from '../../core/models/category.model';
import { Recipe } from '../../core/models/recipe.model';
import { CategoryService } from '../../core/services/category.service';
import { RecipeService } from '../../core/services/recipe.service';
import { RecipeCardComponent } from '../../shared/components/recipe-card/recipe-card.component';

@Component({
  selector: 'app-recipes',
  standalone: true,
  imports: [CommonModule, FormsModule, RecipeCardComponent],
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

  readonly timeOptions = [
    { value: 'all', label: 'Cualquier tiempo' },
    { value: '20', label: '≤ 20 min' },
    { value: '40', label: '≤ 40 min' },
    { value: '60', label: '≤ 60 min' },
  ];

  readonly pageSizeOptions = [4, 8, 12, 20];
  pageSize = 4;
  currentPage = 1;

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

    this.currentPage = 1;
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

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredRecipes.length / this.pageSize));
  }

  get paginatedRecipes(): Recipe[] {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredRecipes.slice(start, start + this.pageSize);
  }

  get pageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, index) => index + 1);
  }

  changePageSize(size: number | string): void {
    const numericSize = Number(size) || this.pageSize;

    if (numericSize !== this.pageSize) {
      this.pageSize = numericSize;
      this.currentPage = 1;
    }
  }

  goToPage(page: number): void {
    const target = Math.min(Math.max(page, 1), this.totalPages);
    this.currentPage = target;
  }

  prevPage(): void {
    this.goToPage(this.currentPage - 1);
  }

  nextPage(): void {
    this.goToPage(this.currentPage + 1);
  }
}
