import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/auth.model'; 

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/usuarios';

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  create(user: any): Observable<User> {
    const userToBack = {
      ...user,
      "contraseña": `{noop}${user.contrasena || user.password}`,
      esAdmin: user.esAdmin === true || user.esAdmin === 1
    };
    return this.http.post<User>(this.apiUrl, userToBack);
  }

  update(id: number, user: any): Observable<User> {
    const userToBack = { ...user };
    if (user.contrasena) {
      userToBack["contraseña"] = `{noop}${user.contrasena}`;
    }
    return this.http.put<User>(`${this.apiUrl}/${id}`, userToBack);
  }
}