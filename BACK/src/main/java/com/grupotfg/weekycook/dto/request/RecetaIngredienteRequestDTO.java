package com.grupotfg.weekycook.dto.request;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * RecetaIngredienteRequestDTO
 * Asocia un ingrediente a una receta con cantidad y unidad
 * Se va a usar en endpoint x separado: POST /api/recetas/{recetaId}/ingredientes
 * No va a incluir recetaId: Se mete en la URL del endpoint
 * IngredienteId en lugar de todo completo para menos carga
 * para manejar mejor claves compuestas + relaciones
 * 
 * x ej Asignar 200gr de Pollo a "Ensalada de pollo"
 */
@Data
public class RecetaIngredienteRequestDTO {
    
    @NotNull(message = "ID de ingrediente es obligatorio")
    private Integer ingredienteId;
    
    @NotNull(message = "La cantidad es obligatoria")
    private Double cantidad;
    
    @NotNull(message = "La unidad es obligatoria")
    private String unidad; // Ej: "gr", "ml", "ud"
}