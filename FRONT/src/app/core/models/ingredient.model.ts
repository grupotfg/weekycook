// Datos completos del ingrediente (IngredienteResponseDTO)
export interface Ingredient {
  id: number;
  nombre: string;
  unidadBase: string;
  caloriasPorUnidad: number;
  proteinasPorUnidad: number;
  grasasPorUnidad: number;
  hidratosPorUnidad: number;
}

// Datos específicos de un ingrediente dentro de una receta (RecetaIngredienteResponseDTO)
export interface IngredientRecipe {
  ingredienteId: number;
  ingredienteNombre: string;
  cantidad: number;
  unidad: string; // 'gr', 'ml', etc.
  unidadBase?: string;
}
