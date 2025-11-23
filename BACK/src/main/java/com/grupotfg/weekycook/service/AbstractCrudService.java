package com.grupotfg.weekycook.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * IMPLEMENTACIÓN ABSTRACTA: AbstractCrudService<T, ID, R>
 * Con esto se mete la implementación base del CRUD genérico
 * 
 * R: Tipo del repositorio (extends JpaRepository<T, ID>)
 * OJO IMPORT!!!! @Autowired protected repository: Inyección del repositorio específico
 * GenericRepository: Acceso abstracto a métodos JPA (save, findById, delete)
 * Cada servicio concreto extiende esta clase y hereda el CRUD completo

 */
public abstract class AbstractCrudService<T, ID, R extends JpaRepository<T, ID>> implements CrudService<T, ID> {
    
    // Repositorio inyectado. Es 'protected' para que sus hijos puedan usarlo
    @Autowired
    protected R repository;
    
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    
    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }
    
    @Override
    public T create(T entity) {
        // ID debe ser null para crear por la tabla, si no es update
        return repository.save(entity);
    }
    
    @Override
    public T update(ID id, T entity) {
        // Primero verificamos que existe, es importante par act
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Registro con ID " + id + " no existe");
        }
        // Esto debería variar según la entidad, debe sobreescribir en concreto
        return repository.save(entity);
    }
    
    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}