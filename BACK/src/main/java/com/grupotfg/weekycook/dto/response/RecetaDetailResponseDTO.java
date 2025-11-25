package com.grupotfg.weekycook.dto.response;


import lombok.Data;
import java.util.List;

/**
 * RecetaDetailResponseDTO esta es la version para el detalle cuando la necesitemos
 * Toda todita la información de una receta (para página de detalle)
 Incluye todo los de la receta ingredientes, valor nutricional, instrucciones completas para detalle
 Solo lo usamos al ver 1 receta, no al listar 20
 List<RecetaIngredienteResponseDTO>: Ingredientes con nombres para mostrar
 ValorNutricionalResponseDTO: Totales calculados (ahorro de cálculo para el front más tarde)
 * 
 * GET /api/recetas/{id} (detalle de una sola receta)
 */
@Data
public class RecetaDetailResponseDTO {
    
    private Integer id;
    
    private String titulo;
    
    private String descripcionCorta;
    
    private String instrucciones; // Texto completo para detalle
    
    private Integer tiempoPreparacionMin;
    
    private Integer porciones;
    
    private String fotoUrl;
    
    private Integer categoriaId;
    
    private String categoriaNombre;
    
    private Integer creadorId;
    
    private String creadorNombre;
    
    private String fechaCreacion; //ya ok para fron
    
    // Lista completa de ingredientes con cantidades
    private List<RecetaIngredienteResponseDTO> ingredientes;
    
    // Valor nutricional total calculado
    private ValorNutricionalResponseDTO valorNutricional;
}