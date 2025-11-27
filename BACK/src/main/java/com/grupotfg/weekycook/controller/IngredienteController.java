package com.grupotfg.weekycook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.response.IngredienteResponseDTO;
import com.grupotfg.weekycook.entity.Ingrediente;
import com.grupotfg.weekycook.mapper.IngredienteMapper;
import com.grupotfg.weekycook.repository.IngredienteRepository;
import com.grupotfg.weekycook.service.IngredienteService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredientes")
@CrossOrigin(origins = "*")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private IngredienteMapper ingredienteMapper;
    
    @Autowired
    private IngredienteRepository ingredienteRepository;

    
     // -------Crear ingrediente
     
    @PostMapping
    public ResponseEntity<IngredienteResponseDTO> create(@RequestBody Ingrediente ingrediente) {

        Ingrediente created = ingredienteService.create(ingrediente);
        IngredienteResponseDTO dto = ingredienteMapper.toDto(created);

        URI location = URI.create("/api/ingredientes/" + created.getId());
        return ResponseEntity.created(location).body(dto);
    }

    //------Listar todos
    
    @GetMapping
    public ResponseEntity<List<IngredienteResponseDTO>> getAll() {
        List<IngredienteResponseDTO> dtos = ingredienteService.findAll()
                .stream()
                .map(ingredienteMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    //-----por ID
    
    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponseDTO> getById(@PathVariable Integer id) {
        return ingredienteService.findById(id)
                .map(ingrediente -> ResponseEntity.ok(ingredienteMapper.toDto(ingrediente)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ------Actualizar
    
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        if (!ingredienteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ingredienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search/nombre")
    public ResponseEntity<List<IngredienteResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
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