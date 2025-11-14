package com.grupotfg.weekycook.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: Usuario
 * Para usuario de WeekyCook (comensal o administrador)
 * 
 * 
 * - @ToString.Exclude en relaciones: Evita StackOverflowError cuando Lombok genera toString()
 *   en relaciones bidireccionales (ej: Usuario → Receta → Usuario → ...)
 * - FetchType.LAZY: No carga recetas/favoritos/planes hasta que se accede específicamente
 *   Mejora rendimiento y evita N+1 queries
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String correo; // Usado para login

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column(nullable = false)
    private String contraseña; // Se mantiene {noop} por ahora (mejora futura: BCrypt)

    @Column(name = "es_admin", nullable = false)
    private Boolean esAdmin = false; // 0=usuario normal, 1=administrador

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(columnDefinition = "TEXT")
    private String descripcion; // Perfil de usuario

    @Column(name = "num_comensales_defecto")
    private Integer numComensalesDefecto; // Para escalado de raciones (mejora futura)

    // Relación 1:N con Receta (un usuario puede crear muchas recetas)
    // @ToString.Exclude: Evita un bucle infinito usuario → receta → usuario → ...
    // FetchType.LAZY: No carga lista de recetas hasta que se llama getRecetas()
    @ToString.Exclude
    @OneToMany(mappedBy = "creadoPor", fetch = FetchType.LAZY)
    private List<Receta> recetas;

    // Relación 1:N con RecetasFavoritas (un usuario tiene muchos favoritos)
    @ToString.Exclude
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<RecetasFavoritas> favoritos;

    // Relación 1:N con PlanSemanal (un usuario crea muchos planes)
    @ToString.Exclude
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<PlanSemanal> planes;
}