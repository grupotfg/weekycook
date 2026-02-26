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
import { AlertService } from '../../../../core/services/alert.service';
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
  private alertService = inject(AlertService);

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
      creadorNombre: '',
      valorNutricional: this.initValoresNutricionales(),
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
              (i) => i.id === ingReceta.ingredienteId,
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
        if (!this.selectedRecipe.valorNutricional) {
          this.selectedRecipe.valorNutricional =
            this.initValoresNutricionales();
        }
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
      this.normalizeSearch(r.titulo).includes(search),
    );
  }

  agregarIngrediente() {
    this.tempIngrediente.cantidad = Math.max(
      0,
      this.tempIngrediente.cantidad ?? 0,
    );

    if (
      this.tempIngrediente.ingredienteId > 0 &&
      this.tempIngrediente.cantidad > 0
    ) {
      const ing = this.ingredientesDisponibles.find(
        (i) => i.id === this.tempIngrediente.ingredienteId,
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
  const action = (this.isEditing && this.selectedRecipe.id)
    ? this.recipeService.update(this.selectedRecipe.id, this.selectedRecipe)
    : this.recipeService.create(this.selectedRecipe);

  action.subscribe({
    next: () => {
      const msg = this.isEditing ? 'Receta actualizada correctamente' : 'Receta creada con éxito';
      this.alertService.success('¡Hecho!', msg);
      this.finalizar();
    },
    error: (err) => {
      console.error('Error al guardar receta:', err);
      this.alertService.error('Error', 'No se pudo guardar la receta. Revisa que todos los campos obligatorios estén rellenos.');
    }
  });
}

  // Cambia a (id, titulo) para que el Alert sea personalizado
eliminar(id: number, titulo: string) {
  this.alertService.confirmDelete(titulo).then((confirmado) => {
    if (confirmado && id) {
      this.recipeService.delete(id).subscribe({
        next: () => {
          this.alertService.success('Eliminada', `La receta "${titulo}" ha sido borrada.`);
          this.cargarDatos();
        },
        error: (err) => {
          // Detectamos si el error es de integridad (normalmente error 500 o 409)
          console.error('Detalle del error técnico:', err);

          // Personalizamos el mensaje para el usuario
          this.alertService.error(
            'No se puede borrar', 
            `La receta "${titulo}" está asociada a un planificador semanal y no puede eliminarse para no perder el historial.`
          );
        }
      });
    }
  });
}

  finalizar() {
    this.showForm = false;
    this.isEditing = false;
    this.formStateChange.emit(false);
    this.cargarDatos();
  }

  cancelar() {
    this.selectedRecipe = this.initRecipe();
    this.tempIngrediente = this.initTempIng();
    if (!this.isEditing) {
      this.showForm = true;
      this.formStateChange.emit(true);
    } else {
      this.closeForm();
    }
  }

  closeForm() {
    this.showForm = false;
    this.isEditing = false;
    this.formStateChange.emit(false);
  }

  private initValoresNutricionales() {
    return {
      calorias: 0,
      proteinas: 0,
      grasas: 0,
      hidratos: 0,
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
