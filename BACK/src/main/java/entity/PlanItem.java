package entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: PlanItem
 * Cada celda del plan semanal (ej: Lunes-Comida → Receta X)
 * - Usamos DiaSemanaEnum y TurnoEnum (en español) porque la base de datos tiene ENUM('Lunes',...)
 * - UNIQUE (plan_id, dia, turno): No puede haber dos recetas para el mismo día y turno
 * - LAZY: No carga receta completa hasta que se accede, más optimo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_item", uniqueConstraints = {
    @UniqueConstraint(name = "uq_plan_dia_turno", columnNames = {"plan_id","dia","turno"})
})
public class PlanItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación N:1 con PlanSemanal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanSemanal planSemanal;

    // Relación N:1 con Receta (la receta que va en esta celda)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;

    // @Enumerated(EnumType.STRING): Guarda el nombre del enum en la BD (ej: "Lunes")
    // DiaSemanaEnum: Nuestro propio enum para coincidir con la BD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemanaEnum dia;

    // TurnoEnum: "Comida" o "Cena" (coincide con ENUM de la BD)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TurnoEnum turno;

    private String notas; // Ej: "(postre para compartir)"
}