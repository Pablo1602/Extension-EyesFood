package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/*
    Define un objeto usuario
    Clase utilizada para obtener los usuarios cuando inician sesión
*/
public class User {
    @SerializedName("idUsuario")
    private String id;
    @SerializedName("Nombre")
    private String name;
    @SerializedName("Apellido")
    private String surName;
    @SerializedName("Correo")
    private String email;
    @SerializedName("FotoUsuario")
    private String photo;
    @SerializedName("Reputacion")
    private String reputation;
    @SerializedName("FechaNacimiento")
    private String dateBirth;
    @SerializedName("Sexo")
    private String gender;
    @SerializedName("Estatura")
    private String height;
    @SerializedName("Nacionalidad")
    private String country;
    @SerializedName("rol")
    private String rol;
    private String session;

    public User(String id, String name, String surName, String email, String photo, String reputation, String dateBirth,
                String gender, String height, String country, String session, String rol) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.photo = photo;
        this.reputation = reputation;
        this.dateBirth = dateBirth;
        this.gender = gender;
        this.height = height;
        this.country = country;
        this.rol = rol;
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRol (){ return rol;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getReputation() {
        return reputation;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) { this.height = height; }

    public String getCountry() {
        return country;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRol (){ this.rol = rol;}
}