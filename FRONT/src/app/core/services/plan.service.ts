import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlanDetail, PlanPayload, PlanItemPayload, PlanSummary } from '../models/plan.model';

@Injectable({ providedIn: 'root' })
export class PlanService {
  private http = inject(HttpClient);


 private apiUrl = 'http://localhost:8080/api/planes';


  // lo cambio al 4 por que patri es el id 4 y me salia vacio el array
  private usuarioId = 4;

  private headers(): HttpHeaders {
    return new HttpHeaders({ usuarioId: String(this.usuarioId) });
  }

  // GET /planes/usuario/{usuarioId}
  getUserPlans(): Observable<PlanSummary[]> {
    return this.http.get<PlanSummary[]>(`${this.apiUrl}/usuario/${this.usuarioId}`);
  }

  // GET /planes/{planId} con header usuarioId
  getById(planId: number): Observable<PlanDetail> {
    return this.http.get<PlanDetail>(`${this.apiUrl}/${planId}`, { headers: this.headers() });
  }

  // POST /planes/usuario con header usuarioId
  create(payload: PlanPayload): Observable<PlanDetail> {
    return this.http.post<PlanDetail>(`${this.apiUrl}/usuario`, payload, { headers: this.headers() });
  }

  // PUT /planes/{planId} con header usuarioId
  update(planId: number, payload: PlanPayload): Observable<PlanDetail> {
    return this.http.put<PlanDetail>(`${this.apiUrl}/${planId}`, payload, { headers: this.headers() });
  }

  // DELETE /planes/{planId} con header usuarioId
  delete(planId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${planId}`, { headers: this.headers() });
  }

  // POST /planes/{planId}/aleatorio con header usuarioId
  randomize(planId: number): Observable<PlanDetail> {
    return this.http.post<PlanDetail>(`${this.apiUrl}/${planId}/aleatorio`, {}, { headers: this.headers() });
  }

  // POST /planes/{planId}/items con header usuarioId
  addOrUpdateItem(planId: number, item: PlanItemPayload): Observable<PlanDetail> {
    return this.http.post<PlanDetail>(`${this.apiUrl}/${planId}/items`, item, { headers: this.headers() });
  }

  // DELETE /planes/{planId}/items?dia=...&turno=... con header usuarioId
  deleteItem(planId: number, dia: string, turno: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${planId}/items?dia=${encodeURIComponent(dia)}&turno=${encodeURIComponent(turno)}`,
      { headers: this.headers() }
    );
  }
}

