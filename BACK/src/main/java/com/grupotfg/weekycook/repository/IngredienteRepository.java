package com.grupotfg.weekycook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.Ingrediente;

import java.util.List;

/**
 * CRUD sobre ingredientes
 * 
 * Para futura funcionalidad de filtro por ingredientes siq ueremos
 */
public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {
    
    /**
     * Busca ingrediente exacto por nombre 
     */
    Ingrediente findByNombre(String nombre);
    
    /**
     * Búsqueda contiene para autocompltar en formularios
     */
    List<Ingrediente> findByNombreContaining(String nombre);
}