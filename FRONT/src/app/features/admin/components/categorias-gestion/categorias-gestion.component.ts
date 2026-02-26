import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoryService } from '../../../../core/services/category.service';
import { Category } from '../../../../core/models/category.model';
import { AlertService } from '../../../../core/services/alert.service';


@Component({
  selector: 'app-categorias-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categorias-gestion.component.html',
})
export class CategoriasGestionComponent implements OnInit {
  private catService = inject(CategoryService);
   private alertService = inject(AlertService);
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

  eliminar(id: number, nombre: string) {
  // Usamos el servicio de alertas que devuelve una Promesa (true/false)
  this.alertService.confirmDelete(nombre).then((confirmado) => {
    if (confirmado && id) {
      this.catService.delete(id).subscribe({
        next: () => {
          this.alertService.success('¡Eliminado!', `La categoría seleccionada ha sido borrada.`);
          this.cargarCategorias();
        },
        error: () => {
          
          this.alertService.error(
            'No se puede eliminar',
            'Esta categoría tiene recetas asociadas. Elimina o mueve las recetas primero.'
          );
        }
      });
    }
  });
}

  editar(cat: Category) {
    this.selectedCat = { ...cat };
    this.isEditing = true;
    this.showForm = true;
  }

  finalizar() {
  const mensaje = this.isEditing ? 'Categoría actualizada' : 'Categoría creada';
  this.alertService.success('¡Hecho!', mensaje);
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
