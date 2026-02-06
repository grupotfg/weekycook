import { Injectable, inject, computed } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { PlanDetail, PlanPayload, PlanItemPayload, PlanSummary } from '../models/plan.model';

@Injectable({ providedIn: 'root' })
export class PlanService {
  private http = inject(HttpClient);
  // Inyecto el auth para saber quién está logueado de verdad no el 4 como teniamos
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api/planes';

  // çcambia el usuario en auth, esto se actualiza solo.
  // Si no hay nadie logueado, devolvemos 0 (que fallará controlado en el back).
  private userId = computed(() => this.authService.currentUser()?.id ?? 0);

  // Helper para generar las cabecerascon el ID actual
  private headers(): HttpHeaders {
    const id = this.userId();
    if (id === 0) {
      console.warn('Ojo: Intentando llamar a la API sin usuario logueado.');
    }
    return new HttpHeaders({ usuarioId: String(id) });
  }

  // GET /planes/usuario/{usuarioId}
  getUserPlans(): Observable<PlanSummary[]> {
    const id = this.userId();
    if (!id) {
        // Si no hay ID, cortamos aquí para no hacer una llamada tonta al servidor
        return throwError(() => new Error('Usuario no autenticado'));
    }
    return this.http.get<PlanSummary[]>(`${this.apiUrl}/usuario/${id}`);
  }

  // GET /planes/{planId}
  getById(planId: number): Observable<PlanDetail> {
    return this.http.get<PlanDetail>(`${this.apiUrl}/${planId}`, { headers: this.headers() });
  }

  // POST /planes/usuario
  create(payload: PlanPayload): Observable<PlanDetail> {
    // Aquí fallaba,ahora enviamos el ID correcto en el header
    return this.http.post<PlanDetail>(`${this.apiUrl}/usuario`, payload, { headers: this.headers() });
  }

  // PUT /planes/{planId}
  update(planId: number, payload: PlanPayload): Observable<PlanDetail> {
    return this.http.put<PlanDetail>(`${this.apiUrl}/${planId}`, payload, { headers: this.headers() });
  }

  // DELETE /planes/{planId}
  delete(planId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${planId}`, { headers: this.headers() });
  }

  // POST /planes/{planId}/aleatorio
  randomize(planId: number): Observable<PlanDetail> {
    return this.http.post<PlanDetail>(`${this.apiUrl}/${planId}/aleatorio`, {}, { headers: this.headers() });
  }

  // POST /planes/{planId}/items
  addOrUpdateItem(planId: number, item: PlanItemPayload): Observable<PlanDetail> {
    return this.http.post<PlanDetail>(`${this.apiUrl}/${planId}/items`, item, { headers: this.headers() });
  }

  // DELETE /planes/{planId}/items
  deleteItem(planId: number, dia: string, turno: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${planId}/items?dia=${encodeURIComponent(dia)}&turno=${encodeURIComponent(turno)}`,
      { headers: this.headers() }
    );
  }
}
