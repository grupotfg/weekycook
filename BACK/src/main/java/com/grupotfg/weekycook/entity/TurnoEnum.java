package com.grupotfg.weekycook.entity;

/**
 * ENUM de losTurnos de comida
 * Para que coincidan con ENUM de BD y simplificar planificador como hemos quedado
 * 
 * Solo dos turnos inicialmente (comida y cena) para evitar planes largos y complicaciones de vida añadidas
 * Más adelante si queremos se pueden meter más, hemos evitado dar de alta tabla por simplicidad, que
 * tb podría ser mejora
 */
public enum TurnoEnum {
    Comida, Cena
}