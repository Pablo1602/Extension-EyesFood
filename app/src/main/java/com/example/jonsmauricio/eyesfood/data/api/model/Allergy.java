package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Allergy implements Serializable {
    @SerializedName("idAlergia")
    private int idAlergia;
    @SerializedName("idUsuario")
    private int userId;
    @SerializedName("leche")
    private int leche;
    @SerializedName("gluten")
    private int gluten;



    public Allergy(int idAlergia, int userId, int leche, int gluten) {
        this.idAlergia = idAlergia;
        this.userId = userId;
        this.leche = leche;
        this.gluten = gluten;
    }

    public int getId() {
        return idAlergia;
    }

    public int getUserId() {
        return userId;
    }

    public int getLeche() {
        return leche;
    }

    public int getGluten() {
        return gluten;
    }
}

