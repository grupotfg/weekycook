package com.grupotfg.weekycook.entity;



import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: Categoria
 * Clasifica recetas (Desayuno, Italiana, Vegano, etc.)
 * 
 * Es una simple entidad, no tiene relaciones inversas. No necesita @ToString.Exclude
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Italiana", "Postres" ...

    private String descripcion; // Para explicar la categoría
}
