package com.grupotfg.weekycook.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupotfg.weekycook.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
    
    
//busco como hacer query x id recetas
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.recetas WHERE u.id = :id")
    Optional<Usuario> findByIdWithRecetas(@Param("id") Integer id);
}
