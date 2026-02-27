package com.grupotfg.weekycook.dto.response;



import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * PlanSemanalResponseDTO
 * Plan semanal completo con todos sus datos
 * 
 va con items (días/turnos) para poder mostrar en el fron la tabla corresp
 UsuarioId evita que el usuario vea planes de otro por seg
 TotalCaloriasSemana es un campo calculado en el Service (no en la entidad)
 * 
 * GET /api/planes/{id} (devuelve plan completo)
 */
@Data
public class PlanSemanalResponseDTO {
    
    private Integer id;
    
    private Integer usuarioId;
    
    private String nombre;
    
    private String semanaInicio; // Formateado ok para fron
    
    private Integer numComensales;
    
    private String fechaCreacion;
    
    private String observaciones;
    
    private List<PlanItemResponseDTO> items; // Todos los días/turnos
    
    // Campo calculado en el Service
    private BigDecimal totalCaloriasSemana;
}