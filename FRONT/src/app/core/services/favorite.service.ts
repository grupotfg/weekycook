import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Favorite, FavoritePayload } from '../models/favorite.model';

@Injectable({ providedIn: 'root' })
export class FavoriteService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/favoritos';

  create(userId: number, payload: FavoritePayload): Observable<Favorite> {
    return this.http.post<Favorite>(
      `${this.apiUrl}/usuario/${userId}`,
      payload,
    );
  }

  delete(userId: number, recipeId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/usuario/${userId}/receta/${recipeId}`,
    );
  }

  getAll(userId: number): Observable<Favorite[]> {
    return this.http.get<Favorite[]>(`${this.apiUrl}/usuario/${userId}`);
  }
}
