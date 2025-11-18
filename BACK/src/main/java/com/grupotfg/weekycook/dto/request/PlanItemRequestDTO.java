package com.grupotfg.weekycook.dto.request;

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
public class PlanItemRequestDTO {

    private Integer planSemanalId;
    private Integer recetaId;
    private DiaSemanaEnum dia;
    private TurnoEnum turno;
    private String notas;

}


/*
 * Aqui dependera de que el front envie los id's. En caso contrario tendremos que dar al front un selector para elegirlos
 * 
 * o bien Cambiaremso el DTO para que envie el objectos completos ( de plansemanal y receta).
 * 
 * 
 */