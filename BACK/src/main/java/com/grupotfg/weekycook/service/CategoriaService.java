package com.grupotfg.weekycook.service;

import com.grupotfg.weekycook.entity.Categoria;
import java.util.List;

/**
 * CategoriaService
 * Hereda CRUD genérico.
 * Con algun método específicos (buscar por nombre, filtros).
 */
public interface CategoriaService extends CrudService<Categoria, Integer> {

    Categoria findByNombre(String nombre);

    List<Categoria> findByNombreContaining(String nombre);

    List<Categoria> findByDescripcionContaining(String descripcion);
}