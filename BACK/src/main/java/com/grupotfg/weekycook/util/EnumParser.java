package com.grupotfg.weekycook.util;

import java.text.Normalizer;
import java.util.Arrays;

/**
 * Import porque no me salía nada
 * -------------------------------
 * Convierte Strings tipo "lunes", "LUNES", "MiErCoLeS", "miercoles"
 * a su Enum correspondiente aunque haya acentos o mayúsculas o lo que sea
 * Se usa en cualquier punto que el usuario pase dia/turno como String.
 */
public class EnumParser {

	private static String normalize(String s) {
	    if (s == null) return null;
	    String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
	    // quitar marcas acentos
	    normalized = normalized.replaceAll("\\p{M}", "");
	    // conservar letras como la ñ, quita espacios/símbolos
	    normalized = normalized.replaceAll("[^A-Za-zÑñ]", "");
	    return normalized.toLowerCase();
	}


    public static <E extends Enum<E>> E parse(Class<E> enumClass, String value) {
        String normalized = normalize(value);

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> normalize(e.name()).equals(normalized))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Valor '" + value + "' no válido para enum " + enumClass.getSimpleName()
                        )
                );
    }
}

