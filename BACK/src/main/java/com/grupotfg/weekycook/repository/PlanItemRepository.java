package com.grupotfg.weekycook.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.PlanItem;

import java.util.List;
import java.util.Optional;

/**
 * 
 * Gestiona las celdas individuales del plan semanal --> los item
 * 
 - findByPlanSemanalId: Obtener todas los item de un plan (para mostrar en tabla que lo saque)
 - findByRecetaId: Buscar en qué planes aparece una receta X 
 */
public interface PlanItemRepository extends JpaRepository<PlanItem, Integer> {
    
    /**
     * Obtiene todos lo item (días/turnos) de un plan específico
     * @param planId ID del plan semanal
     */
    List<PlanItem> findByPlanSemanalId(Integer planId);
    
    /**
     * Busca todos los items que contienen una receta específica
     * @param recetaId ID de la receta
     */
    List<PlanItem> findByRecetaId(Integer recetaId);
    
    /**
     * Podemos comprobar si ya hay recta asignada a un día/turno
     * @param planId ID del plan
     * @param dia Día de la semana (String corresp del enum)
     * @param turno Turno (String del enum)
     */
    Optional<PlanItem> findByPlanSemanalIdAndDiaAndTurno(Integer planId, String dia, String turno);
}
