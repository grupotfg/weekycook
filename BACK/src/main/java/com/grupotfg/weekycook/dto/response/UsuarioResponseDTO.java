package com.grupotfg.weekycook.dto.response;
import lombok.Data;

/**
 * UsuarioResponseDTO
 * Para los datos del usuario que se devuelven al cliente (sin contraseña)
 * No va la contraseña (x seg), esAdmin (no necesita saberlo el cliente normal)
 Lleva solamente los datos de perfil público
 ID aqui si que está incluido ya que es necesario para edición de perfil (el cliente deberia saber su ID!!!)
 
 */
@Data
public class UsuarioResponseDTO {
    
    private Integer id; // Necesario para referencias y ediciones
    
    private String correo;
    
    private String nombre;
    
    private String apellido;
    
    private String descripcion;
    
    private Integer numComensalesDefecto;
    
 // Necesario al final meter para que Angular sepa mostrar el menu correcto
    private Boolean esAdmin;
}