package com.grupotfg.weekycook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.request.UsuarioRequestDTO;
import com.grupotfg.weekycook.dto.response.UsuarioResponseDTO;
import com.grupotfg.weekycook.entity.Usuario;
import com.grupotfg.weekycook.mapper.UsuarioMapper;
import com.grupotfg.weekycook.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.stream.Collectors;


// * Rest Controller para Usuario, endpoints de usuario con funciones crud básicas

@RestController
@RequestMapping("/usuarios")
@Validated
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Gestión de usuarios de la aplicación (CRUD básico)")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    
     //-----Crea usuario
     
    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario de la aplicación")
    public ResponseEntity<UsuarioResponseDTO> createUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {

        Usuario toCreate = usuarioMapper.toEntity(request);
        Usuario created = usuarioService.create(toCreate);

        UsuarioResponseDTO dto = usuarioMapper.toDto(created);

        URI location = Objects.requireNonNull(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri()
        );
        return ResponseEntity.created(location).body(dto);
    }

    
    //-----Lista de usuarios
     
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene el listado completo de usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<UsuarioResponseDTO> dtos = usuarioService.findAll()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    
     //-----Pedir usuario por ID
     
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario concreto por su identificador")
    public ResponseEntity<UsuarioResponseDTO> getById(
            @Parameter(description = "Identificador del usuario a obtener", required = true) @PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(usuarioMapper.toDto(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    
     //----Actualizar usuario
     
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    public ResponseEntity<UsuarioResponseDTO> update(
            @Parameter(description = "Identificador del usuario a actualizar", required = true) @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO request) {

        Usuario existente = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario con ID " + id + " no encontrado"));

        usuarioMapper.updateEntityFromDto(request, existente);

        Usuario actualizado = usuarioService.update(id, existente);

        return ResponseEntity.ok(usuarioMapper.toDto(actualizado));
    }

    
    //-----Eliminar usuario
     
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador del usuario a eliminar", required = true) @PathVariable Integer id) {

        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
