// Filtros para listar recetas (FiltroRecetaRequestDTO)
export interface RecipeFilters {
  titulo?: string;
  categoriaId?: number;
  tiempoMax?: number;
  page?: number;
  size?: number;
}
