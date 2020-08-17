package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/*
    Define un objeto alimento incompleto
    Clase utilizada para mostrar la información general de un alimento en el historial
*/

public class ShortFood{
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("codigoBarras")
    private String barCode;
    @SerializedName("nombreAlimento")
    private String name;
    @SerializedName("peligroAlimento")
    private float foodHazard;
    @SerializedName("fechaEscaneo")
    private String date;
    @SerializedName("date")
    private String fecha;
    @SerializedName("fotoOficial")
    private String officialPhoto;
    @SerializedName("meGusta")
    private int like;
    @SerializedName("escaneo")
    private int list;
    @SerializedName("denuncia")
    private int denuncia;

    public ShortFood(String userId, String barCode, String name, float foodHazard, String date, String officialPhoto, int like, int list) {
        this.userId = userId;
        this.barCode = barCode;
        this.name = name;
        this.foodHazard = foodHazard;
        this.date = date;
        this.officialPhoto = officialPhoto;
        this.like = like;
        this.list = list;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    //No se si lo uso
    public void setName(String name) {
        this.name = name;
    }

    public float getFoodHazard() {
        return foodHazard;
    }

    public String getDate() {
        return date;
    }

    public String getOfficialPhoto() {
        return officialPhoto;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getList() {
        return list;
    }

    public void setOfficialPhoto(String officialPhoto) {
        this.officialPhoto = officialPhoto;
    }

    public int getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(int denuncia) {
        this.denuncia = denuncia;
    }

    @Override
    public String toString(){
        if (fecha == null){
            return this.name + " - " + this.date + " - " +this.barCode;
        }else{
            return this.name + " - " + this.fecha + " - " +this.barCode;
        }
    }
}
