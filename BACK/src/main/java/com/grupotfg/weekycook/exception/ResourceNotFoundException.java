package com.grupotfg.weekycook.exception;

/**
 * 404 no vaaa
 */
public class ResourceNotFoundException extends RuntimeException {
  
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}