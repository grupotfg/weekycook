import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { FavoriteService } from '../../core/services/favorite.service';
import { RecipeService } from '../../core/services/recipe.service';
import { Recipe } from '../../core/models/recipe.model';
import { Favorite } from '../../core/models/favorite.model';
import { RecipeCardComponent } from '../../shared/components/recipe-card/recipe-card.component';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-favoritos',
  standalone: true,
   imports: [CommonModule, RouterLink, RecipeCardComponent],
  templateUrl: './favoritos.component.html',
  styleUrl: './favoritos.component.css',

})
export class FavoritosComponent implements OnInit {
  favorites: Favorite[] = [];      // DTO del backend
  favoriteRecipes: Recipe[] = [];  // recetas completas para pintar cards

  loading = true;
  error = '';



constructor(
  private favoriteService: FavoriteService,
  private recipeService: RecipeService,
  private authService: AuthService
) {}


  ngOnInit(): void {
    this.loadFavorites();
  }

loadFavorites(): void {
  const currentUser = this.authService.currentUser();

  if (!currentUser?.id) {
    this.error = 'Debes iniciar sesión para ver tus favoritos.';
    this.loading = false;
    return;
  }

  this.loading = true;
  this.error = '';

  this.favoriteService.getAll(currentUser.id).subscribe({
    next: (data) => {
      this.favorites = data ?? [];

      if (this.favorites.length === 0) {
        this.favoriteRecipes = [];
        this.loading = false;
        return;
      }

      const requests = this.favorites.map((fav) =>
        this.recipeService.getById(fav.recetaId).pipe(
          catchError(() => of(null as unknown as Recipe))
        )
      );

      forkJoin(requests).subscribe({
        next: (recipes) => {
          this.favoriteRecipes = (recipes ?? []).filter(Boolean);
          this.loading = false;
        },
        error: () => {
          this.error = 'No se pudieron cargar las recetas favoritas.';
          this.loading = false;
        },
      });
    },
    error: () => {
      this.error = 'No se pudieron cargar tus favoritos.';
      this.loading = false;
    },
  });
}
  removeFromFavorites(recipeId: number): void {
    const currentUser = this.authService.currentUser();
    if (!currentUser?.id) return;

    this.favoriteService.delete(currentUser.id, recipeId).subscribe({
      next: () => {
        this.favorites = this.favorites.filter((f) => f.recetaId !== recipeId);
        this.favoriteRecipes = this.favoriteRecipes.filter(
          (r) => r.id !== recipeId
        );
      },
    });
  }
}
