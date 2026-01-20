export interface RecipeIngredientRequest {
  ingredienteId: number;
  ingredienteNombre?: string; //añadi porque daba error
  cantidad: number;
  unidad: string;
  nombreAux?: string; // Para mostrar el nombre en el formulario
}

export interface RecipeNutrition {
  calorias?: number;
  proteinas?: number;
  grasas?: number;
  hidratos?: number;
}

export interface Recipe {
  id?: number;
  titulo: string;
  descripcionCorta: string;
  instrucciones?: string; // Opcional para el listado
  tiempoPreparacionMin: number | null;
  porciones: number | null;
  fotoUrl?: string;
  categoriaId: number;
  categoriaNombre?: string; // Del DTO de listado
  creadorNombre?: string;
  ingredientes?: RecipeIngredientRequest[]; // Opcional para el listado
  valorNutricional: RecipeNutrition;
}
