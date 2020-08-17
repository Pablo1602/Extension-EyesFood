package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Diary;
import com.example.jonsmauricio.eyesfood.data.api.model.Entry;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiaryAdapter extends ArrayAdapter<Diary> {

    private String session;
    private Retrofit mRestAdapter;
    private Retrofit mRestAdapter2;
    private EyesFoodApi mEyesFoodApi;
    private String userIdFinal;

    private List<Entry> listaEntradas;
    CommentsApi mCommentsApi;

    public DiaryAdapter(Context context, List<Diary> objects){
        super(context,0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        session = SessionPrefs.get(getContext()).getUserSession();
        userIdFinal = SessionPrefs.get(getContext()).getUserId();

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_diary_item,
                    parent,
                    false);
        }

        // Referencias UI
        final TextView name = convertView.findViewById(R.id.tvDiaryName);
        final Button ver = convertView.findViewById(R.id.btDiary);

        // Diario actual
        final Diary currentDiary = getItem(position);
        name.setText(currentDiary.getTitulo());

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar(currentDiary);
            }
        });
        return convertView;
    }

    public void showCalendar(Diary currentDiary){
        Intent intent = new Intent(getContext(), CalendarActivity.class);
        Bundle args = new Bundle();
        intent.putExtra("BUNDLE",args);
        intent.putExtra("Diario", currentDiary);
        getContext().startActivity(intent);
    }
}
