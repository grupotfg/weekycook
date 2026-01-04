import { Routes } from '@angular/router';
import { LandingComponent } from './features/landing/landing.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { DashboardComponent } from './features/admin/dashboard/dashboard.component';
import { RecipesComponent } from './features/recipes/recipes.component';
import { VerRecipeComponent } from './features/recipesale/ver-recipe/ver-recipe.component';
import { PlannerComponent } from './features/planner/planner.component';
import { PlannerSelectRecipesComponent } from './features/planner/planner-select-recipes/planner-select-recipes.component';



export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },

  // listado de recetas
  { path: 'recipes', component: RecipesComponent },

  // SELECCIONAR RECETAS 
{ path: 'planner/select', component: PlannerSelectRecipesComponent },


  // DETALLE DE RECETA (esta es la de mi pagina)
  { path: 'recipes/:id', component: VerRecipeComponent },
  //PLANIFICADOR
  { path: 'planner', component: PlannerComponent },

  // admin
  {
    path: 'admin',
    component: DashboardComponent,
  },

  // este es el comodin ssiempre tengo que ponerlo al final y solo uno
  { path: '**', redirectTo: 'recipes' },
];