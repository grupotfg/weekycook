package com.grupotfg.weekycook.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

/**
 * PlanSemanal
 * Representa un plan semanal (Lunes a Domingo).
 * Cada plan pertenece a un usuario y tiene 14 items.
 *
 * orphanRemoval = true:
 *     Al eliminar o sustituir un item, JPA lo borra automáticamente.
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String nombre;

    @Column(name = "semana_inicio", nullable = false)
    private LocalDate semanaInicio;

    @Column(name = "num_comensales")
    private Integer numComensales;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "planSemanal",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PlanItem> items = new ArrayList<>();

    
    public void addItem(PlanItem item) {
        items.add(item);
        item.setPlanSemanal(this);
    }

    public void removeItem(PlanItem item) {
        items.remove(item);
        item.setPlanSemanal(null);
    }
}
