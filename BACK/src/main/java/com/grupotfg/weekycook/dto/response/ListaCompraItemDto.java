package com.grupotfg.weekycook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaCompraItemDto {

    private Integer ingredienteId;
    private String nombre;
    private Double cantidadTotal;
    private String unidad;

}
