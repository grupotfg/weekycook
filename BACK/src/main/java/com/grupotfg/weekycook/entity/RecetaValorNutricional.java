package com.grupotfg.weekycook.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD: RecetaValorNutricional
 * Almacena totales nutricionales calculados de cada receta
 * 
 * 
 * - 1:1 con Receta: Cada receta tiene un único cálculo nutricional
 * - LAZY: No se carga hasta que se consulta específicamente
 * - Precisión 12,3: Más que de sobra para recetas con muchos ingredientes
 * - fecha_calculo: Permite saber si el cálculo está desactualizado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receta_valor_nutricional")
public class RecetaValorNutricional implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación 1:1 con Receta (receta_id es UNIQUE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", unique = true)
    private Receta receta;
    
    // //borro tb precision = 10, scale = 3 xq con double no traga sql

    @Column(name = "calorias_totales")
    private Double caloriasTotales;

    @Column(name = "proteinas_totales")
    private Double proteinasTotales;

    @Column(name = "grasas_totales")
    private Double grasasTotales;

    @Column(name = "hidratos_totales")
    private Double hidratosTotales;

    @Column(name = "fecha_calculo")
    private LocalDateTime fechaCalculo; // Para recalcular si es antiguo
}