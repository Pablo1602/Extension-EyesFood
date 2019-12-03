package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Counter;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.squareup.picasso.Picasso;

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

public class ExpertsAdapter extends ArrayAdapter<Expert> {

    final String baseFotoExperto = EyesFoodApi.BASE_URL+"img/experts/";
    Retrofit mRestAdapter;
    //Retrofit mRestAdapter2;
    //EyesFoodApi mEyesFoodApi;
    CommentsApi mCommentsApi;
    private Counter commentsCounter;
    private int comments;

    public ExpertsAdapter(Context context, List<Expert> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_experts_item,
                    parent,
                    false);
        }

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mCommentsApi = mRestAdapter.create(CommentsApi.class);

        // Referencias UI.
        ImageView avatar = convertView.findViewById(R.id.ivExpertsAvatar);
        TextView name = convertView.findViewById(R.id.tvStoresName);
        TextView specialty = convertView.findViewById(R.id.tvStoresPage);
        TextView phone = convertView.findViewById(R.id.tvExpertsPhone);
        RatingBar ratingBar = convertView.findViewById(R.id.rbExpertsRating);
        TextView commentsCount = convertView.findViewById(R.id.tvCommentsCount);
        //TextView quality = convertView.findViewById(R.id.tvExpertQualify);

        // Experto actual.
        Expert currentExpert = getItem(position);

        // Setup.
        //Cargo avatar de usuario actual
        Picasso.with(getContext()).load(baseFotoExperto + currentExpert.getPhoto()).resize(800,800).into(avatar);

        name.setText(currentExpert.getName() + " " + currentExpert.getLastName());
        ratingBar.setRating(currentExpert.getReputation());
        //quality.setText(Float.toString(currentExpert.getReputation()));
        specialty.setText(currentExpert.getSpecialty());
        phone.setText("Alimentos Aprobados: "+currentExpert.getFoods());
        //REVISAR POR QUE NO SIEMPRE CARGA BIEN LA CANTIDAD DE COMENTARIOS
        getCommentsCount(commentsCount, String.valueOf(currentExpert.getExpertId()));
        //commentsCount.setText(String.valueOf(comments));

        return convertView;
    }

    public void getCommentsCount(final TextView commentsCount, final String referencia){
        Call<Counter> call = mCommentsApi.getCommentsCount(referencia);
        call.enqueue(new Callback<Counter>() {
            @Override
            public void onResponse(Call<Counter> call, Response<Counter> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en getComments " + response.errorBody().toString());
                    return;
                }
                else {
                    commentsCounter = response.body();
                    comments = commentsCounter.getCount();
                    Log.d("myTag", "idExperto: "+referencia+" comments: "+comments);
                    commentsCount.setText(String.valueOf(comments));
                }
            }
            @Override
            public void onFailure(Call<Counter> call, Throwable t) {
                Log.d("myTag", "Fallo en getComments "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }
}