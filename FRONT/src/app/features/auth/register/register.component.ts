import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../../core/services/alert.service';

// VALIDADOR PERSONALIZADO: Comprueba que las contraseñas coincidan
export const passwordMatchValidator: ValidatorFn = (
  group: AbstractControl
): ValidationErrors | null => {
  const password = group.get('password')?.value;
  const confirmPassword = group.get('confirmarContrasena')?.value;

  if (!password || !confirmPassword) return null;

  return password === confirmPassword ? null : { passwordMismatch: true };
};

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: '../login/login.component.css',
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private alertService = inject(AlertService);

  registerForm = this.fb.group(
    {
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: [''],
      correo: [
        '',
        [
          Validators.required,
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
        ],
      ],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmarContrasena: ['', Validators.required],
      descripcion: [''],
      numComensalesDefecto: [2],
    },
    {
      validators: [passwordMatchValidator], 
    }
  );

  onSubmit() {
    console.log('Estado del formulario:', this.registerForm.valid ? 'VÁLIDO' : 'INVÁLIDO');

    if (this.registerForm.valid) {
      const val = this.registerForm.value;
      
      // Creamos el cuerpo de la petición
      
      const requestBody = {
        correo: val.correo,
        nombre: val.nombre,
        apellido: val.apellido || '',
        "contraseña": `{noop}${val.password}`, 
        descripcion: val.descripcion || '',
        numComensalesDefecto: val.numComensalesDefecto || 2,
        esAdmin: false // El backend debería mapear esto a 'es_admin'
        
      };

      
      console.log('Enviando a Java:', requestBody);

      this.http
        .post('http://localhost:8080/api/usuarios', requestBody)
        .subscribe({
          next: () => {
            this.alertService.success(
              '¡Registro completado!', 
              'Ya puedes iniciar sesión con tu cuenta.'
            );
            this.router.navigate(['/auth/login']);
          },
          error: (err) => {
            console.error('Error de registro:', err);
            // Si el error es por el nulo de es_admin, lo veremos aquí
            this.alertService.error(
              'Error al registrar', 
              err.error?.message || 'Error en el servidor. Revisa si el campo es_admin falta.'
            );
          },
        });
    } else {
      this.alertService.error(
        'Revisa el formulario', 
        'Hay campos vacíos o las contraseñas no coinciden.'
      );
      this.registerForm.markAllAsTouched();
    }
  }
}