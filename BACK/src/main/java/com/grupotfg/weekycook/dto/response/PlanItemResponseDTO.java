package com.grupotfg.weekycook.dto.response;

import com.grupotfg.weekycook.entity.DiaSemanaEnum;
import com.grupotfg.weekycook.entity.TurnoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanItemResponseDTO {

    private Integer id;
    private Integer planSemanalId;
    private Integer recetaId;
    private DiaSemanaEnum dia;
    private TurnoEnum turno;
    private String notas;

}
