package entity;

/**
 * ENUM: Turnos de comida
 * Para que coincidan con ENUM de BD y simplificar planificador como hemos quedado
 * 
 * Solo dos turnos inicialmente (comida y cena) para evitar planes largos y complicaciones de vida añadidas
 * Más adelante si queremos se pueden meter más, hemos evitado dar de alta tabla por simplicidad
 */
public enum TurnoEnum {
    Comida, Cena
}