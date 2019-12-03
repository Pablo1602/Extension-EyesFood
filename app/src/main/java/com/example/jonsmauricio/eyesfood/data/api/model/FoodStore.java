package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodStore implements Serializable {

    @SerializedName("codigoBarras")
    private String barCode;
    @SerializedName("precio")
    private String price;
    @SerializedName("peligroAlimento")
    private float foodHazard;
    @SerializedName("nombreAlimento")
    private String foodName;
    private String Photo;

    public FoodStore(String barCode, String price, float foodHazard, String foodName) {
        this.barCode = barCode;
        this.price = price;
        this.foodHazard = foodHazard;
        this.foodName = foodName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getFoodHazard() {
        return foodHazard;
    }

    public void setFoodHazard(float foodHazard) {
        this.foodHazard = foodHazard;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
