package com.example.jonsmauricio.eyesfood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.Recommendation;

import java.util.ArrayList;

public class RecommendationsActivity extends AppCompatActivity{

    //Instancias globales para el Card view
    private RecyclerView recycler;
    private RecommendationsAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    private Food Alimento;
    private int MeGusta;
    private ArrayList<Recommendation> recommendations;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        Bundle args = i.getBundleExtra("BUNDLE");
        recommendations = (ArrayList<Recommendation>) args.getSerializable("Recomendaciones");

        if(b != null){
            Alimento = (Food) b.get("Alimento");
            product = (Product) b.get("Product");
            MeGusta = (int) b.get("MeGusta");
            setTitle(product.getProduct_name());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        showRecommendations(recommendations);
    }

    //Muestra las recomendaciones
    //recommendations: Lista de recomendaciones del alimento
    public void showRecommendations(ArrayList<Recommendation> recommendations) {

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recommendationsRecycler);
        recycler.setHasFixedSize(true);

        //Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        //Crear un nuevo adaptador
        adapter = new RecommendationsAdapter(recommendations);
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
                i.putExtra("Product",product);
                i.putExtra("MeGusta", MeGusta);
                startActivity(i);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

}