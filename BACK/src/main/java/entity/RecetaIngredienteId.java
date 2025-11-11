package entity;


import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * CLASE EMBED: RecetaIngredienteId
 * Clave primaria compuesta para receta_ingrediente
 * 
 * se requiere esto para tablas de unión N:M con atributos adicionales
 * (cantidad, unidad). Combina receta_id + ingrediente_id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RecetaIngredienteId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "receta_id")
    private Integer recetaId;

    @Column(name = "ingrediente_id")
    private Integer ingredienteId;
}
