package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Nutriments  implements Serializable {

    //Energia
    private String energy_100g;
    private String energy_serving;
    private String energy_value;
    private String energy_unit;

    //Grasa Total
    private float fat_100g;
    private float fat_serving;
    private String fat_unit;

    //Acidos Grasos Saturados
    @SerializedName("saturated-fat_100g")
    private float saturatedFat100g;
    @SerializedName("saturated-fat_serving")
    private float saturatedFatServing;
    @SerializedName("saturated-fat_unit")
    private String saturatedFatUnit;

    //Grasas Monoinsaturadas
    @SerializedName("monounsaturated-fat_100g")
    private float monounsaturated_fat_100g;
    @SerializedName("monounsaturated-fat_serving")
    private float monounsaturated_fat_serving;
    @SerializedName("monounsaturated-fat_unit")
    private String monounsaturated_fat_unit;

    //Grasas Poliinsaturadas
    @SerializedName("polyunsaturated-fat_100g")
    private float polyunsaturated_fat_100g;
    @SerializedName("polyunsaturated-fat_serving")
    private float polyunsaturated_fat_serving;
    @SerializedName("polyunsaturated-fat_unit")
    private String polyunsaturated_fat_unit;

    //Grasas Trans
    @SerializedName("trans-fat_100g")
    private float trans_fat_100g;
    @SerializedName("trans-fat_serving")
    private float trans_fat_serving;
    @SerializedName("trans-fat_unit")
    private String trans_fat_unit;

    //Colesterol
    private float cholesterol_100g;
    private float cholesterol_serving;
    private String cholesterol_unit;

    //Hidratos de Carbono Total
    private float carbohydrates_100g;
    private float carbohydrates_serving;
    private String carbohydrates_unit;

    //Proteinas
    private float proteins_100g;
    private float proteins_serving;
    private String proteins_unit;

    //Azucares Totales
    private float sugars_100g;
    private float sugars_serving;
    private String sugars_unit;

    //Fibra
    private float fiber_100g;
    private float fiber_serving;
    private String fiber_unit;

    //Sodio
    private float sodium_100g;
    private float sodium_serving;
    private String sodium_unit;

    //Aditivos
    private ArrayList<String> additives_original_tags;

    public String getEnergy_100g() {
        return energy_100g;
    }

    public void setEnergy_100g(String energy_100g) {
        this.energy_100g = energy_100g;
    }

    public String getEnergy_serving() {
        return energy_serving;
    }

    public void setEnergy_serving(String energy_serving) {
        this.energy_serving = energy_serving;
    }

    public String getEnergy_value() {
        return energy_value;
    }

    public void setEnergy_value(String energy_value) {
        this.energy_value = energy_value;
    }

    public String getEnergy_unit() {
        return energy_unit;
    }

    public void setEnergy_unit(String energy_unit) {
        this.energy_unit = energy_unit;
    }

    public float getFat_100g() {
        return fat_100g;
    }

    public void setFat_100g(float fat_100g) {
        this.fat_100g = fat_100g;
    }

    public float getFat_serving() {
        return fat_serving;
    }

    public void setFat_serving(float fat_serving) {
        this.fat_serving = fat_serving;
    }

    public String getFat_unit() {
        return fat_unit;
    }

    public void setFat_unit(String fat_unit) {
        this.fat_unit = fat_unit;
    }

    public float getCholesterol_100g() {
        return cholesterol_100g;
    }

    public void setCholesterol_100g(float cholesterol_100g) {
        this.cholesterol_100g = cholesterol_100g;
    }

    public float getCholesterol_serving() {
        return cholesterol_serving;
    }

    public void setCholesterol_serving(float cholesterol_serving) {
        this.cholesterol_serving = cholesterol_serving;
    }

    public String getCholesterol_unit() {
        return cholesterol_unit;
    }

    public void setCholesterol_unit(String cholesterol_unit) {
        this.cholesterol_unit = cholesterol_unit;
    }

    public float getCarbohydrates_100g() {
        return carbohydrates_100g;
    }

    public void setCarbohydrates_100g(float carbohydrates_100g) {
        this.carbohydrates_100g = carbohydrates_100g;
    }

    public float getCarbohydrates_serving() {
        return carbohydrates_serving;
    }

    public void setCarbohydrates_serving(float carbohydrates_serving) {
        this.carbohydrates_serving = carbohydrates_serving;
    }

    public String getCarbohydrates_unit() {
        return carbohydrates_unit;
    }

    public void setCarbohydrates_unit(String carbohydrates_unit) {
        this.carbohydrates_unit = carbohydrates_unit;
    }

    public float getSugars_100g() {
        return sugars_100g;
    }

    public void setSugars_100g(float sugars_100g) {
        this.sugars_100g = sugars_100g;
    }

    public float getSugars_serving() {
        return sugars_serving;
    }

    public void setSugars_serving(float sugars_serving) {
        this.sugars_serving = sugars_serving;
    }

    public String getSugars_unit() {
        return sugars_unit;
    }

    public void setSugars_unit(String sugars_unit) {
        this.sugars_unit = sugars_unit;
    }

    public float getFiber_100g() {
        return fiber_100g;
    }

    public void setFiber_100g(float fiber_100g) {
        this.fiber_100g = fiber_100g;
    }

    public float getFiber_serving() {
        return fiber_serving;
    }

    public void setFiber_serving(float fiber_serving) {
        this.fiber_serving = fiber_serving;
    }

    public String getFiber_unit() {
        return fiber_unit;
    }

    public void setFiber_unit(String fiber_unit) {
        this.fiber_unit = fiber_unit;
    }

    public float getSodium_100g() {
        return sodium_100g;
    }

    public void setSodium_100g(float sodium_100g) {
        this.sodium_100g = sodium_100g;
    }

    public float getSodium_serving() {
        return sodium_serving;
    }

    public void setSodium_serving(float sodium_serving) {
        this.sodium_serving = sodium_serving;
    }

    public String getSodium_unit() {
        return sodium_unit;
    }

    public void setSodium_unit(String sodium_unit) {
        this.sodium_unit = sodium_unit;
    }


    public ArrayList<String> getAdditives_original_tags() {
        return additives_original_tags;
    }

    public void setAdditives_original_tags(ArrayList<String> additives_original_tags) {
        this.additives_original_tags = additives_original_tags;
    }

    public float getSaturatedFat100g() {
        return saturatedFat100g;
    }

    public void setSaturatedFat100g(float saturatedFat100g) {
        this.saturatedFat100g = saturatedFat100g;
    }

    public float getSaturatedFatServing() {
        return saturatedFatServing;
    }

    public void setSaturatedFatServing(float saturatedFatServing) {
        this.saturatedFatServing = saturatedFatServing;
    }

    public String getSaturatedFatUnit() {
        return saturatedFatUnit;
    }

    public void setSaturatedFatUnit(String saturatedFatUnit) {
        this.saturatedFatUnit = saturatedFatUnit;
    }

    public float getMonounsaturated_fat_100g() {
        return monounsaturated_fat_100g;
    }

    public void setMonounsaturated_fat_100g(float monounsaturated_fat_100g) {
        this.monounsaturated_fat_100g = monounsaturated_fat_100g;
    }

    public float getMonounsaturated_fat_serving() {
        return monounsaturated_fat_serving;
    }

    public void setMonounsaturated_fat_serving(float monounsaturated_fat_serving) {
        this.monounsaturated_fat_serving = monounsaturated_fat_serving;
    }

    public String getMonounsaturated_fat_unit() {
        return monounsaturated_fat_unit;
    }

    public void setMonounsaturated_fat_unit(String monounsaturated_fat_unit) {
        this.monounsaturated_fat_unit = monounsaturated_fat_unit;
    }

    public float getPolyunsaturated_fat_100g() {
        return polyunsaturated_fat_100g;
    }

    public void setPolyunsaturated_fat_100g(float polyunsaturated_fat_100g) {
        this.polyunsaturated_fat_100g = polyunsaturated_fat_100g;
    }

    public float getPolyunsaturated_fat_serving() {
        return polyunsaturated_fat_serving;
    }

    public void setPolyunsaturated_fat_serving(float polyunsaturated_fat_serving) {
        this.polyunsaturated_fat_serving = polyunsaturated_fat_serving;
    }

    public String getPolyunsaturated_fat_unit() {
        return polyunsaturated_fat_unit;
    }

    public void setPolyunsaturated_fat_unit(String polyunsaturated_fat_unit) {
        this.polyunsaturated_fat_unit = polyunsaturated_fat_unit;
    }

    public float getTrans_fat_100g() {
        return trans_fat_100g;
    }

    public void setTrans_fat_100g(float trans_fat_100g) {
        this.trans_fat_100g = trans_fat_100g;
    }

    public float getTrans_fat_serving() {
        return trans_fat_serving;
    }

    public void setTrans_fat_serving(float trans_fat_serving) {
        this.trans_fat_serving = trans_fat_serving;
    }

    public String getTrans_fat_unit() {
        return trans_fat_unit;
    }

    public void setTrans_fat_unit(String trans_fat_unit) {
        this.trans_fat_unit = trans_fat_unit;
    }

    public float getProteins_100g() {
        return proteins_100g;
    }

    public void setProteins_100g(float proteins_100g) {
        this.proteins_100g = proteins_100g;
    }

    public float getProteins_serving() {
        return proteins_serving;
    }

    public void setProteins_serving(float proteins_serving) {
        this.proteins_serving = proteins_serving;
    }

    public String getProteins_unit() {
        return proteins_unit;
    }

    public void setProteins_unit(String proteins_unit) {
        this.proteins_unit = proteins_unit;
    }
}
