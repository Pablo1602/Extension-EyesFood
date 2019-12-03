package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonásMauricio on 04-11-2017.
 */

public class GmailUserBody {
    @SerializedName("correo")
    private String email;

    public GmailUserBody(String email) {
        this.email = email;
    }
}
