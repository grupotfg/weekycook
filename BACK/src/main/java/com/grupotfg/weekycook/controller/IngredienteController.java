package com.grupotfg.weekycook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.response.IngredienteResponseDTO;
import com.grupotfg.weekycook.entity.Ingrediente;
import com.grupotfg.weekycook.mapper.IngredienteMapper;
import com.grupotfg.weekycook.repository.IngredienteRepository;
import com.grupotfg.weekycook.service.IngredienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredientes")
@CrossOrigin(origins = "*")
@Tag(name = "Ingredientes", description = "Gestión de ingredientes y búsqueda por nombre")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private IngredienteMapper ingredienteMapper;
    
    @Autowired
    private IngredienteRepository ingredienteRepository;

    
     // -------Crear ingrediente
     
    @PostMapping
    @Operation(summary = "Crear un nuevo ingrediente", description = "Crea un ingrediente y devuelve sus datos básicos")
    public ResponseEntity<IngredienteResponseDTO> create(@RequestBody Ingrediente ingrediente) {

        Ingrediente created = ingredienteService.create(ingrediente);
        IngredienteResponseDTO dto = ingredienteMapper.toDto(created);

        URI location = URI.create("/api/ingredientes/" + created.getId());
        return ResponseEntity.created(location).body(dto);
    }

    //------Listar todos
    
    @GetMapping
    @Operation(summary = "Listar todos los ingredientes", description = "Obtiene el listado completo de ingredientes")
    public ResponseEntity<List<IngredienteResponseDTO>> getAll() {

        List<IngredienteResponseDTO> dtos = ingredienteService.findAll()
                .stream()
                .map(ingredienteMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    //-----por ID
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ingrediente por ID", description = "Devuelve un ingrediente concreto por su identificador")
    public ResponseEntity<IngredienteResponseDTO> getById(@PathVariable Integer id) {

        return ingredienteService.findById(id)
                .map(ingrediente -> ResponseEntity.ok(ingredienteMapper.toDto(ingrediente)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ------Actualizar
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un ingrediente", description = "Actualiza los datos de un ingrediente existente")
    public ResponseEntity<IngredienteResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody Ingrediente ingrediente) {

        if (!ingredienteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ingrediente.setId(id); // asegurar ID
        Ingrediente updated = ingredienteService.update(id, ingrediente);

        return ResponseEntity.ok(ingredienteMapper.toDto(updated));
    }

    //-------Eliminar
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un ingrediente", description = "Elimina un ingrediente por su identificador")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        if (!ingredienteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ingredienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search/nombre")
    @Operation(summary = "Buscar ingredientes por nombre", description = "Busca ingredientes cuyo nombre contiene el texto indicado")
    public ResponseEntity<List<IngredienteResponseDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar dentro del nombre del ingrediente")
            @RequestParam String nombre) {

        List<IngredienteResponseDTO> dtos = ingredienteRepository.findByNombreContaining(nombre)
                .stream()
                .map(ingrediente -> {
                    IngredienteResponseDTO dto = new IngredienteResponseDTO();
                    dto.setId(ingrediente.getId());
                    dto.setNombre(ingrediente.getNombre());
                    dto.setUnidadBase(ingrediente.getUnidadBase());
                    dto.setCaloriasPorUnidad(ingrediente.getCaloriasPorUnidad());
                    dto.setProteinasPorUnidad(ingrediente.getProteinasPorUnidad());
                    dto.setGrasasPorUnidad(ingrediente.getGrasasPorUnidad());
                    dto.setHidratosPorUnidad(ingrediente.getHidratosPorUnidad());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}