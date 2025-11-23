package com.grupotfg.weekycook.service;


import java.util.List;
import java.util.Optional;

/**
 * INTERFAZ GENÉRICA: CrudService<T, ID>
 * Define operaciones CRUD genericas y podemos usuarlas es cualquier tabla
 * 
 * T: Tipo de entidad (Usuario, Receta, PlanSemanal...)
 * ID: Tipo del ID (Integer, Long...)
 * Esto hereda la estructura
 * Poner en un sitio todo lo que es común findAll(), findById(), create(), update(), delete()
 */
public interface CrudService<T, ID> {
    
    /**
     * Obtener todos los registros
     * @return Lista de entidades
     */
    List<T> findAll();
    
    /**
     * Buscar por ID
     * @param id Identificador
     * @return Optional con la entidad o vacío
     */
    Optional<T> findById(ID id);
    
    /**
     * Crear nuevo registro
     * @param entity Entidad a crear (sin ID por como están creadas las tablas) 
     * @return Entidad guardada (ya con ID)
     */
    T create(T entity);
    
    /**
     * Actualizacion
     * @param id ID del registro que queremos actualizar
     * @param entity Datos nuevos
     * @return tabla actualizada
     */
    T update(ID id, T entity);
    
    /**
     * Elimina registro por ID
     * @param id Identificador para saber que tabla
     */
    void deleteById(ID id);
}