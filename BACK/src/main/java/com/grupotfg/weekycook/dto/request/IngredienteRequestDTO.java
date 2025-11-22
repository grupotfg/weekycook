package com.grupotfg.weekycook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class IngredienteRequestDTO {

    private String nombre;
    private String unidadBase;
    private Double caloriasPorUnidad;
    private Double proteinasPorUnidad;
    private Double grasasPorUnidad;
    private Double hidratosPorUnidad;

}
