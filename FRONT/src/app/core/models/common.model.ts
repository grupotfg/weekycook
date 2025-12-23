// Respuesta genérica con metadatos de paginación
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

// Envoltorio para respuestas del backend cuando solo devuelve un dato
export interface ApiResponse<T> {
  data: T;
  message?: string;
}

// Estructura básica para errores HTTP normalizados en el front
export interface ApiError {
  status: number;
  message: string;
  details?: string[];
}
