package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JonásMauricio on 02-11-2017.
 */

public class FoodImage implements Serializable{
    @SerializedName("idFotoAlimento")
    private long id;
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("codigoBarras")
    private String barcode;
    @SerializedName("autorizacion")
    private String authorization;
    @SerializedName("ruta")
    private String path;
    @SerializedName("date")
    private String fecha;

    public FoodImage(long id, String userId, String barcode, String authorization, String path, String fecha) {
        this.id = id;
        this.userId = userId;
        this.barcode = barcode;
        this.authorization = authorization;
        this.path = path;
        this.fecha = fecha;
    }

    public FoodImage(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getPath() {
        return path;
    }

    public String getFecha() {
        return fecha;
    }

}
