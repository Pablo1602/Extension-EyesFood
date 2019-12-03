package com.example.jonsmauricio.eyesfood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodImage;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.Recommendation;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImagesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    Retrofit mRestAdapter;
    private EyesFoodApi mEyesFoodApi;

    private ArrayList<FoodImage> listaImagenes = new ArrayList<FoodImage>();
    private Food Alimento;
    private Product product;
    private GridView gridView;
    private ImagesAdapter adapter;
    private int MeGusta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = (GridView) findViewById(R.id.gvImages);
        gridView.setOnItemClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        Bundle args = i.getBundleExtra("BUNDLE");
        listaImagenes = (ArrayList<FoodImage>) args.getSerializable("Imagenes");

        if(b != null){
            Alimento = (Food) b.get("Alimento");
            product = (Product) b.get("Product");
            MeGusta = (int) b.get("MeGusta");
            setTitle(product.getProduct_name());
            showImages(listaImagenes);
        }
    }

    //Muestra el historial
    //historial: Lista de alimentos en el historial
    public void showImages(ArrayList<FoodImage> imagenes) {
        adapter = new ImagesAdapter(getApplicationContext(),imagenes);
        gridView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ImagesDetailActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("Imagenes",listaImagenes);
        intent.putExtra("BUNDLE",args);

        intent.putExtra("Alimento", Alimento);
        intent.putExtra("Product",product);
        intent.putExtra("MeGusta", MeGusta);
        intent.putExtra("indice",i);
        startActivity(intent);
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