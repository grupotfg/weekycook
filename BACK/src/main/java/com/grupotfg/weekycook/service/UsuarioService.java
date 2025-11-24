package com.grupotfg.weekycook.service;


import java.util.Optional;

import com.grupotfg.weekycook.entity.Usuario;

/**
 * UsuarioService
 * Para operaciones CRUD con lógica específica de usuarios
 * MÉTODOS ESPECÍFICOS:
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
     * @param correo Email del usuario
     * @return Optional con usuario o vacío
     */
    Optional<Usuario> findByCorreo(String correo);
    
    /**
     * Verifica si un e.mail ya está registrado
     * @param correo e.mail a verificar
     * @return true si existe
     */
    boolean existePorCorreo(String correo);
    
    /**
     * Carga usuario con su lista de recetas (evita N+1 query)
     * @param id ID del usuario
     * @return Usuario con recetas cargadas
     */
    Optional<Usuario> findByIdWithRecetas(Integer id);
}