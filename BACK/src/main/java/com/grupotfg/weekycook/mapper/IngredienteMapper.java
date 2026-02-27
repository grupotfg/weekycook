package com.grupotfg.weekycook.mapper;

import com.grupotfg.weekycook.dto.request.IngredienteRequestDTO;
import com.grupotfg.weekycook.dto.response.IngredienteResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaIngredienteResponseDTO;
import com.grupotfg.weekycook.entity.Ingrediente;
import com.grupotfg.weekycook.entity.RecetaIngrediente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;



//Mapper ingredientes para mapeo  entre entity y DTOs.


@Mapper(componentModel = "spring")
public interface IngredienteMapper {

	 // De entidad a Response DTO
	 // Ingrediente a IngredienteResponseDTO
    IngredienteResponseDTO toDto(Ingrediente ingrediente);

    List<IngredienteResponseDTO> toResponseDTOList(List<Ingrediente> ingredientes);

    // De Request DTO a entidad
    Ingrediente toEntity(IngredienteRequestDTO ingredienteRequestDTO);

    List<Ingrediente> toEntityList(List<IngredienteRequestDTO> ingredienteRequestDTOs);
    
    // RecetaIngrediente → RecetaIngredienteResponseDTO
    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "ingredienteNombre", source = "ingrediente.nombre")
    @Mapping(target = "unidadBase", source = "ingrediente.unidadBase")
    RecetaIngredienteResponseDTO toRecetaIngredienteDto(RecetaIngrediente recetaIngrediente);
    
}
