package com.grupotfg.weekycook.mapper;

import com.grupotfg.weekycook.dto.request.CategoriaRequestDTO;
import com.grupotfg.weekycook.dto.response.CategoriaResponseDTO;
import com.grupotfg.weekycook.entity.Categoria;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * Mapper categoria para mapeo  entre entity y DTOs.
 */

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaResponseDTO toResponseDTO(Categoria categoria);

    List<CategoriaResponseDTO> toResponseDTOList(List<Categoria> categorias);

    Categoria toEntity(CategoriaRequestDTO categoriaRequestDTO);

    List<Categoria> toEntityList(List<CategoriaRequestDTO> categoriaRequestDTOs);
}
