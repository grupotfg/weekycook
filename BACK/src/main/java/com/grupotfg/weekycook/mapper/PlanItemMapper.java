package com.grupotfg.weekycook.mapper;

import com.grupotfg.weekycook.dto.request.PlanItemRequestDTO;
import com.grupotfg.weekycook.dto.response.PlanItemResponseDTO;
import com.grupotfg.weekycook.entity.PlanItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanItemMapper {

    @Mapping(source = "planSemanal.id", target = "planSemanalId")
    @Mapping(source = "receta.id", target = "recetaId")
    PlanItemResponseDTO toResponseDTO(PlanItem planItem);

    List<PlanItemResponseDTO> toResponseDTOList(List<PlanItem> planItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "planSemanal", ignore = true)
    @Mapping(target = "receta", ignore = true)
    PlanItem toEntity(PlanItemRequestDTO planItemRequestDTO);
}
