package com.grupotfg.weekycook.dto.response;



import lombok.Data;

/**
 * ValorNutricionalResponseDTO
 * Totales nutricionales de una receta (ingredientes ya calculados)
 Valores calculados para solo consulta
 Redondeamos a 2 decimales para mostrar al usuario ya ok
 fechaCalculo: Indica si es necesario recalcular (mejora futura) por si queremos escalar
 
 * GET /api/recetas/{id} que incluye este DTO
 */
@Data
public class ValorNutricionalResponseDTO {
    
    private Double caloriasTotales;
    
    private Double proteinasTotales;
    
    private Double grasasTotales;
    
    private Double hidratosTotales;
    
    private String fechaCalculo; // Formateo para fron
}