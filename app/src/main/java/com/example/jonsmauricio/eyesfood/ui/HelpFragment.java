package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.jonsmauricio.eyesfood.data.api.model.Comment;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Help;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelpFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    private ListView listView;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;
    private List<Help> listaArticulos;
    ArrayAdapter<Help> adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarHelp);
        toolbar.setTitle(getResources().getString(R.string.nav_help));

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

        retrieveArticles();

        return view;
    }

    private void retrieveArticles() {
        Call<List<Help>> call = mEyesFoodApi.getHelp();
        call.enqueue(new Callback<List<Help>>() {
            @Override
            public void onResponse(Call<List<Help>> call, Response<List<Help>> response) {
                if (!response.isSuccessful()) {

                    return;
                }
                listaArticulos = response.body();
                showListHelp(listaArticulos);
            }

            @Override
            public void onFailure(Call<List<Help>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en get help");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void showListHelp(List<Help> lista){
        // Inicializar el adaptador con la fuente de datos.
        adaptador = new ArrayAdapter<Help>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(adaptador);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = getView().findViewById(R.id.lvHelp);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Help currentHelp = adaptador.getItem(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Ayuda", currentHelp);
                HelpDetailFragment detailFragment = new HelpDetailFragment();
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
            getActivity().getSupportFragmentManager().beginTransaction().
                    remove(getActivity().getSupportFragmentManager().findFragmentById(android.R.id.content)).commit();
        }
        return true;
    }
}