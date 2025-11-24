package com.grupotfg.weekycook.mapper;



import org.mapstruct.*;

import com.grupotfg.weekycook.dto.request.UsuarioRequestDTO;
import com.grupotfg.weekycook.dto.response.UsuarioResponseDTO;
import com.grupotfg.weekycook.entity.Usuario;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
    
    // UsuarioRequestDTO → Usuario (crear/modificar)
    Usuario toEntity(UsuarioRequestDTO requestDTO);
    
    // Usuario → UsuarioResponseDTO (esto devolve al cliente)
    
    UsuarioResponseDTO toDto(Usuario usuario);
    
    // Actualizar entidad desde DTO (para modifcar parcialmente)
    @Mapping(target = "id", ignore = true) // No actualiza ID
    @Mapping(target = "fechaCreacion", ignore = true) // No actualiza fecha
    @Mapping(target = "esAdmin", ignore = true) // No cambiar rol desde API
    
    void updateEntityFromDto(UsuarioRequestDTO requestDTO, @MappingTarget Usuario usuario);
    
    // Método para fechaCreacion después de que se haga el mapeo -->after
    @AfterMapping
    default void setFechaCreacion(@MappingTarget Usuario usuario) {
        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(java.time.LocalDateTime.now());
        }
    }
}