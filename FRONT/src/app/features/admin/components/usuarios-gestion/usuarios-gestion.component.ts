import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../../core/services/user.service';
import { User } from '../../../../core/models/user.model';

@Component({
  selector: 'app-usuarios-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios-gestion.component.html'
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
    this.userService.getAll().subscribe(data => this.usuarios = data);
  }
  //Busquedas
get usuariosFiltrados() {
  const filter = this.searchText.toLowerCase().trim();
  return this.usuarios.filter(u => 
    u.nombre.toLowerCase().includes(filter) || 
    u.correo.toLowerCase().includes(filter)
  );
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
      this.userService.update(this.selectedUser.id, this.selectedUser).subscribe(() => this.finalizar());
    } else {
      // Nota: Asegúrate de que tu UserService tenga el método create()
      this.userService.create(this.selectedUser).subscribe(() => this.finalizar());
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