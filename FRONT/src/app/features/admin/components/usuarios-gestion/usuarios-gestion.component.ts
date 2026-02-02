import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../../core/services/user.service';
import { User } from '../../../../core/models/user.model';

@Component({
  selector: 'app-usuarios-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios-gestion.component.html',
})
export class UsuariosGestionComponent implements OnInit {
  private userService = inject(UserService);

  usuarios: User[] = [];
  searchText: string = '';
  selectedUser: User = this.initUser();
  isEditing = false;
  showForm = false;

  ngOnInit() {
    this.cargarUsuarios();
  }

  initUser(): User {
    return { nombre: '', correo: '', contrasena: '', esAdmin: false };
  }

  cargarUsuarios() {
    this.userService.getAll().subscribe((data) => (this.usuarios = data));
  }
  //Busquedas
  get usuariosFiltrados() {
    const filter = this.normalizeForSearch(this.searchText);

    if (!filter) {
      return this.usuarios;
    }

    return this.usuarios.filter((u) => {
      const nombre = this.normalizeForSearch(u.nombre);
      const apellido = this.normalizeForSearch(u.apellido);
      const nombreCompleto = this.normalizeForSearch(
        `${u.nombre ?? ''}${u.apellido ?? ''}`
      );
      const correo = this.normalizeForSearch(u.correo);

      return (
        nombre.includes(filter) ||
        apellido.includes(filter) ||
        nombreCompleto.includes(filter) ||
        correo.includes(filter)
      );
    });
  }

  private normalizeForSearch(value?: string | null): string {
    return (value ?? '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, '')
      .trim();
  }

  nuevoUsuario() {
    this.selectedUser = this.initUser();
    this.isEditing = false;
    this.showForm = true;
  }

  editar(user: User) {
    this.selectedUser = { ...user, contrasena: '' }; // No cargamos la contraseña antigua por seguridad
    this.isEditing = true;
    this.showForm = true;
  }

  guardar() {
    if (this.isEditing && this.selectedUser.id) {
      this.userService
        .update(this.selectedUser.id, this.selectedUser)
        .subscribe(() => this.finalizar());
    } else {
      
      this.userService
        .create(this.selectedUser)
        .subscribe(() => this.finalizar());
    }
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
      this.userService.delete(id).subscribe(() => this.cargarUsuarios());
    }
  }

  finalizar() {
    this.cargarUsuarios();
    this.cancelar();
  }

  cancelar() {
    this.showForm = false;
    this.isEditing = false;
    this.selectedUser = this.initUser();
  }
}
