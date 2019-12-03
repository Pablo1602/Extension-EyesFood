package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    Define el cuerpo JSON del método POST para recibir un alimento para el historial
*/
public class NewFoodBody implements Serializable {
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("codigoBarras")
    private String barcode;
    @SerializedName("nombreAlimento")
    private String name;
    @SerializedName("producto")
    private String product;
    @SerializedName("marca")
    private String brand;
    @SerializedName("contenidoNeto")
    private String content;
    @SerializedName("porcion")
    private String portion;
    @SerializedName("porcionGramos")
    private String portionUnit;
    @SerializedName("energia")
    private String energy;
    @SerializedName("proteinas")
    private String protein;
    @SerializedName("grasaTotal")
    private String totalFat;
    @SerializedName("grasaSaturada")
    private String saturatedFat;
    @SerializedName("grasaMono")
    private String monoFat;
    @SerializedName("grasaPoli")
    private String poliFat;
    @SerializedName("grasaTrans")
    private String transFat;
    @SerializedName("colesterol")
    private String cholesterol;
    @SerializedName("hidratosCarbono")
    private String carbo;
    @SerializedName("azucaresTotales")
    private String totalSugar;
    @SerializedName("fibra")
    private String fyber;
    @SerializedName("sodio")
    private String sodium;
    @SerializedName("ingredientes")
    private String ingredients;
    @SerializedName("date")
    private String date;
    @SerializedName("estadoAlimento")
    private String estado;

    public NewFoodBody(String userId, String barcode, String name, String product, String brand, String content,
                       String portion, String portionUnit, String energy, String protein, String totalFat,
                       String saturatedFat, String monoFat, String poliFat, String transFat, String cholesterol,
                       String carbo, String totalSugar, String fyber, String sodium, String ingredients, String date, String estadoAlimento) {
        this.userId = userId;
        this.barcode = barcode;
        this.name = name;
        this.product = product;
        this.brand = brand;
        this.content = content;
        this.portion = portion;
        this.portionUnit = portionUnit;
        this.energy = energy;
        this.protein = protein;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.monoFat = monoFat;
        this.poliFat = poliFat;
        this.transFat = transFat;
        this.cholesterol = cholesterol;
        this.carbo = carbo;
        this.totalSugar = totalSugar;
        this.fyber = fyber;
        this.sodium = sodium;
        this.ingredients = ingredients;
        this.date = date;
        this.estado = estadoAlimento;
    }

    public NewFoodBody(String codigo) {
        this.barcode = codigo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public String getPortionUnit() {
        return portionUnit;
    }

    public void setPortionUnit(String portionUnit) {
        this.portionUnit = portionUnit;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(String totalFat) {
        this.totalFat = totalFat;
    }

    public String getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(String saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public String getMonoFat() {
        return monoFat;
    }

    public void setMonoFat(String monoFat) {
        this.monoFat = monoFat;
    }

    public String getPoliFat() {
        return poliFat;
    }

    public void setPoliFat(String poliFat) {
        this.poliFat = poliFat;
    }

    public String getTransFat() {
        return transFat;
    }

    public void setTransFat(String transFat) {
        this.transFat = transFat;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getCarbo() {
        return carbo;
    }

    public void setCarbo(String carbo) {
        this.carbo = carbo;
    }

    public String getTotalSugar() {
        return totalSugar;
    }

    public void setTotalSugar(String totalSugar) {
        this.totalSugar = totalSugar;
    }

    public String getFyber() {
        return fyber;
    }

    public void setFyber(String fyber) {
        this.fyber = fyber;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.date + " - "+this.barcode;
    }
}
