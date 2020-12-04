package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Consult implements Serializable {
    @SerializedName("idExperto")
    private int experto;
    @SerializedName("idUsuario")
    private int usuario;

    public Consult(int experto, int usuario) {
        this.experto = experto;
        this.usuario = usuario;
    }

    public int getExperto() {
        return experto;
    }

    public void setExperto(int experto) {
        this.experto = experto;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
