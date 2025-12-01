package com.grupotfg.weekycook.service;

import java.util.List;
import com.grupotfg.weekycook.dto.request.RecetaRequestDTO;
import com.grupotfg.weekycook.dto.response.RecetaDetailResponseDTO;
import com.grupotfg.weekycook.dto.response.RecetaResponseDTO;

public interface RecetaService {

    
     //Obtener todas las recetas para listas
     
    List<RecetaResponseDTO> listarRecetas();

    
     //Obtener el detalle completo de una receta por su ID con detail
     
    RecetaDetailResponseDTO obtenerRecetaPorId(Integer id);

    
     //Obtener una receta aleatoria (para el planificador)
     
    RecetaResponseDTO obtenerRecetaAleatoria();

    // Búsqueda avanzada por texto (titulo/descripcion) y categoría
    List<RecetaResponseDTO> buscarPorTextoYCategoria(String texto, Integer categoriaId);

    // Búsqueda por ingrediente disponible (recetas que usan ese ingrediente)
    List<RecetaResponseDTO> buscarPorIngrediente(Integer ingredienteId);

    // Búsqueda por id de ingrediente
    RecetaResponseDTO buscarPorIdIngrediente(Integer ingredienteId);

    // --- Métodos de Admin

    /**
     * Crear una nueva receta (Solo Admin)
     * param es usuarioId ID del usuario que realiza la acción (para check de admin)
     * param es dto DTO con los datos de la receta
     */
    RecetaDetailResponseDTO crearReceta(Integer usuarioId, RecetaRequestDTO dto);

    /**
     * Actualizar una receta existente (Solo Admin)
     * param es recetaId ID de la receta a modificar
     * param es usuarioId ID del usuario que realiza la acción
     * param es dto DTO con los nuevos datos
     */
    RecetaDetailResponseDTO actualizarReceta(Integer recetaId, Integer usuarioId, RecetaRequestDTO dto);

    /**
     * Eliminar una receta (Solo Admin)
     * param es recetaId ID de la receta a eliminar
     * param es usuarioId ID del usuario que realiza la acción
     */
    void eliminarReceta(Integer recetaId, Integer usuarioId);
}