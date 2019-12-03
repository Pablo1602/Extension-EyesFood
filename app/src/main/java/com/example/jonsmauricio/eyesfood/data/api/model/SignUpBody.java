package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/*
    Define el body del objeto JSON enviado en el POST de registro
*/
public class SignUpBody {
    //SerializedName: Nombre exacto del atributo JSON que será interpretado
    //https://stackoverflow.com/questions/28957285/what-is-the-basic-purpose-of-serializedname-annotation-in-android-using-gson
    @SerializedName("nombre") private String name;
    @SerializedName("apellido") private String surname;
    @SerializedName("correo") private String email;
    private String password;

    public SignUpBody(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}