package com.grupotfg.weekycook.exception;

/**
 * Excepción para controlar accesos de usuarios NO administradores.
 * Se lanza cuando un usuario intenta realizar una acción que no puede
 */
public class AccesDeniedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccesDeniedException(String mensaje) {
        super(mensaje);
    }
}
