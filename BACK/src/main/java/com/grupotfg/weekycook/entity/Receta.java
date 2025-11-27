package com.grupotfg.weekycook.entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

/**
 * Receta
 * Esto es la receta completa con ingredientes, instrucciones y valores nutricionales
 * Lazy en relaciones: No carga ingredientes ni nutrición hasta que se necesita
 *   (evita muchas consultas al listar recetas, menudo lio)
 * - onetomaby con cascade: Al borrar receta, se borran sus ingredientes
 * - onetoone con cascade: Al borrar receta, borra tb su valor nutricional
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recetas")
public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titulo; 

    @Column(name = "descripcion_corta", length = 500)
    private String descripcionCorta; // Resumen para listados

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instrucciones; // Paso a paso

    @Column(name = "tiempo_preparacion_min")
    private Integer tiempoPreparacionMin; // Para filtro 

    private Integer porciones; // Por defecto 2 (quedasmo así en el anteproyecto)

    @Column(name = "foto_url")
    private String fotoUrl; // Ruta imagen (OJO!!!! a ver como lo hacemos)

    // Relación N:1 con Categoria
    // Lazy: No va la categoria completa hasta que se accede para mas sencillez y menos carga
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Relación N:1 con Usuario (creador)
    // lazy: No carga datos del usuario hasta que se accede
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // Relación 1:N con RecetaIngrediente
    // cascade.ALL: Al borrar receta, se borran todos sus ingredientes
    // @ToString.Exclude para evitar bucles
    @ToString.Exclude
    @OneToMany(mappedBy = "receta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RecetaIngrediente> recetaIngredientes;

    // Relación 1:1 con RecetaValorNutricional
    // cascade.ALL: Al borrar receta, se borra su valor nutricional ok
    @ToString.Exclude
    @OneToOne(mappedBy = "receta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RecetaValorNutricional valorNutricional;
}