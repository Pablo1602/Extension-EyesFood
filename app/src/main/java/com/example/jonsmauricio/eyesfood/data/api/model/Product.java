package com.example.jonsmauricio.eyesfood.data.api.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    //Codigo
    private String codigo;
    //Nombre
    private String product_name;
    //Categorias
    private String categories;
    //Marca
    private String brands;
    //Cantidad
    private String quantity;
    //Ultima modificacion
    private Long last_modified_t;
    //Porcion
    private String serving_size;
    //Nutrientes
    private Nutriments nutriments;
    //Cantidad de aditivos
    private int additives_n;
    //Foto Grande
    private String image_front_url;
    //Foto Chica
    private String image_thumb_url;
    //Foto Ingredientes
    private String image_ingredients_url;
    //Foto Nutricion
    private String image_nutrition_url;
    //Ingredientes
    private String ingredients_text;
    //Aditivos
    private List<String> additives_tags;
    //Alergenos
    private String allergens_from_user;
    //Trazas
    private String traces_from_user;
    //Puntaje Nova (nivel de procesamiento del producto)
    private String nova_group;
    //Puntaje nutricional
    private String nutrition_grades;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Long getLast_modified_t() {
        return last_modified_t;
    }

    public void setLast_modified_t(Long last_modified_t) {
        this.last_modified_t = last_modified_t;
    }

    public String getServing_size() {
        return serving_size;
    }

    public void setServing_size(String serving_size) {
        this.serving_size = serving_size;
    }

    public Nutriments getNutriments() {
        return nutriments;
    }

    public void setNutriments(Nutriments nutriments) {
        this.nutriments = nutriments;
    }

    public int getAdditives_n() {
        return additives_n;
    }

    public void setAdditives_n(int additives_n) {
        this.additives_n = additives_n;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImage_front_url() {
        return image_front_url;
    }

    public void setImage_front_url(String image_front_url) {
        this.image_front_url = image_front_url;
    }

    public String getImage_thumb_url() {
        return image_thumb_url;
    }

    public void setImage_thumb_url(String image_thumb_url) {
        this.image_thumb_url = image_thumb_url;
    }

    public String getIngredients_text() {
        return ingredients_text;
    }

    public void setIngredients_text(String ingredients_text) {
        this.ingredients_text = ingredients_text;
    }

    public String getImage_ingredients_url() {
        return image_ingredients_url;
    }

    public void setImage_ingredients_url(String image_ingredients_url) {
        this.image_ingredients_url = image_ingredients_url;
    }

    public String getImage_nutrition_url() {
        return image_nutrition_url;
    }

    public void setImage_nutrition_url(String image_nutrition_url) {
        this.image_nutrition_url = image_nutrition_url;
    }

    public List<String> getAdditives_tags() {
        return additives_tags;
    }

    public void setAdditives_tags(List<String> additives_tags) {
        this.additives_tags = additives_tags;
    }

    public String getAllergens() {
        return allergens_from_user;
    }

    public void setAllergens(String allergens_from_user) {
        this.allergens_from_user = allergens_from_user;
    }

    public String getTraces() {
        return traces_from_user;
    }

    public void setTraces(String traces_from_user) {
        this.traces_from_user = traces_from_user;
    }

    public String getNova() {
        return nova_group;
    }

    public void setNova_group(String nova_group) {
        this.nova_group = nova_group;
    }

    public String getNutritionGrades() {
        return nutrition_grades;
    }

    public void setNutrition_grades(String nutrition_grades) {
        this.nutrition_grades = nutrition_grades;
    }
}
