package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Store implements Serializable {
    @SerializedName("idTienda")
    private String id;
    @SerializedName("nombre")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("descripcion")
    private String description;
    @SerializedName("telefono")
    private String phone;
    private String rss;
    private String twitter;
    private String facebook;
    private String instagram;
    @SerializedName("direccion")
    private String address;
    @SerializedName("paginaWeb")
    private String web;
    @SerializedName("foto")
    private String photo;

    public Store(String id, String name, String email, String description, String phone, String rss, String twitter, String facebook, String instagram, String address, String web, String photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = description;
        this.phone = phone;
        this.rss = rss;
        this.twitter = twitter;
        this.facebook = facebook;
        this.instagram = instagram;
        this.address = address;
        this.web = web;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRss() {
        return rss;
    }

    public void setRss(String rss) {
        this.rss = rss;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
