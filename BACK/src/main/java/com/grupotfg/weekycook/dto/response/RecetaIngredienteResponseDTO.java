package com.grupotfg.weekycook.dto.response;

import lombok.Data;

/**
 * RecetaIngredienteResponseDTO
 * Ingrediente con su cantidad específica en una receta
 * 
 * Combina datos del ingrediente con cantidad/unidad de una receta x
 * Incluye nombre del ingrediente: Evitamos hacer otra llamada
 * Útil para mostrar x ej 200gr de Pollo pechuga
 * No mete recetaId ya por la peticion el cliente ya sabe de qué receta es x contexto
 * 
 * GET /api/recetas/{id}/ingredientes en controller
 */
@Data

public class RecetaIngredienteResponseDTO {
    
    private Integer ingredienteId;
    
    private String ingredienteNombre; // Nombre 
    
    private String unidadBase; // Unidad base del ingrediente
    
    private Double cantidad; // Cantidad en esta receta
    
    private String unidad; // Unidad usada en la receta que podria ser dife de unidadBase
}