package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.PlanSemanal;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

/**
 * Gestión de planes semanales de usuarios
 * 
 * - findByUsuarioIdAndSemanaInicio: Carga el plan específico de una semana
 *   Se uspara "Ver mi plan de esta semana" y "Modificar plan existente"
 *   Sin esto, no podrímos editar un plan que ya esta guardado
 */
public interface PlanSemanalRepository extends JpaRepository<PlanSemanal, Integer> {
    
    /**
     * Obtiene todos los planes de un usuario (historial)
     * @param usuarioId ID del usuario
     */
    List<PlanSemanal> findByUsuarioId(Integer usuarioId);
    
    /**
     * Cargar plan específico de una semana
     * - Edita plan existente
     * - Ver plan actual
     * - Evita crear duplicados de la misma semana
     * 
     * @param usuarioId ID del usuario
     * @param semanaInicio Fecha del lunes de la semana
     * @return Optional con plan o vacío si no existe
     */
    Optional<PlanSemanal> findByUsuarioIdAndSemanaInicio(Integer usuarioId, LocalDate semanaInicio);
}
