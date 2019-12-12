package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpertsFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    private ListView listView;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;
    private List<Expert> listaExpertos;
    private ArrayAdapter<Expert> adaptadorExpertos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_experts, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarExperts);
        toolbar.setTitle(getResources().getString(R.string.nav_experts));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close_black_24dp);
        }

        setHasOptionsMenu(true);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        retrieveExperts();

        return view;
    }

    private void retrieveExperts() {
        Call<List<Expert>> call = mEyesFoodApi.getExperts();
        call.enqueue(new Callback<List<Expert>>() {
            @Override
            public void onResponse(Call<List<Expert>> call, Response<List<Expert>> response) {
                if (!response.isSuccessful()) {

                    return;
                }
                listaExpertos = response.body();
                showListExperts(listaExpertos);
            }

            @Override
            public void onFailure(Call<List<Expert>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void showListExperts(final List<Expert> lista){
        final ArrayList<Call<List<Food>>> productResponseCalls = new ArrayList<>();
        for (Expert expert : lista) {
            productResponseCalls.add(mEyesFoodApi.getFoodsExpert(String.valueOf(expert.getExpertId())));
        }
        for (final Call<List<Food>> call2 : productResponseCalls){
            call2.enqueue(new Callback<List<Food>>() {
                @Override
                public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                    if (response.isSuccessful()){
                        List<Food> foods = response.body();
                        int index = productResponseCalls.indexOf(call2);
                        lista.get(index).setFoods(foods.size());
                        if (index==productResponseCalls.size()-1){
                            showListExpertsFinal(lista);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Food>> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showListExpertsFinal(List<Expert> lista) {
        // Inicializar el adaptador con la fuente de datos.
        adaptadorExpertos = new ExpertsAdapter(getContext(), lista);
        //Relacionando la lista con el adaptador
        listView.setAdapter(adaptadorExpertos);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = getView().findViewById(R.id.lvExperts);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Expert currentExpert = adaptadorExpertos.getItem(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Experto", currentExpert);
                ExpertDetailFragment detailFragment = new ExpertDetailFragment();
                detailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.searchHistory).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            dismiss();
        }
        return true;
    }
}