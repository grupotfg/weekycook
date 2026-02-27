package com.grupotfg.weekycook.service;


import java.util.Optional;

import com.grupotfg.weekycook.entity.Usuario;

/**
 * UsuarioService
 * Para operaciones CRUD con lógica específica de usuarios
 * Metodos propios:
 * findByCorreo: Para login (busca por email)
 * existePorCorreo: Validar si email ya registrado
 * findByIdWithRecetas: Cargar usuario con su lista de recetas
 * ¡¡¡¡OJO podemos tb meter buscar por contiene!!!!
 * Extiende CrudService generico que hereda findAll(), findById(), create(), update(), delete()
 * todo lo necesario para hacer las cosas de siempre
 */
public interface UsuarioService extends CrudService<Usuario, Integer> {
    
    /**
     * Busca un usuario por email (que es el del login)
     * param es correo Email del usuario
     * return da Optional con usuario o vacío
     */
    Optional<Usuario> findByCorreo(String correo);
    
    /**
     * Verifica si un e.mail ya está registrado
     * param es correo e.mail a verificar
     * return da true si existe
     */
    boolean existePorCorreo(String correo);
    
    /**
     * Carga usuario con su lista de recetas (evita N+1 query)
     * param es id ID del usuario
     * return da Usuario con recetas cargadas
     */
    Optional<Usuario> findByIdWithRecetas(Integer id);
}