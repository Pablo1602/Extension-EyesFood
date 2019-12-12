package com.example.jonsmauricio.eyesfood.data.api;

import com.example.jonsmauricio.eyesfood.data.api.model.Additive;
import com.example.jonsmauricio.eyesfood.data.api.model.Consult;
import com.example.jonsmauricio.eyesfood.data.api.model.Counter;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodImage;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodStore;
import com.example.jonsmauricio.eyesfood.data.api.model.GmailRegisterBody;
import com.example.jonsmauricio.eyesfood.data.api.model.GmailUserBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Help;
import com.example.jonsmauricio.eyesfood.data.api.model.HistoryFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Ingredient;
import com.example.jonsmauricio.eyesfood.data.api.model.InsertFromLikeBody;
import com.example.jonsmauricio.eyesfood.data.api.model.LoginBody;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Rating;
import com.example.jonsmauricio.eyesfood.data.api.model.Recommendation;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.api.model.SignUpBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Store;
import com.example.jonsmauricio.eyesfood.data.api.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*
    API de Eyes Food
    Interfaz utilizada para exponer todos los métodos de comunicación con la API
*/

public interface EyesFoodApi {

    //Cambiar host por "10.0.0.2" para Genymotion.
    //Cambiar host por "10.0.0.3" para AVD.
    //Cambiar host por IP de tu PC para dispositivo real.
    // Esta es la ip de usach alumnos
    // public static final String BASE_URL = " ";
    //URL API LOCAL
    //URL antigua https://eyesfood.000webhostapp.com/api.eyesfood.cl/v1/
    //URL nueva https://dinky-frequency.000webhostapp.com/api.eyesfood.cl/v1/
    String BASE_URL = "https://eyesfoodapi.herokuapp.com/api.eyesfood.cl/v1/";
    String BASE_URL_PHOTO = "https://eyesfoodapi.herokuapp.com/api.eyesfood.cl/v1/img/users/";
    String BASE_URL_ADMIN = "https://eyesfoodweb.herokuapp.com/EyesFoodWeb/";
    String ADDITIVE_URL = "https://cl.openfoodfacts.org/aditivo/";
    //URL API WEB
    //String BASE_URL = "https://eyesfood.000webhostapp.com/api.eyesfood.cl/v1/";

    //Esta petición tiene un parámetro del tipo @Body que es un LoginBody llamado loginBody
    @POST("users/login")
    Call<User> login(@Body LoginBody loginBody);

    @POST("users/register")
    Call<User> signUp(@Body SignUpBody signUpBody);

    //Petición que retorna un alimento completo mediante su código de barras
    @GET("users/{code}")
    Call<User> getUser(@Path("code") String barcode);

    //Petición que retorna un alimento completo mediante su código de barras
    @GET("foods/{barcode}")
    Call<Food> getFood(@Path("barcode") String barcode);

    //Petición que retorna los ingredientes de un alimento
    @GET("foods/{barcode}/ingredients")
    Call<List<Ingredient>> getIngredients(@Path("barcode") String barcode);

    //Petición que retorna los aditivos de un alimento
    @GET("foods/{barcode}/additives")
    Call<List<Ingredient>> getAdditives(@Path("barcode") String barcode);

    //Petición que retorna las recomendaciones de un alimento mediante su código de barras
    @GET("foods/{barcode}/recommendations")
    Call<List<Recommendation>> getRecommendations(@Path("barcode") String barcode);

    //Petición que retorna las ediciones aceptadas de un alimento mediante su código de barras
    @GET("foods/{barcode}/edits")
    Call<List<NewFoodBody>> getEdits(@Path("barcode") String barcode);

    //Petición que retorna los alimentos pendientes de subida de un usuario
    @GET("foods/{userId}/new")
    Call<List<NewFoodBody>> getNewFoods(@Path("userId") String userId);

    //Petición que retorna los alimentos pendientes de subida de un usuario
    @GET("foods/{userId}/rejected")
    Call<List<NewFoodBody>> getNewFoodsRejected(@Path("userId") String userId);

    //Petición que retorna los alimentos creados por el usuario y aceptados
    @GET("foods/{userId}/create")
    Call<List<ShortFood>> getNewFoodsAccepted(@Path("userId") String userId);

    //Petición que consulta si el usuario tiene un alimento en su historial de escaneo
    @GET("history/{userId}/{barcode}")
    Call<ShortFood> isInHistory(@Path("userId") String userId, @Path("barcode") String barcode);

    //Petición que retorna los alimentos del historial de usuario
    @GET("history/{userId}")
    Call<List<ShortFood>> getFoodsInHistory(@Path("userId") String userId);

    //Petición que retorna los alimentos del historial de subida del usuario
    @GET("history/{userId}/uploads")
    Call<List<ShortFood>> getFoodsUploads(@Path("userId") String userId);

    //Petición que retorna los alimentos destacados del usuario
    @GET("history/{userId}/favorites")
    Call<List<ShortFood>> getFoodsFavorites(@Path("userId") String userId);

    //Petición que retorna los alimentos rechazados del usuario
    @GET("history/{userId}/rejected")
    Call<List<ShortFood>> getFoodsRejected(@Path("userId") String userId);

    //Petición que inserta un alimento en el historial
    @POST("history/create")
    Call<Food> insertInHistory(@Body HistoryFoodBody historyFoodBody);

    //Petición que actualiza un alimento en el historial
    //No se usa Patch porque no resulta
    @Headers("Content-Type: application/json")
    @POST("history/{userId}/{barcode}")
    Call<ShortFood> modifyHistory(@Path("userId") String userId, @Path("barcode") String barcode);

    //Petición que actualiza un alimento en el historial
    //No se usa Patch porque no resulta
    @Headers("Content-Type: application/json")
    @POST("history/{userId}/{barcode}/scan")
    Call<ShortFood> modifyHistoryScan(@Path("userId") String userId, @Path("barcode") String barcode);

    //Petición que denuncia un alimento
    //No se usa Patch porque no resulta
    @Headers("Content-Type: application/json")
    @POST("foods/{barcode}/report")
    Call<ShortFood> modifyReport(@Path("barcode") String barcode);

    //Petición que retorna los aditivos de un alimento con todos sus datos
    @GET("foods/{barcode}/additives/full")
    Call<List<Additive>> getFullAdditives(@Path("barcode") String barcode);

    //Petición que retorna alimentos desde una búsqueda
    @GET("search/foods/{query}")
    Call<List<Food>> getFoodsQuery(@Path("query") String query);

    //Petición que retorna alimentos desde una búsqueda
    @GET("search/noallergy/{query}")
    Call<List<Food>> getAllergyQuery(@Path("query") String query);

    //Petición que retorna aditivos desde una búsqueda
    @GET("search/additives/{query}")
    Call<List<Additive>> getAdditivesQuery(@Path("query") String query);

    //Petición que retorna un aditivo, usado para los resultados de búsqueda
    @GET("additives/{eCode}")
    Call<List<Additive>> getAdditive(@Path("eCode") String eCode);

    //Inserta una solicitud de nuevo alimento
    @POST("foods/new")
    Call<Food> newFoodSolitude(@Body NewFoodBody newFoodBody);

    ////inserta un alimento
    @POST("foods/create")
    Call<Food> newFood(@Body Food newfood);

    //Inserta una solicitud de edición de alimento
    @POST("foods/complaint")
    Call<Food> newFoodComplaint(@Body NewFoodBody newFoodBody);

    //Retorna los alimentos editados por un usuario especifico
    @GET("foods/{userId}/complaint")
    Call<List<ShortFood>> getFoodsComplaint(@Path("userId") String userId);

    //Retorna los alimentos editados aceptados por un usuario especifico
    @GET("foods/{userId}/complaintp")
    Call<List<NewFoodBody>> getFoodsComplaintP(@Path("userId") String userId);

    //Retorna las solicitudes de edicion rechazadas realizadas por un usuario especifico
    @GET("foods/{userId}/complaintr")
    Call<List<NewFoodBody>> getFoodsComplaintR(@Path("userId") String userId);

    //Retorna las imágenes autorizadas de un alimento
    @GET("images/{barcode}")
    Call<ArrayList<FoodImage>> getImages(@Path("barcode") String barcode);

    //Verifica si el correo de inicio de sesión de gmail o facebook existe en la base de datos de usuarios
    @POST("users/external")
    Call<User> findExternalUser(@Body GmailUserBody gmailUserBody);

    //Inserta una solicitud de edición de alimento
    @POST("users/external/register")
    Call<User> registerExternalUser(@Body GmailRegisterBody gmailRegisterBody);

    //Petición que actualiza un alimento en el historial
    //No se usa Patch porque no resulta
    @Headers("Content-Type: application/json")
    @POST("history/{userId}/{barcode}/{like}/")
    Call<ShortFood> modifyHistoryLike(@Path("userId") String userId, @Path("barcode") String barcode, @Path("like") int like);

    //Petición que retorna la cantidad de likes de un alimento
    @GET("history/{barcode}/likes")
    Call<Counter> getLikes(@Path("barcode") String barcode);

    //Petición que retorna la cantidad de dislikes de un alimento
    @GET("history/{barcode}/dislikes")
    Call<Counter> getDislikes(@Path("barcode") String barcode);

    //Petición que inserta un alimento en el historial
    @POST("history/noscan")
    Call<Food> insertNoScan(@Body InsertFromLikeBody insertFromLikeBody);

    //Retorna los expertos
    @GET("experts")
    Call<List<Expert>> getExperts();

    //Retorna los expertos
    @GET("help")
    Call<List<Help>> getHelp();

    //Retorna las tiendas
    @GET("stores")
    Call<List<Store>> getStores();

    //Retorna las tiendas que tienen un alimento particular
    @GET("stores/product/{barcode}")
    Call<List<Store>> getStoresProduct(@Path("barcode") String barcode);

    //Retorna las alimentos de una tienda
    @GET("stores/{idTienda}")
    Call<List<FoodStore>> getFoodsStore(@Path("idTienda") String idTienda);

    //Retorna los alimentos aprobados por un experto
    @GET("experts/{idExperto}/foods")
    Call<List<Food>> getFoodsExpert(@Path("idExperto") String idExperto);

    //Actualiza calificacion de experto
    @Headers("Content-Type: application/json")
    @POST("experts/{idExperto}/{reputacion}")
    Call<Expert> modifyRatingExpert(@Path("idExperto") String idExperto, @Path("reputacion") float reputacion);

    //Petición que inserta una consulta
    @POST("consult")
    Call<Consult> insertConsult(@Body Consult consulta);

    //Crea una valoracion a un experto
    @POST("ratings/new")
    Call<Rating> newRating(@Body Rating rating);

    //Petición que consulta si fue valorado a un experto segun el usuario
    @GET("ratings/{idExperto}/{userId}")
    Call<Rating> isInRating(@Path("idExperto") String idExperto, @Path("userId") String userId);

    //Petición que actualiza una valoracion de un usuario a un experto
    //No se usa Patch porque no resulta
    @Headers("Content-Type: application/json")
    @POST("ratings/{idExperto}/{userId}/{valoracion}")
    Call<Rating> modifyRating(@Path("idExperto") String idExperto, @Path("userId") String userId, @Path("valoracion") float valoracion);

    //Retorna la lista de valoraciones de un experto
    @GET("ratings/{idExperto}")
    Call<List<Rating>> getRatingExpert(@Path("idExperto") String idExperto);

}