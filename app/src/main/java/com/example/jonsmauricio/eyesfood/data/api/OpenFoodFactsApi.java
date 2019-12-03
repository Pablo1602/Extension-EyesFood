package com.example.jonsmauricio.eyesfood.data.api;

import com.example.jonsmauricio.eyesfood.data.api.model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenFoodFactsApi {

    String BASE_URL = "https://cl.openfoodfacts.org/api/v0/product/";

    //Obtener producto desde OpenFood con su codigo de barra
    @GET("{barcode}")
    Call<ProductResponse> obtenerProducto(@Path("barcode") String barcode);
}
