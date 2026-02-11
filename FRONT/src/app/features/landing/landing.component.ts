import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Recipe } from '../../core/models/recipe.model';
import { RecipeCardComponent } from '../../shared/components/recipe-card/recipe-card.component';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink, RecipeCardComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css',
})
export class LandingComponent implements OnInit {
  private http = inject(HttpClient);
  recetasDestacadas: Recipe[] = [];

  ngOnInit() {
    this.http.get<Recipe[]>('http://localhost:8080/api/recetas').subscribe({
      next: (data) => {
        // Si el array es null o undefined, lo inicializamos a un array vacío.
        this.recetasDestacadas = data ? data.slice(0, 3) : [];
      },
      error: (err) => {
        console.error('Error al cargar las recetas:', err);
        // Si hay error de conexión, se queda en array vacío y se muestra alerta
        this.recetasDestacadas = [];
      },
    });
  }
}
