import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    //para el landing
    path: '',
    loadComponent: () => import('./features/landing/landing.component').then(m => m.LandingComponent)
  },
  {
    //este para llevar al login
    path: 'auth/login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
{
    //para ir a la pantalla de registro
    path: 'auth/register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },


  // Aquí añadiremos 'admin' y 'planner' en los siguientes pasos



  
  { path: '**', redirectTo: '' }
];