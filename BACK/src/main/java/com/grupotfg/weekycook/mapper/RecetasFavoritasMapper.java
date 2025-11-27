package com.grupotfg.weekycook.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.grupotfg.weekycook.dto.request.FavoritoRequestDTO;
import com.grupotfg.weekycook.dto.response.FavoritoResponseDTO;
import com.grupotfg.weekycook.entity.RecetasFavoritas;

@Mapper(componentModel = "spring")
public interface RecetasFavoritasMapper {

    // RequestDTO a Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "receta", ignore = true)
    @Mapping(target = "fechaGuardado", ignore = true)
    RecetasFavoritas toEntity(FavoritoRequestDTO requestDTO);

    // Entity a ResponseDTO
    @Mapping(source = "id", target = "idFavorito")
    @Mapping(source = "receta.id", target = "recetaId")
    @Mapping(source = "receta.titulo", target = "recetaTitulo")
    @Mapping(target = "fechaGuardado", expression = "java(favorito.getFechaGuardado().toString())")
    FavoritoResponseDTO toResponseDTO(RecetasFavoritas favorito);
}
