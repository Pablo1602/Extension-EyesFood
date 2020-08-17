package com.example.jonsmauricio.eyesfood.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.EditMeasureBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabPostGlu extends Fragment{

    private ListView listaUI;
    private List<Measure> listaPostGlu;
    AdapterTabsEdit adaptadorEdit;

    private String userIdFinal;
    Retrofit mRestAdapter;
    UserDataApi mUserDataApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_post_glu, container, false);
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
        listaUI = (ListView) rootView.findViewById(R.id.lvTabPostGlu);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mUserDataApi = mRestAdapter.create(UserDataApi.class);

        loadPostGlu(userIdFinal);

        listaUI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Measure currentMeasure = adaptadorEdit.getItem(i);
                showDialog(currentMeasure.getId());
            }
        });
        return rootView;
    }

    private void showDialog(final int id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        final EditText edittext = new EditText(getActivity());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Editar o eliminar");
        alert.setMessage("Ingresa el valor a editar o selecciona eliminar");

        alert.setView(edittext);

        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String medida = edittext.getText().toString();
                if(!medida.equals(null) && !medida.equals("") && !medida.equals("0")){
                    editMeasure(medida, id);
                }
            }
        });

        alert.setNeutralButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new AlertDialog.Builder(getContext())
                        .setIcon(null)
                        .setTitle("Eliminar")
                        .setMessage("¿Está seguro que desea eliminar el dato?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMeasure(id);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void deleteMeasure(int id) {
        Call<Measure> call = mUserDataApi.deletePostGlu(id);
        call.enqueue(new Callback<Measure>() {
            @Override
            public void onResponse(Call<Measure> call,
                                   Response<Measure> response) {
                if (!response.isSuccessful()) {
                    Log.d("delete","no lo hace " + response.errorBody());
                    return;
                }
                //Al ser editado se tienen que cargar de nuevo los datos
                Log.d("delete","Lo hace");
                loadPostGlu(userIdFinal);
            }

            @Override
            public void onFailure(Call<Measure> call, Throwable t) {
                Log.d("delete",t.getMessage());
            }
        });
    }

    private void editMeasure(String medida, int id) {
        EditMeasureBody body = new EditMeasureBody(id, userIdFinal, medida);
        Call<Measure> call = mUserDataApi.editPostGlu(body);
        call.enqueue(new Callback<Measure>() {
            @Override
            public void onResponse(Call<Measure> call,
                                   Response<Measure> response) {
                if (!response.isSuccessful()) {
                    Log.d("edit","no succesful "+response.errorBody());
                    return;
                }
                //Al ser editado se tienen que cargar de nuevo los datos
                Log.d("edit","succesful ");
                loadPostGlu(userIdFinal);
            }

            @Override
            public void onFailure(Call<Measure> call, Throwable t) {
                Log.d("edit","failure "+t.getMessage());
            }
        });
    }

    public void loadPostGlu(String userId){
        Call<List<Measure>> call = mUserDataApi.getPostglucose(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaPostGlu = response.body();
                showList(listaPostGlu);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void showList(List<Measure> medidasGenerales) {
        adaptadorEdit = new AdapterTabsEdit(getActivity(), medidasGenerales);
        listaUI.setAdapter(adaptadorEdit);
    }
}
