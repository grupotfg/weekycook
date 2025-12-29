import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Recipe } from '../../../core/models/recipe.model';

@Component({
  selector: 'app-recipe-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recipe-card.component.html',
  styleUrl: './recipe-card.component.css',
})
export class RecipeCardComponent {
  @Input({ required: true }) recipe!: Recipe;

  private readonly difficultyLabels: Record<
    'facil' | 'media' | 'dificil',
    string
  > = {
    facil: 'Fácil',
    media: 'Media',
    dificil: 'Difícil',
  };

  get image(): string {
    return this.recipe.fotoUrl?.trim()
      ? this.recipe.fotoUrl
      : 'https://images.unsplash.com/photo-1466637574441-749b8f19452f?auto=format&fit=crop&w=900&q=80&q=60';
  }

  get difficultyBadge(): string {
    switch (this.getDifficulty()) {
      case 'facil':
        return 'badge-easy';
      case 'media':
        return 'badge-medium';
      default:
        return 'badge-hard';
    }
  }

  get difficultyLabel(): string {
    return this.difficultyLabels[this.getDifficulty()];
  }

  get creatorInitial(): string {
    const source = this.recipe.creadorNombre ?? this.recipe.titulo ?? '?';
    return source.charAt(0).toUpperCase();
  }

  private getDifficulty(): 'facil' | 'media' | 'dificil' {
    const time = this.recipe.tiempoPreparacionMin ?? 0;

    if (time <= 25) {
      return 'facil';
    }

    if (time <= 45) {
      return 'media';
    }

    return 'dificil';
  }
}
