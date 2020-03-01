package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Diary implements Serializable {
    @SerializedName("idDiario")
    private String id;
    @SerializedName("titulo")
    private String titulo;

    public Diary (String id, String titulo){
        this.id = id;
        this.titulo = titulo;
    }


    public String getId() {
        return id;
    }
    public String getTitulo (){return titulo;}
}
