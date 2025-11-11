package repository;


import org.springframework.data.jpa.repository.JpaRepository;

import entity.RecetaIngrediente;
import entity.RecetaIngredienteId;

import java.util.List;

/**
 * REPOSITORIO: RecetaIngredienteRepository
 * Acciones sobre la tabla de unión receta-ingrediente
 * 
 * 
 * - Maneja clave compuesta RecetaIngredienteId
 * - Permite obtener todos los ingredientes de una receta
 * - Útil para cálculo de valores nutricionales totales
 */
public interface RecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, RecetaIngredienteId> {
    
    /**
     * Da todos los ingredientes de una receta 
     * @param recetaId ID de la receta
     */
    List<RecetaIngrediente> findByRecetaId(Integer recetaId);
    
    /**
     * Saca todas las recetas que usan un ingrediente
     * @param ingredienteId ID del ingrediente
     */
    List<RecetaIngrediente> findByIngredienteId(Integer ingredienteId);
}
