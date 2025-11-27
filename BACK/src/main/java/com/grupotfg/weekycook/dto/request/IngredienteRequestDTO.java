package com.grupotfg.weekycook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IngredienteResponseDTO
 * ingrediente con valores nutricionales
 * incluye todos los valores nutricionales para cálculos en fron
 * viene pk para mostrar en la lista de ingredientes o en detalle de receta
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class IngredienteRequestDTO {


	private Integer id;
    private String nombre;
    private String unidadBase;
    private Double caloriasPorUnidad;
    private Double proteinasPorUnidad;
    private Double grasasPorUnidad;
    private Double hidratosPorUnidad;

}

