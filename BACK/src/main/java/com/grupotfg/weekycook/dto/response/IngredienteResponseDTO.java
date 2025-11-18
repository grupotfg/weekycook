package com.grupotfg.weekycook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredienteResponseDTO {

    private Integer id;
    private String nombre;
    private String unidadBase;
    private Double caloriasPorUnidad;
    private Double proteinasPorUnidad;
    private Double grasasPorUnidad;
    private Double hidratosPorUnidad;

}
