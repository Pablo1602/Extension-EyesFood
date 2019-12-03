package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
    Define un objeto ingrediente, el cual puede ser ingrediente o aditivo
    Se usa para tratar de igual forma a ingredientes y aditivos y poder unirlos en una lista
*/
public class Ingredient {
    @SerializedName("idIngrediente")
    private String idIngredient;
    @SerializedName("ingrediente")
    private String ingredient;
    @SerializedName("orden")
    private int order;

    public Ingredient(String idIngredient, String ingredient, int order) {
        this.idIngredient = idIngredient;
        this.ingredient = ingredient;
        this.order = order;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString(){
        return idIngredient + " - " + ingredient;
    }
}

