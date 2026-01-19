import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

import { Recipe } from '../../../core/models/recipe.model';
import { RecipeService } from '../../../core/services/recipe.service';

@Component({
  selector: 'app-ver-recipe',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ver-recipe.component.html',
  styleUrl: './ver-recipe.component.css',
})
export class VerRecipeComponent implements OnInit {
  recipe?: Recipe;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService,
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
    }
  }

  private scrollToTop(): void {
    if (typeof window === 'undefined') {
      return;
    }

    window.scrollTo({ top: 0, left: 0, behavior: 'auto' });
  }

  public get difficultyLabel(): string {
    const minutes = this.recipe?.tiempoPreparacionMin ?? 0;

    if (minutes <= 20) {
      return 'Fácil';
    }

    if (minutes <= 40) {
      return 'Media';
    }

    return 'Difícil';
  }

  public get totalTimeLabel(): string {
    const total = this.recipe?.tiempoPreparacionMin;
    return total === null || total === undefined
      ? 'Sin dato'
      : `${total} minutos`;
  }

  public get instructionSteps(): string[] {
    const raw = this.recipe?.instrucciones ?? '';
    if (!raw.trim()) {
      return [];
    }

    const cleanSteps = (steps: string[]): string[] =>
      steps
        .map((step) => step.replace(/^\d+[)\.:\-]*\s*/, '').trim())
        .filter(Boolean);

    const byNewline = cleanSteps(raw.split(/\r?\n+/));
    if (byNewline.length > 1) {
      return byNewline;
    }

    const byNumbering = cleanSteps(raw.split(/\d+[)\.]\s*/));
    if (byNumbering.length > 1) {
      return byNumbering;
    }

    return cleanSteps([raw]);
  }
}
