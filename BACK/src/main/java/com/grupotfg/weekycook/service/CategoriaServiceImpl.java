package com.grupotfg.weekycook.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.grupotfg.weekycook.entity.Categoria;
import com.grupotfg.weekycook.repository.CategoriaRepository;


/**
 * CategoriaServiceImpl
 * Extiende AbstractCrudService (que ya tiene CRUD completo).
 * - Solo implementamos métodos específicos el resto viene
 */
@Service
public class CategoriaServiceImpl 
        extends AbstractCrudService<Categoria, Integer, CategoriaRepository>
        implements CategoriaService {

    @Override
    public Categoria findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    @Override
    public List<Categoria> findByNombreContaining(String nombre) {
        return repository.findByNombreContaining(nombre);
    }

    @Override
    public List<Categoria> findByDescripcionContaining(String descripcion) {
        return repository.findByDescripcionContaining(descripcion);
    }
}