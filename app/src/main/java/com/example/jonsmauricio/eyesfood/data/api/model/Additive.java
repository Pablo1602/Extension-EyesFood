package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
    Define un objeto aditivo
    Clase utilizada para mostrar la información detallada de los aditivos
*/
public class Additive implements Serializable{
    @SerializedName("codigoE")
    private String eCode;
    @SerializedName("gradoPeligro")
    private String hazard;
    @SerializedName("origen")
    private String source;
    @SerializedName("clasificacion")
    private String classification;
    @SerializedName("aditivo")
    private String additive;
    @SerializedName("descripcionAditivo")
    private String description;
    @SerializedName("usoAditivo")
    private String usage;
    @SerializedName("efectosSecundariosAditivo")
    private String secondaryEffects;

    public Additive(String eCode, String hazard, String source, String classification, String additive, String description,
                    String usage, String secondaryEffects) {
        this.eCode = eCode;
        this.hazard = hazard;
        this.source = source;
        this.classification = classification;
        this.additive = additive;
        this.description = description;
        this.usage = usage;
        this.secondaryEffects = secondaryEffects;
    }

    public String geteCode() {
        return eCode;
    }

    public String getHazard() {
        return hazard;
    }

    public String getSource() {
        return source;
    }

    public String getClassification() {
        return classification;
    }

    public String getAdditive() {
        return additive;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getSecondaryEffects() {
        return secondaryEffects;
    }

    @Override
    public String toString(){
        return additive;
    }
}
