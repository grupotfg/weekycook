package com.grupotfg.weekycook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.PlanSemanal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Gestiona de planes semanales de usuarios
 * 
 * - findByUsuarioIdAndSemanaInicio: Carga el plan específico de una semana
 *   Se uspara por ver  mi plan de esta semana y modificar plan existente
 *   Sin esto, no podrímos editar un plan que ya esta guardado y seria caca
 */
public interface PlanSemanalRepository extends JpaRepository<PlanSemanal, Integer> {
    
    /**
     * Obtiene todos los planes de un usuario (historial)
     * param- usuarioId  que es ID del usuario
     */
    List<PlanSemanal> findByUsuarioId(Integer usuarioId);
    
    /**
     * Carga plan específico de una semana
     * Edita plan existente
     * Ver plan actual
     * Evita crear duplicados de la misma semana
     * 
     * param- usuarioId ID del usuario
     * param- semanaInicio del lunes de la semana
     * return Optional con plan o vacío si no existe
     */
    Optional<PlanSemanal> findByUsuarioIdAndSemanaInicio(Integer usuarioId, LocalDate semanaInicio);

    /**
     * Obtiene todos los planes de un usuario filtrando por un rango de fechas
     * de semana_inicio (inclusive).
     *
     * @param usuarioId     ID del usuario
     * @param desdeSemana   fecha de inicio del rango (semana_inicio >= desdeSemana)
     * @param hastaSemana   fecha fin del rango (semana_inicio <= hastaSemana)
     * @return lista de planes dentro del rango indicado
     */
    List<PlanSemanal> findByUsuarioIdAndSemanaInicioBetween(Integer usuarioId, LocalDate desdeSemana, LocalDate hastaSemana);
}
