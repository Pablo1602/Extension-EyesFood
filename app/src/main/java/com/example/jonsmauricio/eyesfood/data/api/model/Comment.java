package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Comment implements Serializable {
    @SerializedName("idComentario")
    private String id;
    @SerializedName("idColaborador")
    private String idColaborador;
    @SerializedName("colaborador")
    private String colaborador;
    @SerializedName("comentario")
    private String comment;
    @SerializedName("fecha")
    private String date;
    @SerializedName("borrar")
    private String borrar;
    @SerializedName("referencia")
    private String referencia;
    @SerializedName("idRespuesta")
    private String idRespuesta;


    public Comment(String id, String idColaborador, String colaborador, String comment, String date, String borrar, String referencia, String idRespuesta) {
        this.id = id;
        this.idColaborador = idColaborador;
        this.colaborador = colaborador;
        this.comment = comment;
        this.date = date;
        this.borrar = borrar;
        this.referencia = referencia;
        this.idRespuesta = idRespuesta;
    }

    public String getId() {
        return id;
    }

    public String getIdColaborador() {
        return idColaborador;
    }

    public String getColaborador() {
        return colaborador;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getBorrar() {
        return borrar;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getidRespuesta() {return idRespuesta;}
}
