package com.grupotfg.weekycook.mapper;

import org.mapstruct.*;
import com.grupotfg.weekycook.dto.request.PlanSemanalRequestDTO;
import com.grupotfg.weekycook.dto.response.PlanSemanalResponseDTO;
import com.grupotfg.weekycook.entity.PlanSemanal;
import java.util.List;


/**
 *PlanSemanalMapper
 *Convierte PlanSemanal y sus DTO (Request y Response).
 *
 * OJO!! he visto que MapStruct NO hace conversiones de String a LocalDate/Date/Enum en DTOs de Request.
 * tienen que ser realizadas en el Service antes de persistir la entidad.
 */
@Mapper(
        componentModel = "spring",
        uses = {PlanItemMapper.class}, // Usa el PlanItemMapper para mapeo auto de la lista de items
        unmappedTargetPolicy = ReportingPolicy.IGNORE //ignora campos que no estén e l dto
)

public interface PlanSemanalMapper {

	//Entidad PlanSemanal a su DTO de respuesta
    @Mapping(target = "usuarioId", source = "usuario.id")//Mapea el ID de 'usuario'    
    @Mapping(target = "items", source = "items")//Usa el PlanItemMapper de uses de arriba
    PlanSemanalResponseDTO toResponseDto(PlanSemanal entity);

    // ignora ignora
    @Mapping(target = "id", ignore = true) //El ID lo genera la base de datos 
    @Mapping(target = "usuario", ignore = true)//El service establece el objeto Usuario
    @Mapping(target = "fechaCreacion", ignore = true)//La fecha de creación la establece el Service tb por lo de arriba
    @Mapping(target = "items", ignore = true)//dto contiene dto hacer por service 
    PlanSemanal toEntity(PlanSemanalRequestDTO dto);

    List<PlanSemanalResponseDTO> toResponseDtoList(List<PlanSemanal> entities);
}
