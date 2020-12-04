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
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Allergy;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.InsertFromLikeBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.ProductResponse;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    MaterialSearchView searchView;
    String query;

    Retrofit mRestAdapter;
    Retrofit mRestAdapter2;
    Retrofit mOpenRestAdapter;
    EyesFoodApi mEyesFoodApi;
    UserDataApi mUserDataApi;
    OpenFoodFactsApi mOpenFoodApi;

    private List<Food> resultadoAlimentos;
    private List<Food> resultadoAlergenos;
    //List<Additive> resultadoAditivos;
    private ListView resultFoods;
    private ListView resultAllergy;
    //private ListView resultAdditives;
    private ArrayAdapter<Food> adaptadorFoods;
    private ArrayAdapter<Food> adaptadorAllergy;
    //private ArrayAdapter<Additive> adaptadorAdditives;
    private View searchProgress;
    TextView searchProgressText;
    TextView searchEmptyState;
    TextView searchFoodsHeader;
    TextView searchAllergyHeader;
    TextView searchAdditivesHeader;
    boolean noFoods;
    boolean noAllergy;
    //boolean noAdditives;
    private ShortFood shortFood;
    private String userIdFinal;
    private int like;
    private int flag;

    private Allergy alergiasUser;
    private int alergiaLeche;
    private int alergiaGluten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view_search);
        resultFoods = (ListView) findViewById(R.id.lvResultPendientes);
        resultAllergy = (ListView) findViewById(R.id.lvResultAllergy);
        //resultAdditives = (ListView) findViewById(R.id.lvResultAdditives);
        searchProgress = findViewById(R.id.pbSearchProgress);
        searchProgressText = (TextView) findViewById(R.id.tvSearchProgressText);
        searchEmptyState = (TextView) findViewById(R.id.tvSearchEmptyState);
        searchFoodsHeader = (TextView) findViewById(R.id.tvPendientesHeader);
        searchAllergyHeader = (TextView) findViewById(R.id.tvAllergyHeader);
        searchAdditivesHeader = (TextView) findViewById(R.id.tvAceptadosHeader);

        userIdFinal = SessionPrefs.get(this).getUserId();

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mOpenRestAdapter = new Retrofit.Builder()
                .baseUrl(OpenFoodFactsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        mOpenFoodApi = mOpenRestAdapter.create(OpenFoodFactsApi.class);

        mUserDataApi = mRestAdapter2.create(UserDataApi.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultFoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Food currentSearch = adaptadorFoods.getItem(position);
                Log.d("FLAGSEARCH","setOnItemClickListener getFoodName "+currentSearch.getFoodName());
                Log.d("FLAGSEARCH","setOnItemClickListener getBarCode "+currentSearch.getBarCode());
                isFoodInHistory(userIdFinal, currentSearch.getBarCode(), currentSearch);
            }
        });

        resultAllergy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Food currentSearch = adaptadorAllergy.getItem(position);
                Log.d("FLAGSEARCH","setOnItemClickListener getFoodName "+currentSearch.getFoodName());
                Log.d("FLAGSEARCH","setOnItemClickListener getBarCode "+currentSearch.getBarCode());
                isFoodInHistory(userIdFinal, currentSearch.getBarCode(), currentSearch);
            }
        });

        /*resultAdditives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Additive currentSearch = adaptadorAdditives.getItem(position);
                Intent i = new Intent(getApplicationContext(), AdditiveActivity.class);
                i.putExtra("Aditivo", currentSearch);
                startActivity(i);
            }
        });*/
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            Log.d("FLAGSEARCH", "Bundle no vacio");
            //showLists(false);
            //showProgress(true);
            query = (String) b.get("query");
            Log.d("FLAGSEARCH","query "+query);
            makeQueryFoods(query);
            prepareAllergyQuery(userIdFinal,query);
            //showProgress(false);
            //showLists(true);
            //makeQueryAdditives(query);
        }
    }

    public void prepareAllergyQuery(String userId, final String query){
        Call<Allergy> call = mUserDataApi.getAllergy(userId);
        call.enqueue(new Callback<Allergy>() {
            @Override
            public void onResponse(Call<Allergy> call,
                                   Response<Allergy> response) {
                if (!response.isSuccessful()) {
                    Log.d("FLAGSEARCH","loadAllergy Error"+response.message());
                    return;
                }
                alergiasUser = response.body();
                if (alergiasUser.getLeche() == 0 && alergiasUser.getGluten() == 0){
                    resultAllergy.setVisibility(View.GONE);
                    searchAllergyHeader.setVisibility(View.GONE);
                }else{
                    makeQueryAllergy(query,alergiasUser);
                }
            }
            @Override
            public void onFailure(Call<Allergy> call, Throwable t) {
                Log.d("FLAGSEARCH","loadAllergy Fallo en API "+t.getMessage());
            }
        });
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
                if (!resultadoAlimentos.isEmpty()) {
                    //for(Food food: resultadoAlergenos){
                    //    Log.d("SEARCHFLAG","makeQueryFoods name:"+food.getFoodName());
                    //}
                    showListFoods(resultadoAlimentos);
                }else{
                    noFoods = true;
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.d("myTag", "Falla en la llamada de aditivos: loadAdditives");
            }
        });
    }

    public void makeQueryAllergy(String query, Allergy alergiasUser){
        Log.d("FLAGSEARCH","makeQueryAllergy alergiasUser.getLeche() "+alergiasUser.getLeche());
        Log.d("FLAGSEARCH","makeQueryAllergy alergiasUser.getGluten() "+alergiasUser.getGluten());
        Call<List<Food>> call2 = mEyesFoodApi.getAllergyQuery(alergiasUser.getLeche(),alergiasUser.getGluten(),query);
        call2.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call,
                                   Response<List<Food>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Falla en la llamada de Foods: makeQueryAllergy" + response.message());
                    return;
                }
                resultadoAlergenos = response.body();
                if (!resultadoAlergenos.isEmpty()) {
                    for(Food food: resultadoAlergenos){
                        Log.d("SEARCHFLAG","makeQueryAllergy name:"+food.getFoodName());
                    }
                    showListFoodsAllergy(resultadoAlergenos);
                }
                else{
                    noAllergy = true;
                }
            }
            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.d("myTag", "Falla en la llamada de aditivos: makeQueryAllergy");
            }
        });
    }

    /*public void makeQueryAdditives(String query){
        resultadoAditivos = new ArrayList<>();
        showListAdditives(resultadoAditivos);
        Call<List<Additive>> call = mEyesFoodApi.getAdditivesQuery(query);
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
        });
    }*/

    public void showListFoods(List<Food> lista){
        int tamanoLista = lista.size();
        //Log.d("myTag", "tamano Lista" + lista.size());
        if(tamanoLista > 0) {
            noFoods = false;
            adaptadorFoods = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            resultFoods.setAdapter(adaptadorFoods);
        }
        else{
            noFoods = true;
        }
    }

    public void showListFoodsAllergy(List<Food> lista){
        int tamanoLista = lista.size();
        //Log.d("myTag", "tamano Lista" + lista.size());
        if(tamanoLista > 0) {
            noAllergy = false;
            adaptadorAllergy = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            resultAllergy.setAdapter(adaptadorAllergy);
        }
        else{
            noAllergy = true;
        }
    }

    /*public void showListAdditives(List<Additive> lista){
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
            showEmptyState(noAdditives, noFoods, noAllergy);
        }
        showProgress(false);
        if(!noAdditives || !noFoods){
            showLists(true);
        }
    }*/

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
                if (!resultadoAlimentos.isEmpty()){
                    resultadoAlimentos.clear();
                }
                /*if (!resultadoAditivos.isEmpty()){
                    resultadoAditivos.clear();
                }*/
                if (!resultadoAlergenos.isEmpty()){
                    resultadoAlergenos.clear();
                }
                //showProgress(true);
                prepareAllergyQuery(userIdFinal,query);
                makeQueryFoods(query);
                //makeQueryAdditives(query);
                //showProgress(false);
                //showLists(true);
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
            showEmptyState(noFoods, noAllergy);
        }
        searchProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        searchProgressText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showLists(boolean show){
        if(!show){
            resultFoods.setVisibility(View.GONE);
            searchFoodsHeader.setVisibility(View.GONE);
            //resultAdditives.setVisibility(View.GONE);
            //searchAdditivesHeader.setVisibility(View.GONE);
            resultAllergy.setVisibility(View.GONE);
            searchAllergyHeader.setVisibility(View.GONE);
        }
        else{
            resultFoods.setVisibility(View.VISIBLE);
            searchFoodsHeader.setVisibility(View.VISIBLE);
            //resultAdditives.setVisibility(View.VISIBLE);
            //searchAdditivesHeader.setVisibility(View.VISIBLE);
            resultAllergy.setVisibility(View.VISIBLE);
            searchAllergyHeader.setVisibility(View.VISIBLE);
        }
    }

    public void showEmptyState(boolean noFoods, boolean noAllergy){
        if(noFoods && noAllergy){
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
                    Log.d("myTag", "Falla en la llamada de Foods: isFoodInHistory " + response.message());
                    return;
                }
                //El alimento está en el historial
                shortFood = response.body();
                like = shortFood.getLike();
                Log.d("FLAGSEARCH","isFoodInHistory antes de show ");
                Log.d("FLAGSEARCH","isFoodInHistory like "+like);
                //Log.d("myTag", currentSearch.getName);
                Call<ProductResponse> call2 = mOpenFoodApi.obtenerProducto(barcode);
                call2.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (!response.isSuccessful()) {
                            // TODO: Procesar error de API
                            return;
                        }
                        ProductResponse respuesta = response.body();
                        Log.d("FLAGSEARCH","isFoodInHistory obtenerProducto "+respuesta.getProduct().getProduct_name());
                        Product product = respuesta.getProduct();
                        product.setCodigo(respuesta.getCode());
                        showFood(like, currentSearch, product);
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Log.d("myTag", "Falla en la llamada de aditivos: makeQueryAllergy");
                    }
                });
            }

            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                //El alimento no está
                Log.d("FLAGSEARCH","isFoodInHistory el alimento no esta");
                like=0;
                flag = 1;
                Call<ProductResponse> call2 = mOpenFoodApi.obtenerProducto(barcode);
                call2.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.d("myTag", "Falla en la llamada de Foods: isFoodInHistory2 " + response.message());
                            return;
                        }
                        ProductResponse respuesta = response.body();
                        insertNoScan(userIdFinal,respuesta.getCode());
                        Log.d("FLAGSEARCH","isFoodInHistory obtenerProducto "+respuesta.getProduct().getProduct_name());
                        Product product = respuesta.getProduct();
                        product.setCodigo(respuesta.getCode());
                        showFood(like, currentSearch, product);
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Log.d("myTag", "Falla en la llamada de aditivos: makeQueryAllergy2 ");
                    }
                });
            }
        });
    }

    private void insertNoScan(String userIdFinal, String codigoBarras) {
        Call<Food> call = mEyesFoodApi.insertNoScan(new InsertFromLikeBody(userIdFinal, codigoBarras, 0));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                else {
                    Log.d("myTag", "Éxito en insertFood");
                }
            }
            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.d("myTag", "Fallo en insertFood " + t.getMessage());
                return;
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