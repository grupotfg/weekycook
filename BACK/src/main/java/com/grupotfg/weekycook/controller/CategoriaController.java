package com.grupotfg.weekycook.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.response.CategoriaResponseDTO;
import com.grupotfg.weekycook.entity.Categoria;
import com.grupotfg.weekycook.mapper.CategoriaMapper;
import com.grupotfg.weekycook.service.CategoriaService;

/**
 * CategoriaController
 * No hay DTO de entrada, las categorías SOLO se crean desde backend.
 * Solo devolvemos DTOResponse.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaMapper categoriaMapper;

    // -----todos-------------
    @GetMapping
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    // ------por ID-------------
    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return categoriaMapper.toDto(categoria);
    }

    // ---------crear--------------
    @PostMapping
    public CategoriaResponseDTO crear(@RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.create(categoria);
        return categoriaMapper.toDto(nueva);
    }

    // -----actualizar-----------
    @PutMapping("/{id}")
    public CategoriaResponseDTO actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        Categoria actualizada = categoriaService.update(id, categoria);
        return categoriaMapper.toDto(actualizada);
    }

    // ---------borrar-----------
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        categoriaService.deleteById(id);
    }

    // ------buscar por nombre -----------
    @GetMapping("/search/nombre")
    public List<CategoriaResponseDTO> buscarPorNombre(@RequestParam String nombre) {
        return categoriaService.findByNombreContaining(nombre)
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    // ---buscar por descripcion contiene-------
    @GetMapping("/search/descripcion")
    public List<CategoriaResponseDTO> buscarPorDescripcion(@RequestParam String descripcion) {
        return categoriaService.findByDescripcionContaining(descripcion)
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }
}
