package com.grupotfg.weekycook.service;


import java.util.List;
import java.util.Optional;

/**
 * generico CrudService<T, ID>
 * Van las operaciones CRUD genericas y podemos usuarlas es cualquier tabla
 * 
 * T: Tipo de entidad (Usuario, Receta, PlanSemanal...)
 * ID: Tipo del ID (Integer, Long...)
 * Esto hereda la estructura
 * Poner en un sitio todo lo que es común findAll(), findById(), create(), update(), delete()
 * no ahorra mucho ya que es comun no tabla por tabla
 */
public interface CrudService<T, ID> {
    
    /**
     * Obtener todos los registros
     * return de la lista de entidades
     */
    List<T> findAll();
    
    /**
     * Buscar por ID
     * param es id Identificador
     * return da optional con la entidad o vacío
     */
    Optional<T> findById(ID id);
    
    /**
     * Crear nuevo registro
     * param es entity Entidad a crear (sin ID por como están creadas las tablas) 
     * return da la entidad guardada (ya con ID)
     */
    T create(T entity);
    
    /**
     * Actualizacion
     * param es id ID del registro que queremos actualizar
     * param es entity Datos nuevos
     * return de tabla actualizada
     */
    T update(ID id, T entity);
    
    /**
     * Elimina registro por ID
     * param es id como identificador para saber que tabla
     */
    void deleteById(ID id);
}