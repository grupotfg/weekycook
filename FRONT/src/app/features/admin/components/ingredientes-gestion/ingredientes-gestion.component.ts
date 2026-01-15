import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IngredientService } from '../../../../core/services/ingredient.service';
import { Ingredient } from '../../../../core/models/ingredient.model';

@Component({
  selector: 'app-ingredientes-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ingredientes-gestion.component.html',
})
export class IngredientesGestionComponent implements OnInit {
  private ingService = inject(IngredientService);

  ingredientes: Ingredient[] = [];
  searchText: string = '';
  selectedIng: Ingredient = this.resetIng();
  isEditing = false;
  showForm = false; // visibilidad del formulario

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.ingService.getAll().subscribe((data) => (this.ingredientes = data));
  }

  get ingredientesFiltrados() {
    const filter = this.normalizeSearch(this.searchText);

    if (!filter) {
      return this.ingredientes;
    }

    return this.ingredientes.filter((ing) =>
      this.normalizeSearch(ing.nombre).includes(filter)
    );
  }

  guardar() {
    const action =
      this.isEditing && this.selectedIng.id
        ? this.ingService.update(this.selectedIng.id, this.selectedIng)
        : this.ingService.create(this.selectedIng);

    action.subscribe(() => {
      this.cargarDatos();
      this.cancelar();
    });
  }

  editar(ing: Ingredient) {
    this.selectedIng = { ...ing };
    this.isEditing = true;
    this.showForm = true; // Al editar, mostramos el formulario
  }

  eliminar(ing: Ingredient) {
    if (confirm(`¿Eliminar "${ing.nombre}"?`)) {
      this.ingService.delete(ing.id!).subscribe(() => this.cargarDatos());
    }
  }

  cancelar() {
    this.showForm = false;
    this.isEditing = false;
    this.selectedIng = this.resetIng();
  }

  private resetIng(): Ingredient {
    return {
      nombre: '',
      unidadBase: 'gr',
      caloriasPorUnidad: 0,
      proteinasPorUnidad: 0,
      grasasPorUnidad: 0,
      hidratosPorUnidad: 0,
    };
  }

  private normalizeSearch(value?: string | null): string {
    return (value ?? '')
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, '')
      .trim();
  }
}
