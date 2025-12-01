package com.grupotfg.weekycook.mapper;

import com.grupotfg.weekycook.dto.request.CategoriaRequestDTO;
import com.grupotfg.weekycook.dto.response.CategoriaResponseDTO;
import com.grupotfg.weekycook.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;



 //Mapper categoria para mapeo  entre entity y DTOs.
 

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

	CategoriaResponseDTO toDto(Categoria categoria);

    List<CategoriaResponseDTO> toResponseDTOList(List<Categoria> categorias);

    @Mapping(target = "id", ignore = true)
    Categoria toEntity(CategoriaRequestDTO categoriaRequestDTO);

    List<Categoria> toEntityList(List<CategoriaRequestDTO> categoriaRequestDTOs);
}
