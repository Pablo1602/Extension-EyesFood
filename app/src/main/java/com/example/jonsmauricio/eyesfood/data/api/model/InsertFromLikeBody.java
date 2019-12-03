package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/*
    Define el cuerpo JSON del método POST para recibir un alimento para el historial
*/
public class InsertFromLikeBody{
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("codigoBarras")
    private String barCode;
    @SerializedName("meGusta")
    private int like;

    public InsertFromLikeBody(String userId, String barCode, int like) {
        this.userId = userId;
        this.barCode = barCode;
        this.like = like;
    }
}
