package com.example.jonsmauricio.eyesfood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Additive;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.Recommendation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdditivesActivity extends AppCompatActivity {

    //Instancias globales para el Card view
    private RecyclerView recycler;
    private AdditivesAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    private Food Alimento;
    private Product product;
    private int MeGusta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additives);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        Bundle args = i.getBundleExtra("BUNDLE");

        if(b != null){
            Alimento = (Food) b.get("Alimento");
            product = (Product) b.get("Product");
            MeGusta = (int) b.get("MeGusta");
            setTitle(product.getProduct_name());
            showAdditives();
        }
    }

    //Muestra los aditivos
    //historial: Lista de aditivos del alimento
    public void showAdditives() {

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.additivesRecycler);
        recycler.setHasFixedSize(true);

        //Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        //Crear un nuevo adaptador
        adapter = new AdditivesAdapter(product.getAdditives_tags(), this);
        recycler.setAdapter(adapter);
    }

    //Carga el menú a la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, FoodsActivity.class);
                i.putExtra("Alimento", Alimento);
                i.putExtra("Product", product);
                i.putExtra("MeGusta", MeGusta);
                startActivity(i);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }


}
