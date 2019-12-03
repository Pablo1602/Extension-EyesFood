package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonásMauricio on 06-11-2017.
 */

public class GmailRegisterBody {
    @SerializedName("nombre")
    private String name;
    @SerializedName("apellido")
    private String lastName;
    @SerializedName("correo")
    private String email;
    @SerializedName("foto")
    private String photo;

    public GmailRegisterBody(String name, String lastName, String email, String photo) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.photo = photo;
    }
}
