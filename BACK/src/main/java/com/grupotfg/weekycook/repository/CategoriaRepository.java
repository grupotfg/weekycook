package com.grupotfg.weekycook.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.Categoria;

import java.util.List;

/**
 * CRUD sobre categorías
 * 
 * Métodos para filtros de búsqueda en recetas
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    /**
     * Busca categoría exacta por nombre
     * @param nombre Nombre de la categoría
     * @return Categoria o null
     */
    Categoria findByNombre(String nombre);
    
    /**
     * Búsqueda por contiene
     * @param nombre /o Parte del nombre
     * @return Lista de categorías coincidentes
     */
    List<Categoria> findByNombreContaining(String nombre);
    
    /**
     * Búsqueda por descripción si queremos
     */
    List<Categoria> findByDescripcionContaining(String descripcion);
}