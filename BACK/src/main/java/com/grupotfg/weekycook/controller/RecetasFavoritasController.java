package com.grupotfg.weekycook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.request.FavoritoRequestDTO;
import com.grupotfg.weekycook.dto.response.FavoritoResponseDTO;

import com.grupotfg.weekycook.service.RecetasFavoritasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Favoritos", description = "Gestión de recetas favoritas de los usuarios")
public class RecetasFavoritasController {

    @Autowired
    private RecetasFavoritasService favoritasService;

    // ----Agregar a favoritos
    
    @PostMapping("/usuario/{usuarioId}")
    @Operation(summary = "Agregar receta a favoritos", description = "Marca una receta como favorita para un usuario")
    public ResponseEntity<FavoritoResponseDTO> agregarFavorito(
            @Parameter(description = "ID del usuario que añade la receta a favoritos")
            @PathVariable Integer usuarioId,
            @RequestBody @Valid FavoritoRequestDTO request) {

        return ResponseEntity.ok(favoritasService.agregarFavorito(usuarioId, request));
    }

    // ----Eliminar favorito
    
    @DeleteMapping("/usuario/{usuarioId}/receta/{recetaId}")
    @Operation(summary = "Eliminar receta de favoritos", description = "Quita una receta de la lista de favoritos de un usuario")
    public ResponseEntity<Void> eliminarFavorito(
            @Parameter(description = "ID del usuario")
            @PathVariable Integer usuarioId,
            @Parameter(description = "ID de la receta favorita a eliminar")
            @PathVariable Integer recetaId) {

        favoritasService.eliminarFavorito(usuarioId, recetaId);
        return ResponseEntity.noContent().build();
    }

    // ----Listar favoritos del usuario
    
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar favoritos de un usuario", description = "Obtiene la lista de recetas marcadas como favoritas por un usuario")
    public ResponseEntity<List<FavoritoResponseDTO>> listarFavoritos(
            @Parameter(description = "ID del usuario")
            @PathVariable Integer usuarioId) {

        return ResponseEntity.ok(favoritasService.listarFavoritosUsuario(usuarioId));
    }
}