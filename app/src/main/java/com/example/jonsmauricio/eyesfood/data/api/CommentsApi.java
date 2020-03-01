package com.example.jonsmauricio.eyesfood.data.api;

import com.example.jonsmauricio.eyesfood.data.api.model.Comment;
import com.example.jonsmauricio.eyesfood.data.api.model.CommentBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Counter;
import com.example.jonsmauricio.eyesfood.data.api.model.Diary;
import com.example.jonsmauricio.eyesfood.data.api.model.Entry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentsApi {

    String BASE_URL = "https://eyesfoodapi.herokuapp.com/api.eyesfood.comments.cl/v1/";

    //Peticion que retorna los comentarios de un contexto (1 = alimentos, 2 = expertos)
    @GET("comments/{idContexto}/{referencia}")
    Call<List<Comment>> getComments(@Path("idContexto") int idContexto, @Path("referencia") String referencia);

    //Peticion que retorna las respuestas de un comentario
    @GET("comments/respuesta/{idComentario}")
    Call<List<Comment>> getResponses(@Path("idComentario") int idComentario);

    //Peticion de creacion de un comentario
    @POST("comments/{idContexto}/{referencia}")
    Call<Comment> newComment(@Body CommentBody commentBody, @Path("idContexto") int idContexto, @Path("referencia") String referencia);

    //Peticion de creacion de una respuesta
    @POST("comments/respuesta/{idComentario}")
    Call<Comment> newResponse(@Body CommentBody commentBody, @Path("idComentario") int idComentario);

    //Peticion para borrar un comentario
    @POST("comments/borrar/{idComentario}")
    Call<Comment> deleteComment(@Path("idComentario") int idComentario);

    //Peticion para borrar una respuesta
    @POST("comments/borrar/respuesta/{idRespuesta}")
    Call<Comment> deleteResponse(@Path("idRespuesta") int idRespuesta);

    //Peticion que cuenta los alimentos en el historial
    @GET("comments/countHistory/{codigo}")
    Call<Counter> getCommentsCount(@Path("codigo") String codigo);

    //Actualiza comentario
    @Headers("Content-Type: application/json")
    @POST("comments/editar/{idComentario}")
    Call<Comment> modifyComment(@Body CommentBody commentBody, @Path("idComentario") int idComentario);

    //Actualiza respuesta
    @Headers("Content-Type: application/json")
    @POST("comments/editar/respuesta/{idRespuesta}")
    Call<Comment> modifyResponse(@Body CommentBody commentBody, @Path("idRespuesta") int idRespuesta);

    //Peticion que retorna los diarios de una persona
    @GET("diary/{idUsuario}")
    Call<List<Diary>> getDiary(@Path("idUsuario") String idUsuario);

    //Peticion que retorna las entradas de un diario
    @GET("diary/entrada/{idDiario}")
    Call<List<Entry>> getEntry(@Path("idDiario") String idDiario);

    //Peticion que retorna las entradas de un diario en un día
    @GET("diary/entrada/{idDiario}/{fecha}")
    Call<List<Entry>> getEntryDate(@Path("idDiario") String idDiario, @Path("fecha") String fecha);

    //Peticion de creacion de un diario
    @POST("diary/{idUsuario}")
    Call<Diary> newDiary(@Body Diary diary, @Path("idUsuario") int idUsuario);

    //Peticion de creacion de una entrada
    @POST("diary/entrada/{idDiario}")
    Call<Entry> newEntry(@Body Entry entry, @Path("idDiario") int idDiario);

    //Peticion para borrar un diario
    @POST("diary/borrar/{idDiario}")
    Call<Diary> deleteDiary(@Path("idDiario") int idDiario);

    //Peticion para borrar una entrada
    @POST("diary/borrar/entrada/{idEntrada}")
    Call<Entry> deleteEntry(@Path("idEntrada") int idEntrada);

    //Actualiza un diario
    @Headers("Content-Type: application/json")
    @POST("diary/editar/{idDiario}")
    Call<Diary> modifyDiary(@Body Diary diary, @Path("idDiario") int idDiario);

    //Actualiza una entrada
    @Headers("Content-Type: application/json")
    @POST("diary/editar/entrada/{idEntrada}")
    Call<Entry> modifyEntry(@Body Entry entry, @Path("idEntrada") int idEntrada);
}
