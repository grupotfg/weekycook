package com.grupotfg.weekycook.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupotfg.weekycook.entity.Usuario;
import com.grupotfg.weekycook.repository.UsuarioRepository;

import java.util.Optional;

/**
 * UsuarioServiceImpl
 * Cosas concretas para operaciones del usuario
 * extiende AbstractCrudServic para heredae el CRUD genérico
 * implementa el UsuarioService con sus métodos especiales
 * 
 * va asi: Repository → Service → Mapper → Controller
 */
@Service
@Transactional // Todos los métodos son transaccionales por defecto
public class UsuarioServiceImpl extends AbstractCrudService<Usuario, Integer, UsuarioRepository> implements UsuarioService {
    
    // Repositorio ya inyectado en AbstractCrudService
    // Pero sobreescribo para acceso directo en métodos específicos
    
    private final UsuarioRepository usuarioRepository;
    
    
    //he buscado y se puede hacer asi por constructor o con el Autowired, vamos a ver que tal queda
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository; // Inyectamos en el padre
        this.usuarioRepository = usuarioRepository; // Guardamos referencia local
    }
    
    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    
    @Override
    public boolean existePorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true) // Solo lectura (optimiza performance)
    public Optional<Usuario> findByIdWithRecetas(Integer id) {
        return usuarioRepository.findByIdWithRecetas(id); // Método custom con JOIN FETCH
    }
    
    /**
     * Aqui sobreescribo create() para añadir lógica específica
     *  
     *Verificamos e.mail que sea único antes de crear
     *fechaCreacion
     *numComensalesDefecto = 2 si viene null
     */
    @Override
    public Usuario create(Usuario usuario) {
        // tiene que ser e.mail único
        if (existePorCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El email " + usuario.getCorreo() + " ya está registrado");
        }
        
        // Setear valores por defecto
        if (usuario.getNumComensalesDefecto() == null) {
            usuario.setNumComensalesDefecto(2); // hemos quedado que sea dos para hacer mejora y luego escalar raciones
        }
        
        // Super llama al método del AbstractCrudService
        return super.create(usuario);
    }
    
    /**
     * sobreescribo tb update() por seguridad
     - No permitir cambiar email a uno existente
     - No permitir cambiar fechaCreacion
     - No permitir cambiar esAdmin para rol
     */
    @Override
    public Usuario update(Integer id, Usuario usuario) {
        // Buscar el usuario
        Usuario existente = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario con ID " + id + " no encontrado"));
        
        // Si cambia e.mail, verificar que no exista
        if (!existente.getCorreo().equals(usuario.getCorreo()) && existePorCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El email " + usuario.getCorreo() + " ya está en uso");
        }
        
        // Mantenimiento de campos protegidos
        usuario.setFechaCreacion(existente.getFechaCreacion());
        usuario.setEsAdmin(existente.getEsAdmin());
        
        // ID para que base realice update
        usuario.setId(id);
        
        return super.update(id, usuario);
    }
}