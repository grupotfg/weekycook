package com.grupotfg.weekycook.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * RecetaValorNutricionalRequestDTO
 * Valores nutricionales enviados desde el front para una receta
 */
@Data
public class RecetaValorNutricionalRequestDTO {

    @Min(value = 0, message = "Las calorías no pueden ser negativas")
    private Double calorias;

    @Min(value = 0, message = "Las proteínas no pueden ser negativas")
    private Double proteinas;

    @Min(value = 0, message = "Las grasas no pueden ser negativas")
    private Double grasas;

    @Min(value = 0, message = "Los hidratos no pueden ser negativos")
    private Double hidratos;
}
