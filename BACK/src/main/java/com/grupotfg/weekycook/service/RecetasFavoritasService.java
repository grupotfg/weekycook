package com.grupotfg.weekycook.service;

import java.util.List;

import com.grupotfg.weekycook.dto.request.FavoritoRequestDTO;
import com.grupotfg.weekycook.dto.response.FavoritoResponseDTO;


public interface RecetasFavoritasService {

	FavoritoResponseDTO agregarFavorito(Integer usuarioId, FavoritoRequestDTO request);
	List<FavoritoResponseDTO> listarFavoritosUsuario(Integer usuarioId);

    void eliminarFavorito(Integer usuarioId, Integer recetaId);

  
}
