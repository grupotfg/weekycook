package com.grupotfg.weekycook.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * PlanSemanalRequestDTO
 * Crea o edita un plan semanal completo
 * 
 * MapStruct necesita que el campo este en el DTO para poder mapearlo
 * notnull y size para validar entrada (seguridad y UX)
 * 
 * POST /api/planes { "nombre": "Mi plan", "semanaInicio": "2024-01-01", ... }
 */
@Data
public class PlanSemanalRequestDTO {
    
    
    @NotNull(message = "El nombre del plan es obligatorio")
    @Size(max = 150, message = "Nombre máximo 150 caracteres")
    private String nombre; // Ej: "Semana completa"
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate semanaInicio; // Lunes de la semana
    
    @NotNull(message = "El número de comensales es obligatorio 2")
    private Integer numComensales; // Default 2 tal y como hemos acordado
    
    @Size(max = 500, message = "Observaciones máximo 500 caracteres")
    private String observaciones;
    
    // Lista de items (14 elementos: 7 días × 2 turnos)
    private List<PlanItemRequestDTO> items;
}