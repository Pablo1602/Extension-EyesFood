package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Comment;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.User;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JonásMauricio on 04-11-2017.
 */

public class CommentsAdapter extends ArrayAdapter<Comment>{

    final String baseFotoUsuario = EyesFoodApi.BASE_URL+"img/users/";
    private String session;
    private Retrofit mRestAdapter;
    private Retrofit mRestAdapter2;
    private EyesFoodApi mEyesFoodApi;
    private String userIdFinal;

    private List<Comment> listaRespuestas;
    CommentsApi mCommentsApi;
    //private Comment Comentario;


    public CommentsAdapter(CommentsActivity context, List<Comment> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Crear conexión al servicio REST EyesFood
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        session = SessionPrefs.get(getContext()).getUserSession();
        userIdFinal = SessionPrefs.get(getContext()).getUserId();


        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_comments_item,
                    parent,
                    false);
        }

        // Referencias UI.
        final ImageView avatar = convertView.findViewById(R.id.ivCommentsAvatar);
        final TextView name = convertView.findViewById(R.id.tvCommentsName);
        final TextView comment = convertView.findViewById(R.id.tvCommentsComment);
        final TextView date = convertView.findViewById(R.id.tvCommentsDate);
        final RatingBar ratingBar = convertView.findViewById(R.id.rbCommentsRating);
        final Button response = convertView.findViewById(R.id.btResponse);

        // Comentario actual.
        final Comment currentComment = getItem(position);

        // Setup.
        //Cargo avatar de usuario actual
        // TODO: 07-11-2017 VER COMO ARREGLAR ESTO
        /*if(currentComment.getUserPhoto().contains("facebook") || currentComment.getUserPhoto().contains("google")) {
            Picasso.with(getContext()).load(currentComment.getUserPhoto()).resize(800, 800).into(avatar);
        }*/
        Call<User> call = mEyesFoodApi.getUser(currentComment.getColaborador());
        call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             if (!response.isSuccessful()) {
                                 // TODO: Procesar error de API
                                 Log.d("myTag", "hola"+response.errorBody().toString());
                                 return;
                             }
                             User user = response.body();
                             Picasso.with(getContext()).load(baseFotoUsuario + "default.png").resize(800, 800).into(avatar);
                             name.setText(user.getName() + " " + user.getSurName());
                             ratingBar.setRating(Float.parseFloat(user.getReputation()));
                             comment.setText(currentComment.getComment());
                             date.setText(currentComment.getDate());

                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {

                         }
                     });


        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResponses(currentComment);
            }
        });

        return convertView;
    }


    //Carga los comentarios del alimento
    public void loadResponses(final Comment currentComment) {
        Call<List<Comment>> call = mCommentsApi.getResponses(Integer.parseInt(currentComment.getId()));
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaRespuestas = response.body();
                showResponses(listaRespuestas, currentComment);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }

    public void showResponses(List<Comment> lista, Comment currentComment){
        Intent intent = new Intent(getContext(), ResponseActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("Respuestas",(Serializable) lista);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("Comentario", currentComment);
        getContext().startActivity(intent);
    }

}