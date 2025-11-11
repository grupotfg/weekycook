package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Usuario;

import java.util.Optional;

/**
 * Para hacer CRUD sobre usuarios
 * 
 * - findByCorreo: lo usamos como login para autenticación
 * - Devuelve Optional para manejar usuarios no encontrados
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    /**
     * Busca usuario por email (login)
     * @param correo Email del usuario
     * @return Optional con usuario o vacío si no existe
     */
    Optional<Usuario> findByCorreo(String correo);
}