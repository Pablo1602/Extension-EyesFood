package com.example.jonsmauricio.eyesfood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.Additive;

import java.util.ArrayList;
import java.util.List;

public class AdditiveActivity extends AppCompatActivity {

    Additive Aditivo;
    public List<Additive> listaAditivos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            Aditivo = (Additive) b.get("Aditivo");
            setTitle(Aditivo.getAdditive());
            listaAditivos.clear();
            listaAditivos.add(Aditivo);
            showAdditive(listaAditivos);
        }
    }

    //Muestra los aditivos
    //historial: Lista de aditivos del alimento
    public void showAdditive(List<Additive> aditivo) {
        //Instancias locales| para el Card view
        RecyclerView recycler;
        AdditivesAdapter adapter;
        RecyclerView.LayoutManager lManager;

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.additiveRecycler);
        recycler.setHasFixedSize(true);

        //Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        //Crear un nuevo adaptador
        //adapter = new AdditivesAdapter(aditivo);
        //recycler.setAdapter(adapter);
    }
}