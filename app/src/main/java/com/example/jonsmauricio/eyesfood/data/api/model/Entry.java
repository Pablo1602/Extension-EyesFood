package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Entry implements Serializable {
    @SerializedName("idEntrada")
    private String id;
    @SerializedName("idDiario")
    private String idDiario;
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("texto")
    private String texto;
    @SerializedName("fecha")
    private String date;

    public Entry (String id, String titulo, String texto, String fecha){
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.date = fecha;
    }

    public String getId() {
        return id;
    }
    public String getTitulo (){return titulo;}
    public String getTexto (){return texto;}
    public String getFecha (){return date;}
}
