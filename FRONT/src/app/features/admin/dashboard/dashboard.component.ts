import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importamos el componente de ingredientes para usarlo en el HTML
import { IngredientesGestionComponent } from '../components/ingredientes-gestion/ingredientes-gestion.component';
//importamos categorias
import { CategoriasGestionComponent } from '../components/categorias-gestion/categorias-gestion.component';
import { UsuariosGestionComponent } from '../components/usuarios-gestion/usuarios-gestion.component';
import { RecetasGestionComponent } from "../components/recetas-gestion/recetas-gestion.component";
@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  //en este import metemos las pestañas del panel, ingredientes, usuarios...
  imports: [CommonModule, IngredientesGestionComponent, CategoriasGestionComponent, UsuariosGestionComponent, RecetasGestionComponent], 
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent {
  // Controlamos qué pestaña está activa
  activeTab: string = 'ingredientes';

  setTab(tabName: string) {
    this.activeTab = tabName;
  }
}