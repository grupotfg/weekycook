package entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: RecetaIngrediente
 * Tabla de unión N:M entre Receta e Ingrediente con atributos extra
 * 
 * 
 * - @EmbeddedId: Usa la clave compuesta definida arriba
 * - @MapsId: Mapea cada parte de la clave a su entidad correspondiente
 * - LAZY: No carga receta ni ingrediente hasta que se necesita
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receta_ingrediente")
public class RecetaIngrediente implements Serializable {
    private static final long serialVersionUID = 1L;

    // Clave primaria compuesta (receta_id + ingrediente_id)
    @EmbeddedId
    private RecetaIngredienteId id;

    // Mapea receta_id de la clave compuesta a la entidad Receta
    @MapsId("recetaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta;

    // Mapea ingrediente_id de la clave compuesta a la entidad Ingrediente
    @MapsId("ingredienteId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    // Cantidad del ingrediente en la receta
    @Column(nullable = false, precision = 10, scale = 3)
    private Double cantidad; // Ej: 200.00

    @Column(nullable = false)
    private String unidad; // Ej: "gr", "ml", "ud"
}