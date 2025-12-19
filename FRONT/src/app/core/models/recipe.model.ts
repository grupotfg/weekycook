// Interfaz ligera para listados (Home, Buscador, Cards)
// se basa en RecetaResponseDTO
export interface Recipe {
  id: number;
  titulo: string;
  descripcionCorta: string;
  tiempoPreparacionMin: number;
  porciones: number;
  fotoUrl?: string;       // Opcional
  categoriaId: number;
  categoriaNombre: string; // Muy útil para mostrar "Desayuno" sin hacer otra petición
  creadorNombre: string;
}

// Interfaz detallada para la vista de una receta individual
// se basa en RecetaDetailResponseDTO
export interface RecipeDetail extends Recipe {
  instrucciones: string;
  ingredientes: IngredientRecipe[]; // Lista de ingredientes específicos de esta receta
  valorNutricional?: NutritionalValue;
}

// Subinterfaz para los ingredientes dentro de una receta
// base de RecetaIngredienteResponseDTO
export interface IngredientRecipe {
  ingredienteId: number;
  ingredienteNombre: string;
  cantidad: number;
  unidad: string; // 'gr', 'ml', etc.
  unidadBase?: string;
}

// Subinterfaz para valores nutricionales
// se base en ValorNutricionalResponseDTO
export interface NutritionalValue {
  caloriasTotales: number;
  proteinasTotales: number;
  grasasTotales: number;
  hidratosTotales: number;
}