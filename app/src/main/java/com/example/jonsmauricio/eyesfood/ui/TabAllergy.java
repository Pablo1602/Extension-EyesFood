package com.example.jonsmauricio.eyesfood.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Allergy;
import com.example.jonsmauricio.eyesfood.data.api.model.AllergyList;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TabAllergy extends Fragment {

    private ListView listaAlergias;
    AdapterTabsAllergy adaptadorAllergy;

    private Allergy alergiasUser;
    private List<AllergyList> alergiaslista = new ArrayList<>();
    private String userIdFinal;
    Retrofit mRestAdapter;
    UserDataApi mUserDataApi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_allergy, container, false);
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
        listaAlergias = (ListView) rootView.findViewById(R.id.lvTabAllergy);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mUserDataApi = mRestAdapter.create(UserDataApi.class);

        listaAlergias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AllergyList currentAllergy = adaptadorAllergy.getItem(i);
                showDialog(Integer.parseInt(userIdFinal), currentAllergy);
            }
        });

        loadAllergy(userIdFinal);
        return rootView;
    }

    public void loadAllergy(String userId){
        Call<Allergy> call = mUserDataApi.getAllergy(userId);
        call.enqueue(new Callback<Allergy>() {
            @Override
            public void onResponse(Call<Allergy> call,
                                   Response<Allergy> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","Error"+response.message());
                    return;
                }
                alergiasUser = response.body();
                makelist(alergiaslista, alergiasUser);
            }
            @Override
            public void onFailure(Call<Allergy> call, Throwable t) {
                Log.d("myTag","Fallo en API "+t.getMessage());
            }
        });
    }

    public void makelist(List<AllergyList> lista, Allergy alergias){
        if(!lista.isEmpty()){
            lista.clear();
        }
        AllergyList leche = new AllergyList("Leche",alergias.getLeche());
        AllergyList gluten = new AllergyList("Gluten",alergias.getGluten());
        lista.add(leche);
        lista.add(gluten);
        showList(lista);
    }

    public void showList(List<AllergyList> listaAlergenos){
        adaptadorAllergy = new AdapterTabsAllergy(getActivity(), listaAlergenos);
        listaAlergias.setAdapter(adaptadorAllergy);
    }

    public void modifyAllergy(int leche, int gluten){
        Call<Allergy> call = mUserDataApi.modifyAllergy(new Allergy(alergiasUser.getId(),Integer.parseInt(userIdFinal), leche, gluten));
        call.enqueue(new Callback<Allergy>() {
            @Override
            public void onResponse(Call<Allergy> call, Response<Allergy> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag","Error"+response.message());
                    return;
                }
                Toast.makeText(getContext(), "Alergia actualizada", Toast.LENGTH_SHORT).show();
                loadAllergy(userIdFinal);
            }

            @Override
            public void onFailure(Call<Allergy> call, Throwable t) {
                Log.d("myTag","Fallo en API "+t.getMessage());
            }
        });
    }

    private void showDialog(final int id, final AllergyList allergy) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //final EditText edittext = new EditText(getActivity());
        //edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Actualizar Alergeno");
        alert.setMessage("Habilitar o deshabilitar el alergeno: "+allergy.getAlergeno());

        alert.setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("myTag","Aprete habilitar con "+allergy.getAlergeno());
                if(allergy.getAlergeno() == "Leche"){
                    modifyAllergy(1, alergiasUser.getGluten());
                }
                else if(allergy.getAlergeno() == "Gluten"){
                    modifyAllergy(alergiasUser.getLeche(), 1);
                }
            }
        });
        alert.setNegativeButton("Deshabilitar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("myTag","Aprete habilitar con "+allergy.getAlergeno());
                if(allergy.getAlergeno() == "Leche"){
                    modifyAllergy(0, alergiasUser.getGluten());
                }
                else if(allergy.getAlergeno() == "Gluten"){
                    modifyAllergy(alergiasUser.getLeche(), 0);
                }
            }
        });

        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }
}
