package com.grupotfg.weekycook.dto.response;



import lombok.Data;

/**
 * RecetaResponseDTO para listado
 * Lista de recetas sin ingredientes
   Al listar ochomil recetas, no cargamos 300mil ingredientes que aqui no son necesarios
 Título, tiempo, porciones, categoría como campos
 CategoriaNombre: Evita para fornt que haga 20 llamadas extra por categoría
 Sin ingredientes que se obtienen x GET /api/recetas/{id}/ingredientes
 * 
 *GET /api/recetas (devuelve List<RecetaResponseDTO>)
 */
@Data
public class RecetaResponseDTO {
    
    private Integer id;
    
    private String titulo;
    
    private String descripcionCorta;
    
    private Integer tiempoPreparacionMin;
    
    private Integer porciones;
    
    private String fotoUrl;
    
    private Integer categoriaId;
    
    private String categoriaNombre; // Incluido para evitar llamadas extras
    
    private String creadorNombre; // Solo nombre, sin datos sensibles
}