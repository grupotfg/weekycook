import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoryService } from '../../../../core/services/category.service';
import { Category } from '../../../../core/models/category.model';

@Component({
  selector: 'app-categorias-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categorias-gestion.component.html',
})
export class CategoriasGestionComponent implements OnInit {
  private catService = inject(CategoryService);

  categorias: Category[] = [];
  searchText: string = ''; // Texto del buscador
  selectedCat: Category = { nombre: '' };
  isEditing = false;
  showForm = false;

  ngOnInit() {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.catService.getAll().subscribe((data) => (this.categorias = data));
  }

  // Lógica del BUSCADOR
  get categoriasFiltradas() {
    const filter = this.normalizeSearch(this.searchText);

    if (!filter) {
      return this.categorias;
    }

    return this.categorias.filter((c) =>
      this.normalizeSearch(c.nombre).includes(filter)
    );
  }

  guardar() {
    if (this.isEditing && this.selectedCat.id) {
      this.catService
        .update(this.selectedCat.id, this.selectedCat)
        .subscribe(() => this.finalizar());
    } else {
      this.catService
        .create(this.selectedCat)
        .subscribe(() => this.finalizar());
    }
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar esta categoría?')) {
      this.catService.delete(id).subscribe({
        next: () => this.cargarCategorias(),
        error: () =>
          alert(
            'Error: No se puede eliminar una categoría con recetas asociadas.'
          ),
      });
    }
  }

  editar(cat: Category) {
    this.selectedCat = { ...cat };
    this.isEditing = true;
    this.showForm = true;
  }

  finalizar() {
    this.cargarCategorias();
    this.cancelar();
  }

  cancelar() {
    this.showForm = false;
    this.isEditing = false;
    this.selectedCat = { nombre: '', descripcion: '' }; //para limpiar los campos
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
