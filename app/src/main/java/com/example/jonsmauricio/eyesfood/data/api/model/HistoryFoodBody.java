package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/*
    Define el cuerpo JSON del método POST para recibir un alimento para el historial
*/
public class HistoryFoodBody {
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("codigoBarras")
    private String barCode;

    public HistoryFoodBody(String userId, String barCode) {
        this.userId = userId;
        this.barCode = barCode;
    }
}
