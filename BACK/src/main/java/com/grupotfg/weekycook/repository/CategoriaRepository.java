package com.grupotfg.weekycook.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.grupotfg.weekycook.entity.Categoria;

import java.util.List;


 //Crud sobre categorías
 //Métodos para filtros de búsqueda en recetas
 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    /**
     * Busca categoría exacta por nombre
     * param nombre es el nombre de la categoría
     * return la categoria o null
     */
    Categoria findByNombre(String nombre);
    
    /**
     * Búsqueda por contiene
     * param nombre o parte del nombre
     * return la lista de categorías coincidentes
     */
    List<Categoria> findByNombreContaining(String nombre);
    
    
     //Búsqueda por contiene eb descripción si queremos
     
    List<Categoria> findByDescripcionContaining(String descripcion);
}