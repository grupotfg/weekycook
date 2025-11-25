package com.grupotfg.weekycook.dto.request;



import lombok.Data;

/**
 * FiltroRecetaRequestDTO
 * búsqueda y filtrado de recetas

 * Permite búsquedas parciales (solo por título, solo por tiempo, etc.)
 * tiempoMax: Filtro "Rápidas < 30 min" del anteproyecto
 * titulo: Búsqueda parcial (contiene la palabra)
 * categoriaId: Filtro por categoría
 * campos opcionales no hay validaciones
 * 
 * GET /api/recetas/filtrar?titulo=pollo&tiempoMax=30
 */
@Data
public class FiltroRecetaRequestDTO {
    
    private String titulo; // Búsqueda parcial
    
    private Integer categoriaId; // Filtrar por categoría
    
    private Integer tiempoMax; // Filtrar "menos de X minutos"
    
    private Integer page; // Para paginación (mejora futura)
    
    private Integer size; // Para paginación (mejora futura)
}