import { Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecipeService } from '../../../../core/services/recipe.service';
import { CategoryService } from '../../../../core/services/category.service';
import { IngredientService } from '../../../../core/services/ingredient.service';
import {
  Recipe,
  RecipeIngredientRequest,
} from '../../../../core/models/recipe.model';

@Component({
  selector: 'app-recetas-gestion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './recetas-gestion.component.html',
})
export class RecetasGestionComponent implements OnInit {
  private recipeService = inject(RecipeService);
  private categoryService = inject(CategoryService);
  private ingredientService = inject(IngredientService);

  recetas: Recipe[] = [];
  categorias: any[] = [];
  ingredientesDisponibles: any[] = [];

  searchText: string = '';
  showForm = false;
  isEditing = false;

  selectedRecipe: Recipe = this.initRecipe();
  tempIngrediente: RecipeIngredientRequest = this.initTempIng();

  @Output() formStateChange = new EventEmitter<boolean>();

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.recipeService.getAll().subscribe((data) => (this.recetas = data));
    this.categoryService.getAll().subscribe((data) => (this.categorias = data));
    this.ingredientService
      .getAll()
      .subscribe((data) => (this.ingredientesDisponibles = data));
  }

  initRecipe(): Recipe {
    return {
      titulo: '',
      descripcionCorta: '',
      instrucciones: '',
      tiempoPreparacionMin: 30,
      porciones: 2,
      categoriaId: 0,
      ingredientes: [],
      fotoUrl: '',
    };
  }

  initTempIng(): RecipeIngredientRequest {
    return { ingredienteId: 0, cantidad: 0, unidad: 'gr' };
  }

  // MÉTODO QUE FALTABA
  nuevaReceta() {
    this.selectedRecipe = this.initRecipe();
    this.isEditing = false;
    this.showForm = true;
    this.formStateChange.emit(true);
  }
  editar(receta: Recipe) {
    if (!receta.id) return;

    this.recipeService.getById(receta.id).subscribe({
      next: (fullRecipe) => {
        // --- TRUCO PARA REUPERAR NOMBRES --- que no me salian ni a tiros, busco info
        // Antes de asignar la receta, vemos sus ingredientes para buscar el nombre
        if (fullRecipe.ingredientes) {
          fullRecipe.ingredientes = fullRecipe.ingredientes.map((ingReceta) => {
            // Buscamos el ingrediente en la lista que ya tenemos cargada
            const infoIng = this.ingredientesDisponibles.find(
              (i) => i.id === ingReceta.ingredienteId
            );
            return {
              ...ingReceta,
              nombreAux: infoIng
                ? infoIng.nombre
                : `Ingrediente ${ingReceta.ingredienteId}`,
            };
          });
        }
        // -----------------------------------

        this.selectedRecipe = fullRecipe;
        this.isEditing = true;
        this.showForm = true;
        this.formStateChange.emit(true);
      },
      error: (e) => console.error('Error al cargar detalle:', e),
    });
  }

  get recetasFiltradas() {
    const search = this.normalizeSearch(this.searchText);
    if (!search) {
      return this.recetas;
    }

    return this.recetas.filter((r) =>
      this.normalizeSearch(r.titulo).includes(search)
    );
  }

  agregarIngrediente() {
    this.tempIngrediente.cantidad = Math.max(
      0,
      this.tempIngrediente.cantidad ?? 0
    );

    if (
      this.tempIngrediente.ingredienteId > 0 &&
      this.tempIngrediente.cantidad > 0
    ) {
      const ing = this.ingredientesDisponibles.find(
        (i) => i.id === this.tempIngrediente.ingredienteId
      );

      if (!this.selectedRecipe.ingredientes) {
        this.selectedRecipe.ingredientes = [];
      }

      this.selectedRecipe.ingredientes.push({
        ...this.tempIngrediente,
        nombreAux: ing?.nombre,
      });

      this.tempIngrediente = this.initTempIng();
    }
  }

  quitarIngrediente(index: number) {
    this.selectedRecipe.ingredientes?.splice(index, 1);
  }

  guardar() {
    if (this.isEditing && this.selectedRecipe.id) {
      this.recipeService
        .update(this.selectedRecipe.id, this.selectedRecipe)
        .subscribe(() => this.finalizar());
    } else {
      this.recipeService
        .create(this.selectedRecipe)
        .subscribe(() => this.finalizar());
    }
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar esta receta?')) {
      this.recipeService.delete(id).subscribe(() => this.cargarDatos());
    }
  }

  finalizar() {
    this.showForm = false;
    this.isEditing = false;
    this.formStateChange.emit(false);
    this.cargarDatos();
  }

  cancelar() {
    this.showForm = false;
    this.isEditing = false;
    this.formStateChange.emit(false);
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
