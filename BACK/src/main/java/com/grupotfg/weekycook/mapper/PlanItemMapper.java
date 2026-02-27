package com.grupotfg.weekycook.mapper;

import org.mapstruct.*;
import com.grupotfg.weekycook.dto.response.PlanItemResponseDTO;
import com.grupotfg.weekycook.entity.PlanItem;

@Mapper(
        componentModel = "spring",
        uses = {RecetaMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PlanItemMapper {
	
	//Mapeo de PlanItem a su DTO de respuesta.
	
	
	/**
     * Param es la entidad PlanItem.
     * devuelve el DTO PlanItemResponseDTO
     * receta a recetaresponse
     * El resto de campos (id, dia, turno, notas) están ok automáticamente por tener el mismo nombre.
     */

    @Mapping(target = "receta", source = "receta")
    PlanItemResponseDTO toResponseDto(PlanItem entity);
}
