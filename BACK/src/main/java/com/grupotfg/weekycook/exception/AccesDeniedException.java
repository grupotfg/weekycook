package com.grupotfg.weekycook.exception;


 //acceso denegadoooo
 
public class AccesDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccesDeniedException(String mensaje) {
        super(mensaje);
    }
}
