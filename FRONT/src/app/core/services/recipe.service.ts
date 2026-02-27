import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recipe } from '../models/recipe.model';

@Injectable({ providedIn: 'root' })
export class RecipeService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/recetas';

  // El ID del admin (esto vendrá del Auth más adelante, usamos 1 por ahora)
  private adminId = 1; 

  getAll(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(this.apiUrl);
  }

  getById(id: number): Observable<Recipe> {
    // Pasamos el usuarioId=1 como parámetro de consulta para saltar el checkAdmin de Java si es necesario
    return this.http.get<Recipe>(`${this.apiUrl}/${id}?usuarioId=1`);
  }

  create(recipe: Recipe): Observable<Recipe> {
    return this.http.post<Recipe>(`${this.apiUrl}?usuarioId=${this.adminId}`, recipe);
  }

  update(id: number, recipe: Recipe): Observable<Recipe> {
    return this.http.put<Recipe>(`${this.apiUrl}/${id}?usuarioId=${this.adminId}`, recipe);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}?usuarioId=${this.adminId}`);
  }
}