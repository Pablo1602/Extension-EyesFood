package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Help implements Serializable{
    @SerializedName("idArticuloAyuda")
    private int idHelp;
    @SerializedName("titulo")
    private String title;
    @SerializedName("texto")
    private String text;
    @SerializedName("fecha")
    private String date;

    public Help(int idHelp, String title, String text, String date) {
        this.idHelp = idHelp;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public int getIdHelp() {
        return idHelp;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString(){
        return title;
    }
}
