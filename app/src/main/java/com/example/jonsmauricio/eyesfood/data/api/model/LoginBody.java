package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/*
    Define el body del objeto JSON utilizado para el POST de inicio de sesión
*/
public class LoginBody {
    //SerializedName: Nombre exacto del atributo JSON que será interpretado
    //https://stackoverflow.com/questions/28957285/what-is-the-basic-purpose-of-serializedname-annotation-in-android-using-gson
    @SerializedName("correo")
    private String userId;
    private String password;

    public LoginBody(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}