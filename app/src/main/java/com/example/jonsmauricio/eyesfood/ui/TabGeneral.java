package com.example.jonsmauricio.eyesfood.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class TabGeneral extends Fragment {

    private ListView listaGeneral;
    AdapterTabsGeneral adaptadorGeneral;

    private List<Measure> listaPeso;
    private List<Measure> listaGrasa;
    private List<Measure> listaCintura;
    private List<Measure> listaUltimos = new ArrayList<>();

    private String userIdFinal;
    Retrofit mRestAdapter;
    UserDataApi mUserDataApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_general, container, false);
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
        listaGeneral = (ListView) rootView.findViewById(R.id.lvTabGeneral);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    
        // Crear conexión a la API de EyesFood
        mUserDataApi = mRestAdapter.create(UserDataApi.class);

        listaGeneral.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Measure currentMeasure = adaptadorGeneral.getItem(i);
                String title = "";
                String message = "";
                String selection = "";
                int measureId = currentMeasure.getId();
                //Estatura así que edito
                if(i == 0){
                    title = "Ingresar estatura";
                    message = "Ingresa tu estatura";
                    selection = "height";
                    showDialog(0, title, message, selection,0);
                }
                //Cualquiera menos IMC
                else if(i != 2){
                    //Existe el dato así que lo edito
                    if(currentMeasure.getId() > 0){
                        //Peso
                        if(i == 1){
                            title = "Editar peso";
                            message = "Ingresa tu peso";
                            selection = "weight";
                        }
                        //Grasa
                        else if(i==3){
                            title = "Editar % de grasa corporal";
                            message = "Ingresa tu % de grasa corporal";
                            selection = "fat";
                        }
                        //Cintura
                        else if(i==4){
                            title = "Editar medida de cintura";
                            message = "Ingresa tu medida de cintura";
                            selection = "waist";
                        }
                        showDialog(1,title,message, selection, measureId);
                    }
                    //No existe el dato así que lo inserto
                    else{
                        //Peso
                        if(i == 1){
                            title = "Ingresar peso";
                            message = "Ingresa tu peso";
                            selection = "weight";
                        }
                        else if(i==3){
                            title = "Ingresar % de grasa corporal";
                            message = "Ingresa tu % de grasa corporal";
                            selection = "fat";
                        }
                        else if(i==4){
                            title = "Ingresar medida de cintura";
                            message = "Ingresa tu medida de cintura";
                            selection = "waist";
                        }
                        showDialog(2,title,message,selection,measureId);
                    }
                }
            }
        });

        loadWeight(userIdFinal);
        return rootView;
    }

    private void makeList(List<Measure> lista, List<Measure> listaPeso, List<Measure> listaGrasa, List<Measure> listaCintura){
        Measure peso, grasa, cintura, vacio;
        vacio = new Measure(0,0,0,"");
        //Peso
        if(listaPeso.isEmpty()){
            peso = vacio;
        }
        else{
            peso = listaPeso.get(0);
        }
        //Grasa
        if(listaGrasa.isEmpty()){
            Log.d("myTag","vacio grasa");
            grasa = vacio;
        }
        else{
            grasa = listaGrasa.get(0);
        }
        //Cintura
        if(listaCintura.isEmpty()){
            Log.d("myTag","vacio cintura");
            cintura = vacio;
        }
        else{
            cintura = listaCintura.get(0);
        }

        //Vacío la lista antes de llenarla
        if(!lista.isEmpty()){
            lista.clear();
        }
        lista.add(vacio);
        lista.add(peso);
        lista.add(vacio);
        lista.add(grasa);
        lista.add(cintura);

        showList(lista);
    }

    private void showList(List<Measure> medidasGenerales) {
        adaptadorGeneral = new AdapterTabsGeneral(getActivity(), medidasGenerales);
        listaGeneral.setAdapter(adaptadorGeneral);
    }

    public void loadWeight(String userId){
        Call<List<Measure>> call = mUserDataApi.getWeight(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaPeso = response.body();
                loadFat(userIdFinal);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadFat(String userId){
        Call<List<Measure>> call = mUserDataApi.getFat(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                listaGrasa = response.body();
                loadWaist(userIdFinal);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadWaist(String userId){
        Call<List<Measure>> call = mUserDataApi.getWaist(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                listaCintura = response.body();
                makeList(listaUltimos, listaPeso, listaGrasa, listaCintura);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void showDialog(int selection, String title, String message, final String measureSelection, final int measureId){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        final EditText edittext = new EditText(getActivity());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle(title);
        alert.setMessage(message);

        alert.setView(edittext);

        //Editar estatura
        if(selection == 0){
            alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String estatura = edittext.getText().toString();
                    if(!estatura.equals(null) && !estatura.equals("") && !estatura.equals("0")){
                        SessionPrefs.get(getContext()).setUserHeight(estatura);
                        editMeasure(0, estatura, measureSelection);
                        showList(listaUltimos);
                    }
                }
            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
        }
        //Editar medidas
        else if(selection == 1){
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
        else if(selection == 2){
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

    private void editMeasure(int measureId, String measure, String selection) {
        EditMeasureBody body = new EditMeasureBody(measureId, userIdFinal, measure);
        Call<Measure> call = null;
        switch(selection){
            case "height":
                 call = mUserDataApi.editHeight(body);
                break;
            case "weight":
                call = mUserDataApi.editWeight(body);
                break;
            case "fat":
                call = mUserDataApi.editFat(body);
                break;
            case "waist":
                call = mUserDataApi.editWaist(body);
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
                loadWeight(userIdFinal);
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
            case "weight":
                call = mUserDataApi.insertWeight(body);
                break;
            case "fat":
                call = mUserDataApi.insertFat(body);
                break;
            case "waist":
                call = mUserDataApi.insertWaist(body);
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
                loadWeight(userIdFinal);
            }

            @Override
            public void onFailure(Call<Measure> call, Throwable t) {
                Log.d("insert","failure "+t.getMessage());
            }
        });
    }
}