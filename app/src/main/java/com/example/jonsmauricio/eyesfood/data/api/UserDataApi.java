package com.example.jonsmauricio.eyesfood.data.api;

import com.example.jonsmauricio.eyesfood.data.api.model.Allergy;
import com.example.jonsmauricio.eyesfood.data.api.model.EditMeasureBody;
import com.example.jonsmauricio.eyesfood.data.api.model.InsertMeasureBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserDataApi {

    String BASE_URL = "https://eyesfoodapi.herokuapp.com/api.eyesfood.userdata.cl/v1/";

    //Retorna las medidas
    @GET("measures/weight/{userId}")
    Call<List<Measure>> getWeight(@Path("userId") String userId);

    @GET("measures/fat/{userId}")
    Call<List<Measure>> getFat(@Path("userId") String userId);

    @GET("measures/waist/{userId}")
    Call<List<Measure>> getWaist(@Path("userId") String userId);

    @GET("measures/a1c/{userId}")
    Call<List<Measure>> getA1c(@Path("userId") String userId);

    @GET("measures/preglucose/{userId}")
    Call<List<Measure>> getPreglucose(@Path("userId") String userId);

    @GET("measures/postglucose/{userId}")
    Call<List<Measure>> getPostglucose(@Path("userId") String userId);

    @GET("measures/pressure/{userId}")
    Call<List<Measure>> getPressure(@Path("userId") String userId);

    //Edita las medidas
    @POST("measures/height/edit")
    Call<Measure> editHeight(@Body EditMeasureBody editMeasureBody);

    @POST("measures/weight/edit")
    Call<Measure> editWeight(@Body EditMeasureBody editMeasureBody);

    @POST("measures/fat/edit")
    Call<Measure> editFat(@Body EditMeasureBody editMeasureBody);

    @POST("measures/waist/edit")
    Call<Measure> editWaist(@Body EditMeasureBody editMeasureBody);

    @POST("measures/a1c/edit")
    Call<Measure> editA1c(@Body EditMeasureBody editMeasureBody);

    @POST("measures/preglucose/edit")
    Call<Measure> editPreGlu(@Body EditMeasureBody editMeasureBody);

    @POST("measures/postglucose/edit")
    Call<Measure> editPostGlu(@Body EditMeasureBody editMeasureBody);

    @POST("measures/pressure/edit")
    Call<Measure> editPressure(@Body EditMeasureBody editMeasureBody);

    //Eliminar datos, se hace con POST porque el delete no funciona Stream was reset:internal error
    @POST("measures/weight/delete/{measureId}")
    Call<Measure> deleteWeight(@Path("measureId") int measureId);

    @POST("measures/fat/delete/{measureId}")
    Call<Measure> deleteFat(@Path("measureId") int measureId);

    @POST("measures/waist/delete/{measureId}")
    Call<Measure> deleteWaist(@Path("measureId") int measureId);

    @POST("measures/a1c/delete/{measureId}")
    Call<Measure> deleteA1c(@Path("measureId") int measureId);

    @POST("measures/preglucose/delete/{measureId}")
    Call<Measure> deletePreGlu(@Path("measureId") int measureId);

    @POST("measures/postglucose/delete/{measureId}")
    Call<Measure> deletePostGlu(@Path("measureId") int measureId);

    @POST("measures/pressure/delete/{measureId}")
    Call<Measure> deletePressure(@Path("measureId") int measureId);

    //Insertar datos
    @POST("measures/weight")
    Call<Measure> insertWeight(@Body InsertMeasureBody insertMeasureBody);

    @POST("measures/fat")
    Call<Measure> insertFat(@Body InsertMeasureBody insertMeasureBody);

    @POST("measures/waist")
    Call<Measure> insertWaist(@Body InsertMeasureBody insertMeasureBody);

    @POST("measures/a1c")
    Call<Measure> insertA1c(@Body InsertMeasureBody insertMeasureBody);

    @POST("measures/preglucose")
    Call<Measure> insertPreGlu(@Body InsertMeasureBody insertMeasureBody);

    @POST("measures/postglucose")
    Call<Measure> insertPostGlu(@Body InsertMeasureBody insertMeasureBody);

    @POST("measures/pressure")
    Call<Measure> insertPressure(@Body InsertMeasureBody insertMeasureBody);

    //Devolver alergias
    @GET("allergy/{userId}")
    Call<Allergy> getAllergy(@Path("userId") String userId);

    //Actualizar alergias
    @POST("allergy")
    Call<Allergy> modifyAllergy(@Body Allergy allergy);
}
