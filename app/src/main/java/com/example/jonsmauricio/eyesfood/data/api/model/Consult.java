package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Consult implements Serializable {
    @SerializedName("idExperto")
    private String experto;
    @SerializedName("idUsuario")
    private String usuario;

    public Consult(String experto, String usuario) {
        this.experto = experto;
        this.usuario = usuario;
    }

    public String getExperto() {
        return experto;
    }

    public void setExperto(String experto) {
        this.experto = experto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
