package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notification implements Serializable {
    @SerializedName("idNotificacion")
    private String id;
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("texto")
    private String texto;
    @SerializedName("push")
    private String push;
    @SerializedName("habilitar")
    private String habilitar;
    @SerializedName("fecha")
    private String fecha;

    public Notification(String id, String titulo, String texto, String push, String habilitar, String fecha){
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.push = push;
        this.habilitar = habilitar;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }
    public String getTitulo (){return titulo;}
    public String getTexto (){return texto;}
    public String getPush (){return push;}
    public String getHabilitar (){return habilitar;}
    public String getFecha (){return fecha;}
}
