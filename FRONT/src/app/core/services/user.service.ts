import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

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

 create(user: User): Observable<User> {
  const userToBack = {
    nombre: user.nombre,
    apellido: user.apellido,
    correo: user.correo,
    "contraseña": `{noop}${user.contrasena}`,
    esAdmin: user.esAdmin === true, // Fuerzo que sea un booleano puro xq no pilla el cambio
    descripcion: user.descripcion,
    numComensalesDefecto: user.numComensalesDefecto || 2
  };

  console.log('JSON enviado a Java:', JSON.stringify(userToBack)); 
  return this.http.post<User>(this.apiUrl, userToBack);
}

  update(id: number, user: User): Observable<User> {
    const userToBack: any = {
      nombre: user.nombre,
      apellido: user.apellido,
      correo: user.correo,
      esAdmin: user.esAdmin,
      descripcion: user.descripcion,
      numComensalesDefecto: user.numComensalesDefecto
    };

    // Solo enviamos la contraseña si se ha modificado en el formulario
    if (user.contrasena && user.contrasena.trim() !== '') {
      userToBack["contraseña"] = `{noop}${user.contrasena}`;
    }

    return this.http.put<User>(`${this.apiUrl}/${id}`, userToBack);
  }
}
