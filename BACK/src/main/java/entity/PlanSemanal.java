package entity;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: PlanSemanal
 * Plan de comidas de Lunes a Domingo para un usuario
 * 
 * 
 * - semana_inicio: Fecha del lunes de esa semana (permite agrupar por semanas)
 - num_comensales: Para escalado de raciones (mejora futura)
 - cascade.ALL: Al borrar plan, se borran todos sus items automáticamente
 - @ToString.Exclude: Evita recursión plan → items → receta → plan...
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_semanal")
public class PlanSemanal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación N:1 con Usuario (un usuario tiene muchos planes)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String nombre; // Ej: "Semana completa"

    @Column(name = "semana_inicio", nullable = false)
    private LocalDate semanaInicio; // Lunes de esa semana

    @Column(name = "num_comensales")
    private Integer numComensales; // Valor por defecto del plan

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones; // Notas adicionales del planificador

    // Relación 1:N con PlanItem (un plan tiene 14 items: 7 días × 2 turnos)
    // cascade.ALL: Borra items al borrar plan
    @ToString.Exclude
    @OneToMany(mappedBy = "planSemanal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlanItem> items;
}