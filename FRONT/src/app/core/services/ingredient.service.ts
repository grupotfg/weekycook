import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ingredient } from '../models/ingredient.model';

//comunica con el controller de ingredientes

@Injectable({ providedIn: 'root' })
export class IngredientService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/ingredientes';

  getAll(): Observable<Ingredient[]> {
    return this.http.get<Ingredient[]>(this.apiUrl);
  }

  create(ing: Ingredient): Observable<Ingredient> {
    return this.http.post<Ingredient>(this.apiUrl, ing);
  }

  update(id: number, ing: Ingredient): Observable<Ingredient> {
    return this.http.put<Ingredient>(`${this.apiUrl}/${id}`, ing);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}