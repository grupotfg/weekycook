package com.grupotfg.weekycook.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * UsuarioRequestDTO
 * Datos que el cliente envía al registrar/editar un usuario
 * quito el id (ya que se genera automáticamente en la base), fechaCreacion (que va en back), 
 * esAdmin 0/1n no hay rol como tal 
 * Solo campos modificables por el usuario
 * Sin contraseña codificada: Se recibe plana, integración más segura en mejoras posteriores
 * 
 */
@Data
public class UsuarioRequestDTO {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String correo; // Para login
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "Nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "Apellido máximo 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "Contraseña mínimo 6 caracteres")
    private String contraseña; // Se guarda como {noop} x ahora
    
    @Size(max = 500, message = "Descripción máximo 500 caracteres")
    private String descripcion;
    
    private Integer numComensalesDefecto; // x Defecto: 2, para escalado de recetas cuando lo hagamos
}