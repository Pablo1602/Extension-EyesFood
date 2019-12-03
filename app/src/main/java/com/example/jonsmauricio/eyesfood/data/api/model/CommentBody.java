package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

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

    public CommentBody(String colaborador, String idColaborador, String comment) {
        this.colaborador = colaborador;
        this.idColaborador = idColaborador;
        this.comment = comment;
    }
}
