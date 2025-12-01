package com.grupotfg.weekycook.controller; 

import com.grupotfg.weekycook.dto.request.RecetaRequestDTO;
import com.grupotfg.weekycook.dto.response.RecetaDetailResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaResponseDTO;
import com.grupotfg.weekycook.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Gestión de recetas, búsquedas y administración de recetas")
public class RecetaController {

    private final RecetaService recetaService;

    // -------publico (Usuarios NO-ADMIN y ADMIN)

    
     // Obtener una lista de todas las recetas (versión pequeña sin detalle)
     // GET /api/recetas
     
    @GetMapping
    @Operation(summary = "Listar todas las recetas", description = "Obtiene una lista de todas las recetas (sin detalle completo)")
    public ResponseEntity<List<RecetaResponseDTO>> listarTodasLasRecetas() {

        return ResponseEntity.ok(recetaService.listarRecetas());
    }

     //Obtener el detalle de una receta por ID
     //GET /api/recetas/{id}
     
    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de receta", description = "Obtiene el detalle completo de una receta por su ID")
    public ResponseEntity<RecetaDetailResponseDTO> obtenerRecetaPorId(@PathVariable Integer id) {

        return ResponseEntity.ok(recetaService.obtenerRecetaPorId(id));
    }
    
    
     //Obtener una receta aleatoria por si aca
     //GET /api/recetas/random
     
    @GetMapping("/random")
    @Operation(summary = "Obtener receta aleatoria", description = "Devuelve una receta aleatoria para el planificador")
    public ResponseEntity<RecetaResponseDTO> obtenerRecetaAleatoria() {

        return ResponseEntity.ok(recetaService.obtenerRecetaAleatoria());
    }

    // Búsqueda avanzada por texto (titulo/descripcion) y categoría
    // GET /api/recetas/search?texto=...&categoriaId=...
    @GetMapping("/search")
    @Operation(summary = "Búsqueda avanzada por texto y categoría", description = "Busca recetas filtrando por texto en el título y opcionalmente por categoría")
    public ResponseEntity<List<RecetaResponseDTO>> buscarPorTextoYCategoria(
            @Parameter(description = "Texto a buscar en el título de la receta")
            @RequestParam(required = false) String texto,
            @Parameter(description = "ID de la categoría para filtrar")
            @RequestParam(required = false) Integer categoriaId) {

        List<RecetaResponseDTO> resultados = recetaService.buscarPorTextoYCategoria(texto, categoriaId);
        return ResponseEntity.ok(resultados);
    }

    // Búsqueda por ingrediente disponible (recetas que usan ese ingrediente)
    // GET /api/recetas/search/by-ingrediente?ingredienteId=...
    @GetMapping("/search/by-ingrediente")
    @Operation(summary = "Buscar recetas por ingrediente", description = "Obtiene recetas que utilizan un ingrediente concreto")
    public ResponseEntity<List<RecetaResponseDTO>> buscarPorIngrediente(
            @Parameter(description = "ID del ingrediente disponible")
            @RequestParam Integer ingredienteId) {

        List<RecetaResponseDTO> resultados = recetaService.buscarPorIngrediente(ingredienteId);
        return ResponseEntity.ok(resultados);
    }

    // ---------------Para ADMIN

    /**
     * Crear una nueva receta
     * POST /api/recetas
     * ojojojoo Pasar 'usuarioId' como requestparam es mu poco seguro
     * más adelante, deberíamos obtener el ID del usuario del TOKEN de autenticación cuando añadamos mejoras.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva receta (ADMIN)", description = "Crea una nueva receta. Requiere un usuario administrador.")
    public ResponseEntity<RecetaDetailResponseDTO> crearReceta(
            @Valid @RequestBody RecetaRequestDTO dto,
            @Parameter(description = "ID del usuario que realiza la acción (debe ser admin)")
            @RequestParam Integer usuarioId) { // <-- ahora pruebas

        RecetaDetailResponseDTO recetaCreada = recetaService.crearReceta(usuarioId, dto);
        
        // Devolver 201 Created 
        URI location = URI.create("/api/recetas/" + recetaCreada.getId());
        return ResponseEntity.created(location).body(recetaCreada);
    }

    
     //Actualizar una receta existente
     //PUT /api/recetas/{id}
     
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una receta (ADMIN)", description = "Actualiza una receta existente. Requiere un usuario administrador.")
    public ResponseEntity<RecetaDetailResponseDTO> actualizarReceta(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaRequestDTO dto,
            @Parameter(description = "ID del usuario que realiza la acción (debe ser admin)")
            @RequestParam Integer usuarioId) { // <-- ahora pruebas

        RecetaDetailResponseDTO recetaActualizada = recetaService.actualizarReceta(id, usuarioId, dto);
        return ResponseEntity.ok(recetaActualizada);
    }

    /**
     * Eliminar una receta
     * DELETE /api/recetas/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una receta (ADMIN)", description = "Elimina una receta por su ID. Requiere un usuario administrador.")
    public ResponseEntity<Void> eliminarReceta(
            @PathVariable Integer id,
            @Parameter(description = "ID del usuario que realiza la acción (debe ser admin)")
            @RequestParam Integer usuarioId) { // <-- ahora pruebas

        recetaService.eliminarReceta(id, usuarioId);
        
        // Devolver 204 No Content (éxito yujuuu¡¡, sin cuerpo)
        return ResponseEntity.noContent().build();
    }
}