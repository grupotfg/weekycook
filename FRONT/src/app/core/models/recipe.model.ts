export interface RecipeIngredientRequest {
  ingredienteId: number;
  cantidad: number;
  unidad: string;
  nombreAux?: string; // Para mostrar el nombre en el formulario
}

export interface Recipe {
  id?: number;
  titulo: string;
  descripcionCorta: string;
  instrucciones?: string; // Opcional para el listado
  tiempoPreparacionMin: number;
  porciones: number;
  fotoUrl?: string;
  categoriaId: number;
  categoriaNombre?: string; // Del DTO de listado
  creadorNombre?: string;
  ingredientes?: RecipeIngredientRequest[]; // Opcional para el listado
}
