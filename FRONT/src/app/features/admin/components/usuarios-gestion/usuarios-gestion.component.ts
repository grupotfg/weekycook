import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../../core/services/user.service';
import { User } from '../../../../core/models/auth.model'; 
import { AlertService } from '../../../../core/services/alert.service';

@Component({
  selector: 'app-usuarios-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios-gestion.component.html',
})
export class UsuariosGestionComponent implements OnInit {
  private userService = inject(UserService);
  private alertService = inject(AlertService);

  usuarios: User[] = [];
  searchText: string = '';
  selectedUser: User = this.initUser();
  isEditing = false;
  showForm = false;

  ngOnInit() {
    this.cargarUsuarios();
  }

  initUser(): User {
    // Inicializamos con los campos que espera el modelo y el backend
    return { 
      nombre: '', 
      correo: '', 
      contrasena: '', 
      esAdmin: false,
      numComensalesDefecto: 2 
    } as User;
  }

  cargarUsuarios() {
    // Añadimos el tipo (data: User[]) para evitar errores de compilación
    this.userService.getAll().subscribe({
      next: (data: User[]) => {
        this.usuarios = data;
      },
      error: () => {
        this.alertService.error('Error', 'No se pudieron cargar los usuarios del servidor.');
      }
    });
  }

  // Lógica de búsqueda avanzada
  get usuariosFiltrados() {
    const filter = this.normalizeForSearch(this.searchText);
    if (!filter) return this.usuarios;

    return this.usuarios.filter((u) => {
      const nombreCompleto = this.normalizeForSearch(`${u.nombre ?? ''} ${u.apellido ?? ''}`);
      const correo = this.normalizeForSearch(u.correo);
      return nombreCompleto.includes(filter) || correo.includes(filter);
    });
  }

  private normalizeForSearch(value?: string | null): string {
    return (value ?? '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '') // Quita tildes
      .replace(/\s+/g, '') // Quita espacios
      .trim();
  }

  nuevoUsuario() {
    this.selectedUser = this.initUser();
    this.isEditing = false;
    this.showForm = true;
  }

  editar(user: User) {
    // duplicamos el usuario para no modificar la lista original antes de guardar
    this.selectedUser = { ...user, contrasena: '' }; 
    this.isEditing = true;
    this.showForm = true;
  }

  guardar() {
    if (this.isEditing && this.selectedUser.id) {
      this.userService.update(this.selectedUser.id, this.selectedUser).subscribe({
        next: () => {
          this.alertService.success('¡Actualizado!', 'Usuario modificado correctamente.');
          this.finalizar();
        },
        //pongo revisar la clave, porque para modificar es necesario reenviar
        //  clave y ponerla nueva sino no funciona
        error: () => this.alertService.error('Error', 'No se pudo actualizar el usuario, revisa la clave.')
      });
    } else {
      this.userService.create(this.selectedUser).subscribe({
        next: () => {
          this.alertService.success('¡Creado!', 'El nuevo usuario ha sido registrado.');
          this.finalizar();
        },
        error: () => this.alertService.error('Error', 'No se pudo crear el usuario. Revisa si el correo ya existe.')
      });
    }
  }

  eliminar(id: number, nombre: string) {
    // Usamos SweetAlert
    this.alertService.confirmDelete(nombre).then((confirmado) => {
      if (confirmado && id) {
        this.userService.delete(id).subscribe({
          next: () => {
            this.alertService.success('Eliminado', 'El usuario ha sido borrado.');
            this.cargarUsuarios();
          },
          error: () => this.alertService.error('Error', 'No se pudo eliminar el usuario.')
        });
      }
    });
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