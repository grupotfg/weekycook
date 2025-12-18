import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

// VALIDADOR
export const passwordMatchValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
  const password = group.get('password')?.value;
  const confirmPassword = group.get('confirmarContrasena')?.value;

  // Si coinciden o si alguno está vacío (para no molestar mientras se escribe), no hay error
  if (!password || !confirmPassword) return null;

  return password === confirmPassword ? null : { passwordMismatch: true };
};

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: '../login/login.component.scss'
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  registerForm = this.fb.group({
  nombre: ['', [Validators.required, Validators.minLength(2)]],
  apellido: [''],
  // lo pongo más estricto para email debe tener como algo@algo.dominio
  correo: ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
  password: ['', [Validators.required, Validators.minLength(6)]],
  confirmarContrasena: ['', Validators.required],
  descripcion: [''],
  numComensalesDefecto: [2]
}, { 
  validators: [passwordMatchValidator] //va a nivel de form
});

  onSubmit() {
    // ya esta ok, l o dejo porque me daba errores el validades de la contraseña con f12 ya ok los emnsajes por consola
    console.log('Errores del formulario:', this.registerForm.errors);
    console.log('Valor Password:', this.registerForm.get('password')?.value);
    console.log('Valor Confirmar:', this.registerForm.get('confirmarContrasena')?.value);

    if (this.registerForm.valid) {
      const val = this.registerForm.value;
      const requestBody = {
        correo: val.correo,
        nombre: val.nombre,
        apellido: val.apellido || '',
        // AÑADIMOS EL {noop} AQUÍ
        contraseña: '{noop}' + val.password, // y Mapeo por la puñetera ñ que hemos puesto que por eso daba error
        descripcion: val.descripcion || '',
        numComensalesDefecto: val.numComensalesDefecto || 2
      };

      this.http.post('http://localhost:8080/api/usuarios', requestBody)
        .subscribe({
          next: () => {
            alert('¡Registro con éxito!');
            this.router.navigate(['/auth/login']);
          },
          error: (err) => alert('Error: ' + (err.error?.message || 'Fallo en el servidor'))
        });
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}