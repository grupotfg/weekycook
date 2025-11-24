package com.grupotfg.weekycook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {
	//plano solo datos básicos

    private Integer id;
    private String nombre;
    private String descripcion;

}
