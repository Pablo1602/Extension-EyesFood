package com.example.jonsmauricio.eyesfood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.OpenFoodFactsApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Additive;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.ProductResponse;
import com.example.jonsmauricio.eyesfood.data.api.model.Rating;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    MaterialSearchView searchView;
    private String query;
    Retrofit mRestAdapter;
    Retrofit mOpenRestAdapter;
    EyesFoodApi mEyesFoodApi;
    OpenFoodFactsApi mOpenFoodApi;
    private List<Food> resultadoAlimentos;
    private List<Additive> resultadoAditivos;
    private ListView resultFoods;
    private ListView resultAdditives;
    private ArrayAdapter<Food> adaptadorFoods;
    private ArrayAdapter<Additive> adaptadorAdditives;
    private View searchProgress;
    TextView searchProgressText;
    TextView searchEmptyState;
    TextView searchFoodsHeader;
    TextView searchAdditivesHeader;
    boolean noFoods;
    boolean noAdditives;
    private ShortFood shortFood;
    private String userIdFinal;
    private int like;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view_search);
        resultFoods = (ListView) findViewById(R.id.lvResultPendientes);
        resultAdditives = (ListView) findViewById(R.id.lvResultAdditives);
        searchProgress = findViewById(R.id.pbSearchProgress);
        searchProgressText = (TextView) findViewById(R.id.tvSearchProgressText);
        searchEmptyState = (TextView) findViewById(R.id.tvSearchEmptyState);
        searchFoodsHeader = (TextView) findViewById(R.id.tvPendientesHeader);
        searchAdditivesHeader = (TextView) findViewById(R.id.tvAceptadosHeader);

        userIdFinal = SessionPrefs.get(this).getUserId();

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mOpenRestAdapter = new Retrofit.Builder()
                .baseUrl(OpenFoodFactsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        mOpenFoodApi = mOpenRestAdapter.create(OpenFoodFactsApi.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultFoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Food currentSearch = adaptadorFoods.getItem(position);
                isFoodInHistory(userIdFinal, currentSearch.getBarCode(), currentSearch);
            }
        });

        resultAdditives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Additive currentSearch = adaptadorAdditives.getItem(position);
                Intent i = new Intent(getApplicationContext(), AdditiveActivity.class);
                i.putExtra("Aditivo", currentSearch);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            showLists(false);
            showProgress(true);
            query = (String) b.get("query");
            makeQueryFoods(query);
            makeQueryAdditives(query);
        }
    }

    public void makeQueryFoods(String query){
        Call<List<Food>> call = mEyesFoodApi.getFoodsQuery(query);
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call,
                                   Response<List<Food>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Falla en la llamada de Foods: makeQueryFoods" + response.message());
                    return;
                }
                resultadoAlimentos = response.body();
                showListFoods(resultadoAlimentos);
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.d("myTag", "Falla en la llamada de aditivos: loadAdditives");
            }
        });
    }

    public void makeQueryAdditives(String query){
        resultadoAditivos = new ArrayList<>();
        showListAdditives(resultadoAditivos);
        /*Call<List<Additive>> call = mEyesFoodApi.getAdditivesQuery(query);
        call.enqueue(new Callback<List<Additive>>() {
            @Override
            public void onResponse(Call<List<Additive>> call,
                                   Response<List<Additive>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Falla en la llamada de aditivos: makeQueryAdditives");
                    return;
                }
                resultadoAditivos = response.body();
                showListAdditives(resultadoAditivos);
            }

            @Override
            public void onFailure(Call<List<Additive>> call, Throwable t) {
                Log.d("myTag", "Falla en la llamada de aditivos: loadAdditives");
            }
        });*/
    }

    public void showListFoods(List<Food> lista){
        int tamanoLista = lista.size();
        //Log.d("myTag", "tamano Lista" + lista.size());
        if(tamanoLista > 0) {
            noFoods = false;
            adaptadorFoods = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    lista);
            resultFoods.setAdapter(adaptadorFoods);
        }
        else{
            noFoods = true;
        }
    }

    public void showListAdditives(List<Additive> lista){
        int tamanoLista = lista.size();
        if(tamanoLista > 0) {
            noAdditives = false;
            adaptadorAdditives = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    lista);
            resultAdditives.setAdapter(adaptadorAdditives);
        }
        else{
            noAdditives = true;
            showEmptyState(noAdditives, noFoods);
        }
        showProgress(false);
        if(!noAdditives || !noFoods){
            showLists(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.searchSearch);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Vacío la lista anterior seteo el empty state y el progress antes de hacer la query
                resultadoAlimentos.clear();
                resultadoAditivos.clear();
                showProgress(true);
                makeQueryFoods(query);
                makeQueryAdditives(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void showProgress(boolean show) {
        if(show) {
            showLists(false);
            showEmptyState(false, false);
        }
        else{
            showEmptyState(noAdditives, noFoods);
        }
        searchProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        searchProgressText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showLists(boolean show){
        if(!show){
            resultFoods.setVisibility(View.GONE);
            searchFoodsHeader.setVisibility(View.GONE);
            resultAdditives.setVisibility(View.GONE);
            searchAdditivesHeader.setVisibility(View.GONE);
        }
        else{
            resultFoods.setVisibility(View.VISIBLE);
            searchFoodsHeader.setVisibility(View.VISIBLE);
            resultAdditives.setVisibility(View.VISIBLE);
            searchAdditivesHeader.setVisibility(View.VISIBLE);
        }
    }

    public void showEmptyState(boolean noAdditives, boolean noFoods){
        if(noAdditives && noFoods){
            searchEmptyState.setVisibility(View.VISIBLE);
        }
        else{
            searchEmptyState.setVisibility(View.GONE);
        }
    }



    //Comprueba si el alimento consultado está en el historial del usuario
    public void isFoodInHistory(String userId, final String barcode, final Food currentSearch){
        Call<ShortFood> call = mEyesFoodApi.isInHistory(userId, barcode);
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call,
                                   Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                //El alimento está en el historial
                shortFood = response.body();
                like = shortFood.getLike();
                Log.d("myTag", "antes de show");
                Log.d("myTag", String.valueOf(like));
                //Log.d("myTag", currentSearch.getName());
                Call<ProductResponse> call2 = mOpenFoodApi.obtenerProducto(barcode);
                call2.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (!response.isSuccessful()) {
                            // TODO: Procesar error de API
                            return;
                        }
                        ProductResponse respuesta = response.body();
                        Product product = respuesta.getProduct();
                        product.setCodigo(respuesta.getCode());
                        showFood(like, currentSearch, product);
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                //El alimento no está
                like=0;
                flag = 1;
                Call<ProductResponse> call2 = mOpenFoodApi.obtenerProducto(barcode);
                call2.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (!response.isSuccessful()) {
                            // TODO: Procesar error de API
                            return;
                        }
                        ProductResponse respuesta = response.body();
                        Product product = respuesta.getProduct();
                        product.setCodigo(respuesta.getCode());
                        showFood(like, currentSearch, product);
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void showFood(int like, Food currentSearch, Product product) {
        Intent i = new Intent(getApplicationContext(), FoodsActivity.class);
        i.putExtra("Alimento", currentSearch);
        i.putExtra("Product", product);
        i.putExtra("MeGusta", like);
        startActivity(i);
    }


}