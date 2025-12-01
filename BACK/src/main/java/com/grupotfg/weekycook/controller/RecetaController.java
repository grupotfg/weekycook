package com.grupotfg.weekycook.controller; 

import com.grupotfg.weekycook.dto.request.RecetaRequestDTO;
import com.grupotfg.weekycook.dto.response.RecetaDetailResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaResponseDTO;
import com.grupotfg.weekycook.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;

    
    // -------publico (Usuarios NO-ADMIN y ADMIN)
    

    
     // Obtener una lista de todas las recetas (versión pequeña sin detalle)
     // GET /api/recetas
     
    @GetMapping
    public ResponseEntity<List<RecetaResponseDTO>> listarTodasLasRecetas() {
        return ResponseEntity.ok(recetaService.listarRecetas());
    }

     //Obtener el detalle de una receta por ID
     //GET /api/recetas/{id}
     
    @GetMapping("/{id}")
    public ResponseEntity<RecetaDetailResponseDTO> obtenerRecetaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(recetaService.obtenerRecetaPorId(id));
    }
    
    
     //Obtener una receta aleatoria por si aca
     //GET /api/recetas/random
     
    @GetMapping("/random")
    public ResponseEntity<RecetaResponseDTO> obtenerRecetaAleatoria() {
        return ResponseEntity.ok(recetaService.obtenerRecetaAleatoria());
    }

    // Búsqueda avanzada por texto (titulo/descripcion) y categoría
    // GET /api/recetas/search?texto=...&categoriaId=...
    @GetMapping("/search")
    public ResponseEntity<List<RecetaResponseDTO>> buscarPorTextoYCategoria(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Integer categoriaId) {

        List<RecetaResponseDTO> resultados = recetaService.buscarPorTextoYCategoria(texto, categoriaId);
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
    public ResponseEntity<RecetaDetailResponseDTO> crearReceta(
            @Valid @RequestBody RecetaRequestDTO dto,
            @RequestParam Integer usuarioId) { // <-- ahora pruebas

        RecetaDetailResponseDTO recetaCreada = recetaService.crearReceta(usuarioId, dto);
        
        // Devolver 201 Created 
        URI location = URI.create("/api/recetas/" + recetaCreada.getId());
        return ResponseEntity.created(location).body(recetaCreada);
    }

    
     //Actualizar una receta existente
     //PUT /api/recetas/{id}
     
    @PutMapping("/{id}")
    public ResponseEntity<RecetaDetailResponseDTO> actualizarReceta(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaRequestDTO dto,
            @RequestParam Integer usuarioId) { // <-- ahora pruebas

        RecetaDetailResponseDTO recetaActualizada = recetaService.actualizarReceta(id, usuarioId, dto);
        return ResponseEntity.ok(recetaActualizada);
    }

    /**
     * Eliminar una receta
     * DELETE /api/recetas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReceta(
            @PathVariable Integer id,
            @RequestParam Integer usuarioId) { // <-- ahora pruebas

        recetaService.eliminarReceta(id, usuarioId);
        
        // Devolver 204 No Content (éxito yujuuu¡¡, sin cuerpo)
        return ResponseEntity.noContent().build();
    }
}