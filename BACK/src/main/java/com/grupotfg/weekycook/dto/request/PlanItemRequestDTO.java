package com.grupotfg.weekycook.dto.request;

import com.grupotfg.weekycook.entity.DiaSemanaEnum;
import com.grupotfg.weekycook.entity.TurnoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PlanItemRequestDTO
 * Cambio día y turno como STRING para evitar errores en front, no me salía nada,
 *  al final con parse pra service
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanItemRequestDTO {

    
	  private Integer recetaId;
	    private String dia;   // "Lunes, Martes..."
	    private String turno; // "Comida/Cena"
	    private String notas;

}


/*
 * Aqui dependera de que el front envie los id's. En caso contrario tendremos que dar al front un selector para elegirlos
 * 
 * o bien Cambiaremso el DTO para que envie el objectos completos ( de plansemanal y receta).
 * 
 * 
 */