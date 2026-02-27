package com.grupotfg.weekycook.dto.response;


import lombok.Data;

/**
 * RecetaIngredienteResponseDTO
 * Ingrediente con su cantidad específica en una receta
 Combina datos del ingrediente con cantidad/unidad de esta receta 
 Incluye nombre del ingrediente por no llamar más
 Excluye recetaId ya se sabe por contexto
 
 * GET /api/recetas/{id}/ingredientes
 */
@Data
public class RecetaIngredienteResponseDTO {
    
    private Integer ingredienteId;
    
    private String ingredienteNombre; // Nombre para mostrar
    
    private String unidadBase; // Unidad base del ingrediente
    
    private Double cantidad; // Cantidad en esta receta
    
    private String unidad; // Unidad usada en la receta (puede diferir de unidadBase)
}