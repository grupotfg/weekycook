import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User, LoginRequest } from '../models/auth.model';
import { tap, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  // nuestra Conexión
  private apiUrl = 'http://localhost:8080/api/usuarios'; 

  // --- para REACTIVO con Signals de Angular 19 que he estado mirando ---
  currentUser = signal<User | null>(this.getUserFromStorage());
  isAuthenticated = computed(() => !!this.currentUser());
  isAdmin = computed(() => !!this.currentUser()?.esAdmin);

  // Login Simulado (Busca usuario por correo)
  login(credentials: LoginRequest): Observable<User[]> {
    // Esto es temporal hasta que metamos JWT real.
    // Buscamos el usuario en el backend por correo.
    return this.http.get<User[]>(`${this.apiUrl}?correo=${credentials.correo}`).pipe(
      tap(users => {
        const user = users.find(u => u.correo === credentials.correo);
        if (user) {
          // Si existe, lo guardamos como logueado
          this.currentUser.set(user);
          this.saveToStorage(user);
        }
      })
    );
  }

  logout() {
    this.currentUser.set(null);
    localStorage.removeItem('weeky_user');
    this.router.navigate(['/']);
  }

  //hay que persistir para no perder sesión al recargar
  private saveToStorage(user: User) {
    localStorage.setItem('weeky_user', JSON.stringify(user));
  }

  private getUserFromStorage(): User | null {
    const stored = localStorage.getItem('weeky_user');
    return stored ? JSON.parse(stored) : null;
  }
}