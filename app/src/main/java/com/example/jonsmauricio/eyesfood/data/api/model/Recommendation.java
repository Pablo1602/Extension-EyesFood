package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    Define un objeto recomendación
    Clase utilizada para mostrar las recomendaciones de alimentos
*/
public class Recommendation implements Serializable{
    @SerializedName("recomendacion")
    private String recommendation;

    public Recommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getRecommendation() {
        return recommendation;
    }
}