// Define la estructura del Usuario que viene del backend (UsuarioResponseDTO)
export interface User {
  id: number;
  correo: string;
  nombre: string;
  apellido?: string;     // El '?' es opcional, ya que puede ser null en BD
  contrasena: string;
  descripcion?: string;
  numComensalesDefecto: number;
  
  // ojo En Java es 'Boolean' o 'Byte' (0/1). 
  // En el front lo vamos a tratar como boolean o number según como venga en el jason
  // Si el backend envía 0/1, será como number.
  esAdmin: boolean | number; 
}

// datos que enviamos al hacer login
export interface LoginRequest {
  correo: string;
  contrasena: string; // Evitamos la 'ñ' en el nombre de variable por buenas practicas
}

// opcional si implementamos JWT o lo que sea más adelante
export interface AuthResponse {
  token: string;
  usuario: User;
}