package com.grupotfg.weekycook.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: Ingrediente
 * Almacena información nutricional base de cada ingrediente
 * 
 * Valores x unidad_base para calcular nutrición total en recetas
 * Ej: Pollo pechuga = 1.65 kcal por gramo
 * 
 * precision = 10: Número TOTAL de dígitos (enteros + decimales)
 * scale = 3: Número de dígitos DECIMALES después del punto
 * Ejemplo: 1234567.890 (7 enteros + 3 decimales = 10 dígitos totales)
 * 
 * Esto define cómo se mapea el tipo Double de Java al tipo DECIMAL de MySQL
 * con precisión EXACTA de decimales, así ambos manejan lo mismo, por buenas practicas!!.
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredientes")
public class Ingrediente implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre; // Ej: "Pollo pechuga"

    @Column(name = "unidad_base", nullable = false)
    private String unidadBase; // Ej: "gr", "ml", "ud"

    //he tenido que borra scale y precision porque sql peta
    @Column(name = "calorias_por_unidad") 
    private Double caloriasPorUnidad; // kcal por unidad_base

    @Column(name = "proteinas_por_unidad")
    private Double proteinasPorUnidad; // gramos por unidad_base

    @Column(name = "grasas_por_unidad")
    private Double grasasPorUnidad; // gramos por unidad_base

    @Column(name = "hidratos_por_unidad")
    private Double hidratosPorUnidad; // gramos por unidad_base
}