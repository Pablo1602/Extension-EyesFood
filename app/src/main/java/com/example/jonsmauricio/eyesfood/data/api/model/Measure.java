package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonásMauricio on 27-11-2017.
 */

public class Measure {
    @SerializedName("idMedida")
    private int id;
    @SerializedName("idUsuario")
    private int userId;
    @SerializedName("medida")
    private float measure;
    @SerializedName("fecha")
    private String date;

    public Measure(int id, int userId, float measure, String date) {
        this.id = id;
        this.userId = userId;
        this.measure = measure;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public float getMeasure() {
        return measure;
    }

    public String getDate() {
        return date;
    }
}
