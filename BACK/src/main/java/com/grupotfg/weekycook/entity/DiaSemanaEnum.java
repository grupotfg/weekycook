package com.grupotfg.weekycook.entity;
/**
 * ENUM: Los dias de la semana
 * Para que coioncidan exactamente con los valores ENUM de la base de datos 
 * - la BD tiene y espera 'Lunes', 'Martes'...
 * - Los valores deben ser igualicos al ENUM de MySQL: ENUM('Lunes','Martes'...)
 * - hemos evitado dar de alta tabla por simplicidad
 */
public enum DiaSemanaEnum {
    Lunes, Martes, Miércoles, Jueves, Viernes, Sábado, Domingo
}
