package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    Define un objeto aditivo
    Clase utilizada para mostrar la información detallada de los aditivos
*/
public class Expert implements Serializable{
    @SerializedName("idExperto")
    private int expertId;
    @SerializedName("nombre")
    private String name;
    @SerializedName("apellido")
    private String lastName;
    @SerializedName("correo")
    private String email;
    @SerializedName("foto")
    private String photo;
    @SerializedName("especialidad")
    private String specialty;
    @SerializedName("telefono")
    private String phone;
    @SerializedName("direccion")
    private String adress;
    @SerializedName("descripcion")
    private String description;
    @SerializedName("paginaWeb")
    private String webPage;
    @SerializedName("reputacion")
    private float reputation;
    private int foods;

    public Expert(int expertId, String name, String lastName, String email, String photo, String specialty, String phone,
                  String adress, String description, String webPage, float reputation) {
        this.expertId = expertId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.photo = photo;
        this.specialty = specialty;
        this.phone = phone;
        this.adress = adress;
        this.description = description;
        this.webPage = webPage;
        this.reputation = reputation;
    }

    public int getExpertId() {
        return expertId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return adress;
    }

    public String getDescription() {
        return description;
    }

    public String getWebPage() {
        return webPage;
    }

    public float getReputation() {
        return reputation;
    }

    public int getFoods() {
        return foods;
    }

    public void setFoods(int foods) {
        this.foods = foods;
    }
}
