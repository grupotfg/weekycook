package com.grupotfg.weekycook.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * Ingrediente
 * Con información nutricional base de cada ingrediente
 * 
 * Valores x unidad_base para calcular nutrición total en recetas
 * Ej: Pollo pechuga = 1.65 kcal por gramo
 * 
 * en un principo meti scale y precision pero no va en double lo cambio, sin lios
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