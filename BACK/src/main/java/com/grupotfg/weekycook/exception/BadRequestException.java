package com.grupotfg.weekycook.exception;

public class BadRequestException extends RuntimeException {
    
	 //peticion malaaaa
	 
	private static final long serialVersionUID = 1L;

	public BadRequestException(String msg) {
        super(msg);
    }
}
