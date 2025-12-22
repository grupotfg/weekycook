// Datos de categoría devueltos por el backend
export interface Category {
  id: number;
  nombre: string;
  descripcion?: string;
}

// Payload para crear o editar categorías
export interface CategoryPayload {
  nombre: string;
  descripcion?: string;
}
