package com.grupotfg.weekycook.mapper;

import com.grupotfg.weekycook.dto.request.IngredienteRequestDTO;
import com.grupotfg.weekycook.dto.response.IngredienteResponseDTO;
import com.grupotfg.weekycook.entity.Ingrediente;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper ingredientes para mapeo  entre entity y DTOs.
 */

@Mapper(componentModel = "spring")
public interface IngredienteMapper {

   // De entidad a Response DTO
    IngredienteResponseDTO toResponseDTO(Ingrediente ingrediente);

    List<IngredienteResponseDTO> toResponseDTOList(List<Ingrediente> ingredientes);

    // De Request DTO a entidad
    Ingrediente toEntity(IngredienteRequestDTO ingredienteRequestDTO);

    List<Ingrediente> toEntityList(List<IngredienteRequestDTO> ingredienteRequestDTOs);
}
