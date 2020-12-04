package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by JonásMauricio on 04-11-2017.
 */

public class CommentBody {
    @SerializedName("colaborador")
    private String colaborador;
    @SerializedName("idColaborador")
    private String idColaborador;
    @SerializedName("comentario")
    private String comment;
    @SerializedName("fecha")
    private String fecha;

    public CommentBody(String colaborador, String idColaborador, String comment, String fecha) {
        this.colaborador = colaborador;
        this.idColaborador = idColaborador;
        this.comment = comment;
        this.fecha = fecha;
    }
}
