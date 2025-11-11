package repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import entity.Receta;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORIO: RecetaRepository
 * CRUD de recetas con filtros avanzados
 * 
 * - findByTiempoPreparacionMinLessThanEqual: Filtro "Recetas rápidas < 30min"
 * - findByTituloContainingAndCategoriaId: Filtro combinado (título + categoría)
 * - findRandom: Funcionalidad "Opción aleatoria" del planificador ****Esta mola****
 * - findByTitulo pues eso, es una busqueda exacta, evita duplicados
 */
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    
    /**
     * Búsqueda contiene por título
     */
    List<Receta> findByTituloContaining(String titulo);
    
    /**
     * Filtro por categoría (ej: mostrar solo Postres)
     */
    List<Receta> findByCategoriaId(Integer categoriaId);
    
    /**
     * Da recetas que se preparan en menos de X minutos
     * @param tiempo máximo en minutos
     */
    List<Receta> findByTiempoPreparacionMinLessThanEqual(Integer tiempo);
    
    /**
     * Búsqueda combinada
     * Filtros varios del planificador (título + categoría)
     * @param titulo Parte del título
     * @param categoriaId ID de la categoría
     */
    List<Receta> findByTituloContainingAndCategoriaId(String titulo, Integer categoriaId);
    
    /**
     * Búsqueda exacta por título
     * busca en population_weekycook.sql para evitar duplicados
     * Permite usar receta_id dinámicamente en inserts
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
