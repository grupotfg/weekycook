package com.grupotfg.weekycook.dto.response;

import lombok.Data;

//sencillito jajajaj¡¡

@Data
public class FavoritoResponseDTO {

    private Integer idFavorito;

    private Integer recetaId;
    private String recetaTitulo;

    private String fechaGuardado;
}
