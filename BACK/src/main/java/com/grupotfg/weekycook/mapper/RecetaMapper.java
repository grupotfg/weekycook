package com.grupotfg.weekycook.mapper;

import org.mapstruct.*;

import com.grupotfg.weekycook.dto.request.RecetaRequestDTO;
import com.grupotfg.weekycook.dto.response.RecetaDetailResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaResponseDTO;
import com.grupotfg.weekycook.dto.response.ValorNutricionalResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaIngredienteResponseDTO;
import com.grupotfg.weekycook.entity.Receta;
import com.grupotfg.weekycook.entity.RecetaIngrediente;
import com.grupotfg.weekycook.entity.RecetaValorNutricional;

/**
 * RecetaMapper
 * Convierte Receta a DTO (usuando básico y detalle por ahorrar llamadas)
 * Convierte RecetaIngrediente a DTO
 * Convierte ValorNutricional a DTO
 *  Ignoramos campos no presentes en el DTO de request.
 *  IngredienteMapper se usa solo para ingrediente a DTO,
 *  pero el mapeo RecetaIngrediente a DTO se hace aquí para evitar cosas
 */
@Mapper(
    componentModel = "spring",
    uses = {CategoriaMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RecetaMapper {

    //vamos en orden:
    // 1) DTO de entrada a Entidad
   
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "recetaIngredientes", ignore = true)
    @Mapping(target = "valorNutricional", ignore = true)
    Receta toEntity(RecetaRequestDTO requestDTO);

    
    // 2) Receta a DTO listado (pero no metemos ingredientes)
    
    @Mapping(target = "categoriaId", source = "categoria.id")
    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    @Mapping(target = "creadorNombre", source = "creadoPor.nombre")
    RecetaResponseDTO toRecetaResponseDto(Receta receta);

   
    // 3) Receta a DTO detalle (con ingredientes y nutrición)
    
    @Mapping(target = "categoriaId", source = "categoria.id")
    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    @Mapping(target = "creadorNombre", source = "creadoPor.nombre")
    @Mapping(target = "creadorId", source = "creadoPor.id")
    @Mapping(target = "ingredientes", source = "recetaIngredientes")
    @Mapping(target = "valorNutricional", source = "valorNutricional")
    @Mapping(target = "fechaCreacion", source = "fechaCreacion", dateFormat = "yyyy-MM-dd HH:mm:ss")
    RecetaDetailResponseDTO toRecetaDetailDto(Receta receta);

    
    // 4) RecetaIngrediente a DTO
    // (Solo debe existir aqui para evitar mierdas con IngredienteMapper)
    
    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "ingredienteNombre", source = "ingrediente.nombre")
    @Mapping(target = "unidadBase", source = "ingrediente.unidadBase")
    RecetaIngredienteResponseDTO toRecetaIngredienteDto(RecetaIngrediente recetaIngrediente);

    
    // 5) ValorNutricional a DTO
    
    @Mapping(target = "fechaCalculo", source = "fechaCalculo", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ValorNutricionalResponseDTO toValorNutricionalDto(RecetaValorNutricional valorNutricional);
}
