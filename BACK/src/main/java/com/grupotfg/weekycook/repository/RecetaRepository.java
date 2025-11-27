package com.grupotfg.weekycook.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grupotfg.weekycook.entity.Receta;

import java.util.List;
import java.util.Optional;

/**
 * RecetaRepository
 * CRUD de recetas con filtros mejores
 * 
 * findByTiempoPreparacionMinLessThanEqual: Filtro "Recetas rápidas < 30min" si lo usuamos al final¡
 * findByTituloContainingAndCategoriaId: Filtro conjunto (título + categoría)
 * findRandom: para "Opción aleatoria" del planificador ****Esta nos mola****
 * findByTitulo pues eso, es una busqueda exacta, evita duplicados
 */
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    
    
     //Búsqueda contiene por título
     
    List<Receta> findByTituloContaining(String titulo);
    
    
     //Filtro por categoría (ej: mostrar solo Postres)
     
    List<Receta> findByCategoriaId(Integer categoriaId);
    
    
     //Da recetas que se preparan en menos de X minutos
     //param es tiempo máximo en minutos
     
    List<Receta> findByTiempoPreparacionMinLessThanEqual(Integer tiempo);
    
    /**
     * Búsqueda combinada
     * Filtros varios del planificador (título + categoría)
     * param es titulo Parte del título
     * param es categoriaId ID de la categoría
     */
    List<Receta> findByTituloContainingAndCategoriaId(String titulo, Integer categoriaId);
    
    /**
     * Búsqueda exacta por título
     * Esta la he estado trasteando y buscando en varios sitio que por lo visto viene bien
     */
    Optional<Receta> findByTitulo(String titulo);
    
    /**
     * Receta aleatoria
     * Funcionalidad "Opción aleatoria" del planificador ****ESTA ES IMPORTANTE*****
     * Permite generar planes semanales automáticamente, hasta que lo he visto jajajaaj!!!
     */
    @Query(value = "SELECT * FROM recetas ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Receta> findRandom();
}
