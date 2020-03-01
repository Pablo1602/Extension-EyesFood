package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Entry;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntryAdapter extends ArrayAdapter<Entry> {

    private String session;
    private Retrofit mRestAdapter;
    private Retrofit mRestAdapter2;
    private EyesFoodApi mEyesFoodApi;
    private String userIdFinal;

    private List<Entry> listaEntradas;
    CommentsApi mCommentsApi;

    public EntryAdapter(Context context, List<Entry> objects){
        super(context,0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCommentsApi = mRestAdapter2.create(CommentsApi.class);
*/
        session = SessionPrefs.get(getContext()).getUserSession();
        userIdFinal = SessionPrefs.get(getContext()).getUserId();

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_entry_item,
                    parent,
                    false);
        }

        // Referencias UI
        final TextView name = convertView.findViewById(R.id.tvEntryName);
        final TextView text = convertView.findViewById(R.id.tvEntryText);

        // Entrada actual
        final Entry currentEntry = getItem(position);
        name.setText(currentEntry.getTitulo());
        text.setText(currentEntry.getTexto());

        return convertView;
    }
}