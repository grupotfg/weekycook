package com.grupotfg.weekycook.util;

import java.text.Normalizer;
import java.util.Arrays;

/**
 * Utilidad muy crítica
 *  ya que al tener Enum definidos no me funcionaba nada que los usara practicamente
 * -------------------------------
 * Convierte  "lunes", "LUNES", "MiErCoLeS", "miercoles" o lo que sea que se ponga
 * a su Enum correspondiente aunque haya acentos o mayúsculas, que lo he estado buscando para solucionar.
 *
 * Se usa en eliminarItem(), addOrUpdateItem() x ej o cualquier sitio
 * donde el usuario pase dia/turno como String para que no pete y este ok.
 */
public class EnumParser {

	private static String normalize(String s) {
	    if (s == null) return null;
	    String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
	    // quitar acentos
	    normalized = normalized.replaceAll("\\p{M}", "");
	    // conservar letras como la ñ, por si aca, y quita espacios/símbolos
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
