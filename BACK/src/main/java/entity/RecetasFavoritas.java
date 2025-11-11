package entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: RecetasFavoritas
 * Para marcar recetas favoritas por usuario (supongo que algo como like en la aplicación o corazón... o lo que sea)
 * 
 * 
 * - UNIQUE (usuario_id, receta_id): Un usuario no puede marcar 2 veces la misma receta
 * - LAZY: No carga usuario completo ni receta completa hasta que se necesita
 * - @ToString.Exclude: Evita recursión usuario → favoritos → receta → creador → ...
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recetas_favoritas", uniqueConstraints = {
    @UniqueConstraint(name = "uq_usuario_receta", columnNames = {"usuario_id","receta_id"})
})
public class RecetasFavoritas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación N:1 con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación N:1 con Receta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;

    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado; // Cuándo se añadió a favoritos
}
