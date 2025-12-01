package com.grupotfg.weekycook.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.grupotfg.weekycook.dto.request.CategoriaRequestDTO;
import com.grupotfg.weekycook.dto.response.CategoriaResponseDTO;
import com.grupotfg.weekycook.entity.Categoria;
import com.grupotfg.weekycook.mapper.CategoriaMapper;
import com.grupotfg.weekycook.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * CategoriaController
 * No hay DTO de entrada, las categorías SOLO se crean desde back.
 * Solo devolvemos DTOResponse.
 */
@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de recetas y búsquedas por nombre o descripción")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaMapper categoriaMapper;

    // -----todos-------------
    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Obtiene el listado completo de categorías")
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    // ------por ID-------------
    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Devuelve una categoría concreta por su identificador")
    public CategoriaResponseDTO buscarPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return categoriaMapper.toDto(categoria);
    }

    // ---------crear--------------
    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Crea una categoría a partir de un DTO de entrada")
    public CategoriaResponseDTO crear(@RequestBody CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = categoriaMapper.toEntity(categoriaRequestDTO);
        Categoria nueva = categoriaService.create(categoria);
        return categoriaMapper.toDto(nueva);
    }

    // -----actualizar-----------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza los datos de una categoría existente a partir de un DTO de entrada")
    public CategoriaResponseDTO actualizar(@PathVariable Integer id, @RequestBody CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = categoriaMapper.toEntity(categoriaRequestDTO);
        categoria.setId(id);
        Categoria actualizada = categoriaService.update(id, categoria);
        return categoriaMapper.toDto(actualizada);
    }

    // ---------borrar-----------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría por su identificador")
    public void eliminar(@PathVariable Integer id) {
        categoriaService.deleteById(id);
    }

    // ------buscar por nombre -----------
    @GetMapping("/search/nombre")
    @Operation(summary = "Buscar categorías por nombre", description = "Busca categorías cuyo nombre contiene el texto indicado")
    public List<CategoriaResponseDTO> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre de la categoría")
            @RequestParam String nombre) {
        return categoriaService.findByNombreContaining(nombre)
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }

    // ---buscar por descripcion contiene-------
    @GetMapping("/search/descripcion")
    @Operation(summary = "Buscar categorías por descripción", description = "Busca categorías cuya descripción contiene el texto indicado")
    public List<CategoriaResponseDTO> buscarPorDescripcion(
            @Parameter(description = "Texto a buscar en la descripción de la categoría")
            @RequestParam String descripcion) {
        return categoriaService.findByDescripcionContaining(descripcion)
                .stream()
                .map(categoriaMapper::toDto)
                .toList();
    }
}
