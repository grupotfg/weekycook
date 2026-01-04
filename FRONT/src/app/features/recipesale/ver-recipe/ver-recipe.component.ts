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
  styleUrl: './ver-recipe.component.css' 
})


export class VerRecipeComponent implements OnInit {

  recipe?: Recipe;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService
  ) {}

  ngOnInit(): void {
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
        }
      });
    }
  }
}

