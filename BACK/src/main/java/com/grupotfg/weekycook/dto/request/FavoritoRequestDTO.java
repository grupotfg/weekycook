package com.grupotfg.weekycook.dto.request;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * FavoritoRequestDTO
 * Marca una receta como favorita
 * 
 * Solo metemos recetaId: El usuarioId se obtiene del login
 * No incluimos fechaGuardado, viene del back como LocalDateTime.now()
 * Evitamos que un usuario marque favoritos por otro
 * 
 * POST /api/favoritos { "recetaId": 1 }
 */
@Data
public class FavoritoRequestDTO {
    
    @NotNull(message = "ID de receta es obligatorio")
    private Integer recetaId;
}