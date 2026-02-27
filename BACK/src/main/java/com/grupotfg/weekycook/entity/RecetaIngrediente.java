package com.grupotfg.weekycook.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * RecetaIngrediente
 * une por N:M  Receta e Ingrediente con atributos extra
 * 
 * 
 * -embedid: Usa la clave compuesta definida arriba
 * -mapsid: lleva cada parte de la clave a su entidad correspondiente
 * -lazy: No carga receta ni ingrediente hasta que se necesita
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

    // receta_id de la clave compuesta a la entidad Receta
    @MapsId("recetaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta;

    // ingrediente_id de la clave compuesta a la entidad Ingrediente
    @MapsId("ingredienteId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    // Cantidad del ingrediente en la receta
    //borro tb precision = 10, scale = 3 xq con double no traga sql tendria que ser bigdecimal y paso, asi va ok
    @Column(nullable = false)
    private Double cantidad; // Ej: 200.00

    @Column(nullable = false)
    private String unidad; // Ej: "gr", "ml", "ud"
}