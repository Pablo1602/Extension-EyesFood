package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonásMauricio on 29-11-2017.
 */

public class EditMeasureBody {
    @SerializedName("idMedida")
    private int measureId;
    @SerializedName("idUsuario")
    private String userId;
    @SerializedName("medida")
    private String measure;

    public EditMeasureBody(int measureId, String userId, String measure) {
        this.measureId = measureId;
        this.userId = userId;
        this.measure = measure;
    }
}
