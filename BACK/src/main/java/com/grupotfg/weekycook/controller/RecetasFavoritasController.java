package com.grupotfg.weekycook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.request.FavoritoRequestDTO;
import com.grupotfg.weekycook.dto.response.FavoritoResponseDTO;
import com.grupotfg.weekycook.entity.RecetasFavoritas;
import com.grupotfg.weekycook.service.RecetasFavoritasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class RecetasFavoritasController {

	 @Autowired
	    private RecetasFavoritasService favoritasService;

    // ----Agregar a favoritos
	 
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<FavoritoResponseDTO> agregarFavorito(
            @PathVariable Integer usuarioId,
            @RequestBody @Valid FavoritoRequestDTO request) {

        return ResponseEntity.ok(favoritasService.agregarFavorito(usuarioId, request));
    }

    // ----Eliminar favorito
    
    @DeleteMapping("/usuario/{usuarioId}/receta/{recetaId}")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable Integer usuarioId,
            @PathVariable Integer recetaId) {

        favoritasService.eliminarFavorito(usuarioId, recetaId);
        return ResponseEntity.noContent().build();
    }

    // ----Listar favoritos del usuario
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FavoritoResponseDTO>> listarFavoritos(
            @PathVariable Integer usuarioId) {

        return ResponseEntity.ok(favoritasService.listarFavoritosUsuario(usuarioId));
    }
}