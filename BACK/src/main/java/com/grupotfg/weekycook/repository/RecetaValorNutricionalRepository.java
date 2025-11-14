package com.grupotfg.weekycook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.RecetaValorNutricional;

import java.util.Optional;

/**
 * va sobre los valores nutricionales calculados
 * 
 * - Métodos útiles para mostrar información nutricional en frontend cuando lo hagamos
 * - Optional evita null checks al buscar por receta_id
 */
public interface RecetaValorNutricionalRepository extends JpaRepository<RecetaValorNutricional, Integer> {
    
    /**
     * Busca valor nutricional por ID de receta
     * @param recetaId ID de la receta
     * @return Optional con datos o vacío si no calculados
     */
    Optional<RecetaValorNutricional> findByRecetaId(Integer recetaId);
}
