package com.example.jonsmauricio.eyesfood.ui;

import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.EditMeasureBody;
import com.example.jonsmauricio.eyesfood.data.api.model.InsertMeasureBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabSpecific extends Fragment{

    private ListView listaEspecificos;
    AdapterTabsSpecific adapterTabsSpecific;

    private List<Measure> listaA1c;
    private List<Measure> listaGluPre;
    private List<Measure> listaGluPost;
    private List<Measure> listaPresion;
    private List<Measure> listaUltimos = new ArrayList<>();

    private String userIdFinal;
    Retrofit mRestAdapter;
    UserDataApi mUserDataApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_specific, container, false);
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
        listaEspecificos = (ListView) rootView.findViewById(R.id.lvTabSpecific);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mUserDataApi = mRestAdapter.create(UserDataApi.class);

        loadA1c(userIdFinal);
        listaEspecificos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Measure currentMeasure = adapterTabsSpecific.getItem(i);
                String title = "";
                String message = "";
                String selection = "";
                int measureId = currentMeasure.getId();
                //Existe el dato así que lo edito
                if(currentMeasure.getId() > 0){
                    //A1C
                    if(i == 0){
                        title = "Editar A1C";
                        message = "Ingresa tu hemoglobina glicolisada";
                        selection = "a1c";
                    }
                    //Glucosa preprandial
                    else if(i==1){
                        title = "Editar glucosa pre-prandial";
                        message = "Ingresa tu glucosa pre-prandial";
                        selection = "preglucose";
                    }
                    //Glucosa postprandial
                    else if(i==2){
                        title = "Editar glucosa post-prandial";
                        message = "Ingresa tu glucosa post-prandial";
                        selection = "postglucose";
                    }
                    //Presión arterial
                    else if(i==3){
                        title = "Editar presión arterial";
                        message = "Ingresa tu presión arterial";
                        selection = "pressure";
                    }
                    showDialog(0,title,message, selection, measureId);
                }
                //No existe el dato así que lo inserto
                else{
                    //A1C
                    if(i == 0){
                        title = "Ingresar A1C";
                        message = "Ingresa tu hemoglobina glicolisada";
                        selection = "a1c";
                    }
                    //Glucosa preprandial
                    else if(i==1){
                        title = "Ingresar glucosa pre-prandial";
                        message = "Ingresa tu glucosa pre-prandial";
                        selection = "preglucose";
                    }
                    //Glucosa postprandial
                    else if(i==2){
                        title = "Ingresar glucosa post-prandial";
                        message = "Ingresa tu glucosa psot-prandial";
                        selection = "postglucose";
                    }
                    //Presión arterial
                    else if(i==3){
                        title = "Ingresar presión arterial";
                        message = "Ingresa tu presión arterial";
                        selection = "presion";
                    }
                    showDialog(1,title,message,selection,measureId);
                }
            }
        });
        return rootView;
    }

    private void showDialog(int selection, String title, String message, final String measureSelection, final int measureId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        final EditText edittext = new EditText(getActivity());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle(title);
        alert.setMessage(message);

        alert.setView(edittext);

        //Editar medidas
        if(selection == 0){
            alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String medida = edittext.getText().toString();
                    if(!medida.equals(null) && !medida.equals("") && !medida.equals("0")){
                        editMeasure(measureId, medida, measureSelection);
                    }
                }
            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
        }
        else if(selection == 1){
            alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String medida = edittext.getText().toString();
                    if(!medida.equals(null) && !medida.equals("") && !medida.equals("0")){
                        insertMeasure(medida, measureSelection);
                    }
                }
            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
        }
        alert.show();
    }

    public void loadA1c(String userId){
        Call<List<Measure>> call = mUserDataApi.getA1c(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    return;
                }
                listaA1c = response.body();
                loadGluPre(userIdFinal);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void loadGluPre(String userId) {
        Call<List<Measure>> call = mUserDataApi.getPreglucose(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    return;
                }
                listaGluPre = response.body();
                loadGluPost(userIdFinal);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void loadGluPost(String userId) {
        Call<List<Measure>> call = mUserDataApi.getPostglucose(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    return;
                }
                listaGluPost = response.body();
                loadPressure(userIdFinal);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void loadPressure(String userId) {
        Call<List<Measure>> call = mUserDataApi.getPressure(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    return;
                }
                listaPresion = response.body();
                makeList(listaUltimos, listaA1c, listaGluPre, listaGluPost, listaPresion);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void makeList(List<Measure> lista, List<Measure> listaA1c, List<Measure> listaGluPre, List<Measure> listaGluPost, List<Measure> listaPresion) {
        Measure a1c, preglu, postglu, presion, vacio;
        vacio = new Measure(0,0,0,"");

        //A1C
        if(listaA1c.isEmpty()){
            a1c = vacio;
        }
        else{
            a1c = listaA1c.get(0);
        }
        //Glucosa pre prandial
        if(listaGluPre.isEmpty()){
            preglu = vacio;
        }
        else{
            preglu = listaGluPre.get(0);
        }
        //Glucosa post prandial
        if(listaGluPost.isEmpty()){
            postglu = vacio;
        }
        else{
            postglu = listaGluPost.get(0);
        }
        //Glucosa post prandial
        if(listaPresion.isEmpty()){
            presion = vacio;
        }
        else{
            presion = listaPresion.get(0);
        }
        //Vacío la lista antes de llenarla
        if(!lista.isEmpty()){
            lista.clear();
        }
        lista.add(a1c);
        lista.add(preglu);
        lista.add(postglu);
        lista.add(presion);

        showList(lista);
    }

    private void showList(List<Measure> lista) {
        adapterTabsSpecific = new AdapterTabsSpecific(getActivity(), lista);

        listaEspecificos.setAdapter(adapterTabsSpecific);
    }

    private void editMeasure(int measureId, String measure, String selection) {
        EditMeasureBody body = new EditMeasureBody(measureId, userIdFinal, measure);
        Call<Measure> call = null;
        switch(selection){
            case "a1c":
                call = mUserDataApi.editA1c(body);
                break;
            case "preglucose":
                call = mUserDataApi.editPreGlu(body);
                break;
            case "postglucose":
                call = mUserDataApi.editPostGlu(body);
                break;
            case "pressure":
                call = mUserDataApi.editPressure(body);
                break;
        }
        call.enqueue(new Callback<Measure>() {
            @Override
            public void onResponse(Call<Measure> call,
                                   Response<Measure> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                //Al ser editado se tienen que cargar de nuevo los datos
                loadA1c(userIdFinal);
            }

            @Override
            public void onFailure(Call<Measure> call, Throwable t) {

            }
        });
    }

    private void insertMeasure(String measure, String selection) {
        InsertMeasureBody body = new InsertMeasureBody(userIdFinal, measure);
        Call<Measure> call = null;
        switch(selection){
            case "a1c":
                call = mUserDataApi.insertA1c(body);
                break;
            case "preglucose":
                call = mUserDataApi.insertPreGlu(body);
                break;
            case "postglucose":
                call = mUserDataApi.insertPostGlu(body);
                break;
            case "pressure":
                call = mUserDataApi.insertPressure(body);
                break;
        }
        call.enqueue(new Callback<Measure>() {
            @Override
            public void onResponse(Call<Measure> call,
                                   Response<Measure> response) {
                if (!response.isSuccessful()) {
                    Log.d("insert","no succesfull "+response.errorBody());
                    return;
                }
                //Al insertar se tienen que cargar de nuevo los datos
                Log.d("insert","succesfull");
                loadA1c(userIdFinal);
            }

            @Override
            public void onFailure(Call<Measure> call, Throwable t) {
                Log.d("insert","failure "+t.getMessage());
            }
        });
    }
}
