package com.grupotfg.weekycook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.RecetasFavoritas;

import java.util.List;
import java.util.Optional;

/**
 * Gestión de recetas favoritas por usuario
 * 
 * - existsBy...: Comprueba si ya es favorito (evita duplicados)
 * - deleteBy...: Eliminar de favoritos (funcionalidad de UI)
 * - findByUsuarioId: Muestra "Mis favoritos"
 */
public interface RecetasFavoritasRepository extends JpaRepository<RecetasFavoritas, Integer> {
    
    /**
     * Da todos los favoritos de un usuario
     * param es usuarioId ID del usuario
     */
    List<RecetasFavoritas> findByUsuarioId(Integer usuarioId);
    
    /**
     * Saca todos los usuarios que tienen como favorita una receta
     * param es recetaId ID de la receta
     */
    List<RecetasFavoritas> findByRecetaId(Integer recetaId);
    
    /**
     * Comprueba si una receta ya es favorita del usuario
     * return es true si ya es favorita
     */
    boolean existsByUsuarioIdAndRecetaId(Integer usuarioId, Integer recetaId);
    
    /**
     * Elimina favorito específico
     * Botón "Quitar de favoritos" en la interfaz o lo que sea que pongamos en el diseño
     */
    void deleteByUsuarioIdAndRecetaId(Integer usuarioId, Integer recetaId);
    
    /**
     * Muestra un favorito específico
     * podemos obtener el ID del favorito antes de borrarlo
     */
    Optional<RecetasFavoritas> findByUsuarioIdAndRecetaId(Integer usuarioId, Integer recetaId);
}