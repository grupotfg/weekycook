import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importamos el componente de ingredientes para usarlo en el HTML
import { IngredientesGestionComponent } from '../components/ingredientes-gestion/ingredientes-gestion.component';
//importamos categorias
import { CategoriasGestionComponent } from '../components/categorias-gestion/categorias-gestion.component';
import { UsuariosGestionComponent } from '../components/usuarios-gestion/usuarios-gestion.component';
import { RecetasGestionComponent } from '../components/recetas-gestion/recetas-gestion.component';
@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  //en este import metemos las pestañas del panel, ingredientes, usuarios...
  imports: [
    CommonModule,
    IngredientesGestionComponent,
    CategoriasGestionComponent,
    UsuariosGestionComponent,
    RecetasGestionComponent,
  ],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  @ViewChild(RecetasGestionComponent)
  recetasComponent?: RecetasGestionComponent;
  // Controlamos qué pestaña está activa
  activeTab: string = 'ingredientes';

  // Flag para mostrar el botón de regreso rápido en la cabecera
  showRecetasShortcut = false;

  setTab(tabName: string) {
    this.activeTab = tabName;
    if (tabName !== 'recetas') {
      this.showRecetasShortcut = false;
    }
  }

  handleRecetasFormState(open: boolean) {
    // Solo mostramos el botón cuando la pestaña activa sigue siendo recetas
    this.showRecetasShortcut = open && this.activeTab === 'recetas';
  }

  returnToRecetas() {
    this.recetasComponent?.cancelar();
    this.setTab('recetas');
  }
}
