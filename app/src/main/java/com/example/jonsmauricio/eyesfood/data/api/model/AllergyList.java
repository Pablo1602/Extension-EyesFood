package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AllergyList implements Serializable {
    @SerializedName("alergeno")
    private String alergeno;
    @SerializedName("estado")
    private int estado;



    public AllergyList(String alergeno, int estado) {
        this.alergeno = alergeno;
        this.estado = estado;
    }

    public String getAlergeno() {
        return alergeno;
    }

    public int getEstado() {
        return estado;
    }

}