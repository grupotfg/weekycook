import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { Recipe } from '../../../core/models/recipe.model';
import { RecipeService } from '../../../core/services/recipe.service';
import { FavoriteService } from '../../../core/services/favorite.service';
import { AuthService } from '../../../core/services/auth.service';

interface FavoriteState {
  saving: boolean;
  saved: boolean;
  message: string;
  error: string;
}

@Component({
  selector: 'app-ver-recipe',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ver-recipe.component.html',
  styleUrls: ['./ver-recipe.component.css'],

})
export class VerRecipeComponent implements OnInit {
  recipe?: Recipe;
  loading = true;
  error = false;

  favoriteState: FavoriteState = {
    saving: false,
    saved: false,
    message: '',
    error: '',
  };

  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private favoriteService: FavoriteService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.scrollToTop();
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (id) {
      this.recipeService.getById(id).subscribe({
        next: (data) => {
          this.recipe = data;
          this.loading = false;
        },
        error: () => {
          this.error = true;
          this.loading = false;
        },
      });
    } else {
      this.error = true;
      this.loading = false;
    }
  }

  onFavoriteClick(): void {
    if (
      !this.recipe?.id ||
      this.favoriteState.saving ||
      this.favoriteState.saved
    ) {
      return;
    }

    const currentUser = this.authService.currentUser();

    if (!currentUser?.id) {
      this.favoriteState.error =
        'Inicia sesión para guardar recetas en favoritos.';
      this.favoriteState.message = '';
      return;
    }

    this.favoriteState.saving = true;
    this.favoriteState.error = '';
    this.favoriteState.message = '';

    this.favoriteService
      .create(currentUser.id, { recetaId: this.recipe.id })
      .subscribe({
        next: () => {
          this.favoriteState.saving = false;
          this.favoriteState.saved = true;
          this.favoriteState.message = 'Receta guardada en tus favoritos.';
        },
        error: (err: HttpErrorResponse) => {
          this.favoriteState.saving = false;

          const backendMsg = (err.error?.message ?? '')
            .toString()
            .toLowerCase();

          // 409 = conflicto (por ejemplo: ya existe en favoritos)
          if (
            err.status === 409 ||
            backendMsg.includes('ya está marcada como favorita')
          ) {
            this.favoriteState.saved = true;
            this.favoriteState.message = 'Esta receta ya estaba en tus favoritos.';
            this.favoriteState.error = '';
            return;
          }

          this.favoriteState.error =
            'No pudimos guardar la receta en favoritos. Inténtalo otra vez.';
        },
      });
  }

  private scrollToTop(): void {
    if (typeof window === 'undefined') {
      return;
    }
    window.scrollTo({ top: 0, left: 0, behavior: 'auto' });
  }

  public get difficultyLabel(): string {
    const minutes = this.recipe?.tiempoPreparacionMin ?? 0;

    if (minutes <= 20) return 'Fácil';
    if (minutes <= 40) return 'Media';
    return 'Difícil';
  }

  public get totalTimeLabel(): string {
    const total = this.recipe?.tiempoPreparacionMin;
    return total === null || total === undefined ? 'Sin dato' : `${total} minutos`;
  }

  public get instructionSteps(): string[] {
    const raw = this.recipe?.instrucciones ?? '';
    if (!raw.trim()) return [];

    const cleanSteps = (steps: string[]): string[] =>
      steps
        .map((step) => step.replace(/^\d+[)\.:\-]*\s*/, '').trim())
        .filter(Boolean);

    const byNewline = cleanSteps(raw.split(/\r?\n+/));
    if (byNewline.length > 1) return byNewline;

    const byNumbering = cleanSteps(raw.split(/\d+[)\.]\s*/));
    if (byNumbering.length > 1) return byNumbering;

    return cleanSteps([raw]);
  }
}
