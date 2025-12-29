import { Routes } from '@angular/router';
import { LandingComponent } from './features/landing/landing.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
// Solo vamos a importar el Dashboard, NO el de ingredientes suelto
import { DashboardComponent } from './features/admin/dashboard/dashboard.component';
import { RecipesComponent } from './features/recipes/recipes.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },
  { path: 'recipes', component: RecipesComponent },

  // RUTA DE ADMIN (Apunta al Dashboard con sus pestañas)
  {
    path: 'admin',
    component: DashboardComponent,
  },

  // Redirección por defecto
  { path: '**', redirectTo: '' },
];
