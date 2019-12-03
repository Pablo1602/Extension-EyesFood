package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonásMauricio on 29-11-2017.
 */

public class InsertMeasureBody {
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("medida")
    private String measure;

    public InsertMeasureBody(String userId, String measure) {
        this.userId = userId;
        this.measure = measure;
    }

    public String getUserId() {
        return userId;
    }

    public String getMeasure() {
        return measure;
    }
}
