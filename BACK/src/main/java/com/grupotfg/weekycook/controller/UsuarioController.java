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

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


// * Rest Controller para Usuario, endpoints de usuario con funciones crud básicas

@RestController
@RequestMapping("/api/usuarios")
@Validated
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    /**
     * Crea usuario
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {

        Usuario toCreate = usuarioMapper.toEntity(request);
        Usuario created = usuarioService.create(toCreate);

        UsuarioResponseDTO dto = usuarioMapper.toDto(created);

        URI location = URI.create("/api/usuarios/" + created.getId());
        return ResponseEntity.created(location).body(dto);
    }

    /**
     * Lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<UsuarioResponseDTO> dtos = usuarioService.findAll()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Pedir usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(usuarioMapper.toDto(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO request) {

        Usuario existente = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario con ID " + id + " no encontrado"));

        usuarioMapper.updateEntityFromDto(request, existente);

        Usuario actualizado = usuarioService.update(id, existente);

        return ResponseEntity.ok(usuarioMapper.toDto(actualizado));
    }

    /**
     * Eliminar usuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
