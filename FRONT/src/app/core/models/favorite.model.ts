// Respuesta del backend al listar favoritos (FavoritoResponseDTO)
export interface Favorite {
  idFavorito: number;
  recetaId: number;
  recetaTitulo: string;
  fechaGuardado: string;
}

// Payload para marcar una receta como favorita (FavoritoRequestDTO)
export interface FavoritePayload {
  recetaId: number;
}
