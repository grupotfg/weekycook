export interface User {
  id?: number;
  nombre: string;
  apellido?: string; // para que coincida con el dto
  correo: string;
  contrasena: string; // el misterio de la ñ¡¡¡ traga java pero no angular, da un parse
                      //lo vamos a dejar asi para que esto sea feliz y hacer truqui al enviarlo a java
                      //en el service
  esAdmin: boolean;
  descripcion?: string;
  numComensalesDefecto?: number;
}