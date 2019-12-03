package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    Define un objeto aditivo
    Clase utilizada para mostrar la información detallada de los aditivos
*/
public class Rating implements Serializable{
    @SerializedName("idUsuario")
    private String usuario;
    @SerializedName("idExperto")
    private String experto;
    @SerializedName("valoracion")
    private float valoracion;
    //Valoracion podria ser un set

    public Rating(String usuario, String experto, float valoracion) {
        this.usuario = usuario;
        this.experto = experto;
        this.valoracion = valoracion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getExperto() {
        return experto;
    }

    public void setExperto(String experto) {
        this.experto = experto;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.usuario = valoracion;
    }

}
