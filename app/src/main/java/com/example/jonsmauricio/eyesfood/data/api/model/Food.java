package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    Define un objeto alimento
    Clase utilizada para mostrar la información detallada de los alimentos
*/
public class Food implements Serializable{

    @SerializedName("codigoBarras")
    private String barCode;
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("idPeligroAlimento")
    private String foodHazardId;
    @SerializedName("peligroAlimento")
    private float foodHazard;
    @SerializedName("fechaSubida")
    private String date;
    @SerializedName("indiceGlicemico")
    private float glycemicIndex;
    @SerializedName("nombreAlimento")
    private String foodName;
    @SerializedName("alergenos")
    private String alergenos;
    @SerializedName("trazas")
    private String trazas;

    public Food(String barCode, String userId, float foodHazard, String date, float glycemicIndex, String foodName, String alergenos, String trazas) {
        this.barCode = barCode;
        this.userId = userId;
        this.foodHazard = foodHazard;
        this.date = date;
        this.glycemicIndex = glycemicIndex;
        this.foodName = foodName;
        this.alergenos = alergenos;
        this.trazas = trazas;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFoodHazardId() {
        return foodHazardId;
    }

    public void setFoodHazardId(String foodHazardId) {
        this.foodHazardId = foodHazardId;
    }

    public float getFoodHazard() {
        return foodHazard;
    }

    public void setFoodHazard(float foodHazard) {
        this.foodHazard = foodHazard;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getGlycemicIndex() {
        return glycemicIndex;
    }

    public void setGlycemicIndex(float glycemicIndex) {
        this.glycemicIndex = glycemicIndex;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodAlergenos() {
        return alergenos;
    }

    public void setFoodAlergenos(String alergenos) {
        this.alergenos = alergenos;
    }

    public String getFoodTrazas() {
        return trazas;
    }

    public void setFoodTrazas(String trazas) {
        this.trazas = trazas;
    }

    @Override
    public String toString() {
        return this.foodName;
    }

}