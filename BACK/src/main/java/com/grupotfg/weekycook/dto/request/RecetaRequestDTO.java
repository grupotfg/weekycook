package com.grupotfg.weekycook.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid; // Importante para validar la lista
import lombok.Data;
import java.util.List;

/**
 * RecetaRequestDTO
 * Datos para crear/editar una receta completa
 * Se incluye la lista de ingredientes para que el ServiceImpl pueda
 * crear la receta y sus ingredientes en una sola operacio.
 */
@Data
public class RecetaRequestDTO {
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "Título máximo 200 caracteres")
    private String titulo;
    
    @Size(max = 500, message = "Descripción corta máximo 500 caracteres")
    private String descripcionCorta;
    
    @NotBlank(message = "Las instrucciones son obligatorias")
    @Size(max = 2000, message = "Instrucciones máximo 2000 caracteres")
    private String instrucciones;
    
    @NotNull(message = "El tiempo de preparación es obligatorio")
    private Integer tiempoPreparacionMin;
    
    @NotNull(message = "Las porciones son obligatorias")
    private Integer porciones;
    
    private String fotoUrl; // a ver como menajamos esto, tenemos que comentarlo!!!!!
    
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;

    
    // Esta lista es necesaria para que el ServiceImpl funcione.
    
    @Valid // Valida los objetos dentro de la lista
    private List<RecetaIngredienteRequestDTO> ingredientes;

    @Valid
    private RecetaValorNutricionalRequestDTO valorNutricional;
}