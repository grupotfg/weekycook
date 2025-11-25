package com.grupotfg.weekycook.service;

import org.springframework.stereotype.Service;

import com.grupotfg.weekycook.entity.Ingrediente;
import com.grupotfg.weekycook.repository.IngredienteRepository;


@Service
public class IngredienteServiceImpl 
        extends AbstractCrudService<Ingrediente, Integer, IngredienteRepository>
        implements IngredienteService {

    // No necesitamos añadir nada más por ahora, creo, luego ya saldrán mas cosillas.
    // El CRUD completo ya viene de AbstractCrudService generico y eso.
}
