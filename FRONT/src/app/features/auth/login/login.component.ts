import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
//metemos el nuevo servicio de alertas
import { AlertService } from '../../../core/services/alert.service'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private alertService = inject(AlertService);

  loginForm = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    contrasena: ['', Validators.required],
  });

  onSubmit() {
    if (this.loginForm.valid) {
      const { correo, contrasena } = this.loginForm.value;
      this.authService
        .login({ correo: correo!, contrasena: contrasena! })
        .subscribe({
          next: () => {
            this.alertService.success('¡Hola!', 'Entrando en tu planificador...');
            if (this.authService.isAdmin()) {
              this.router.navigate(['/admin']);
            } else {
              this.router.navigate(['/planner']);
            }
          },
          error: () => this.alertService.error('Error de acceso', 'El correo o la contraseña no son correctos.'),
        });
    }
  }
}