package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.support.v7.widget.Toolbar;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodStore;
import com.example.jonsmauricio.eyesfood.data.api.model.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoresFragment extends DialogFragment {

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    private ListView listView;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;
    private List<Store> listaTiendas;
    private ArrayAdapter<Store> adaptadorTiendas;
    private List<FoodStore> foodArrayList;
    private ProgressDialog progressDialog;
    int menu;
    String barcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_stores, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarStores);
        toolbar.setTitle(getResources().getString(R.string.nav_stores));
        progressDialog= new ProgressDialog(getActivity());

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //Si menu es 1, viene desde el navbar. Se accede a todos los stores
            menu = bundle.getInt("Menu");
            Log.d("myTag2","Menu = "+menu);
            if (menu==1){
                retrieveStores();
            //Si es 2, se accede solo a los stores que tengan el barcode
            }else{
                barcode = bundle.getString("barcode");
                retrieveStores(barcode);
            }
        }

        return view;
    }

    private void retrieveStores(String barcode) {
        Call<List<Store>> call = mEyesFoodApi.getStoresProduct(barcode);
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaTiendas = response.body();
                showListStores(listaTiendas);
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    private void retrieveStores() {
        Call<List<Store>> call = mEyesFoodApi.getStores();
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaTiendas = response.body();
                showListStores(listaTiendas);
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void showListStores(List<Store> lista){
        // Inicializar el adaptador con la fuente de datos.
        adaptadorTiendas = new StoresAdapter(getContext(), lista);
        //Relacionando la lista con el adaptador
        listView.setAdapter(adaptadorTiendas);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = getView().findViewById(R.id.lvStores);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Store currentStore = adaptadorTiendas.getItem(i);
                getListFoods(currentStore);
            }
        });
    }

    public void getListFoods(final Store currentStore){
        Log.d("myTag","En getListsFoods");
        Call<List<FoodStore>> call = mEyesFoodApi.getFoodsStore(currentStore.getId());
        call.enqueue(new Callback<List<FoodStore>>() {
            @Override
            public void onResponse(Call<List<FoodStore>> call,
                                   Response<List<FoodStore>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "hola"+response.errorBody().toString());
                    return;
                }
                foodArrayList = response.body();
                Log.d("myTag","Cantidad Alimentos: "+foodArrayList.size());
                showListFoods(foodArrayList, currentStore);
            }

            @Override
            public void onFailure(Call<List<FoodStore>> call, Throwable t) {
                Log.d("Falla", "Falla en la llamada a historial: loadHistoryFoods"+t.getMessage());
            }
        });
    }

    private void showListFoods(List<FoodStore> foodArrayList, Store currentStore) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Tienda", currentStore);
        bundle.putInt("Alimentos", foodArrayList.size());
        bundle.putInt("Menu", menu);
        if (menu==2){
            bundle.putString("barcode", barcode);
        }
        StoresDetailFragment detailFragment = new StoresDetailFragment();
        detailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, detailFragment)
                .addToBackStack(null)
                .commit();
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
        if (menu.findItem(R.id.action_settings)!=null){
            menu.findItem(R.id.action_settings).setVisible(false);
            menu.findItem(R.id.searchHistory).setVisible(false);
        }
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