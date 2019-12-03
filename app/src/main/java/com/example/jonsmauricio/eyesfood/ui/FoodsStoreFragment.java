package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.OpenFoodFactsApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodStore;
import com.example.jonsmauricio.eyesfood.data.api.model.HistoryFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.ProductResponse;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.api.model.Store;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodsStoreFragment extends DialogFragment {

    private ListView listView;

    Retrofit mRestAdapter, mRestAdapter2;
    private String userIdFinal;
    EyesFoodApi mEyesFoodApi;
    OpenFoodFactsApi mOpenFoodApi;
    private List<FoodStore> listaFoods;
    private ArrayAdapter<FoodStore> adaptadorFoods;
    Store tienda;
    private List<Product> products;
    private ProgressDialog progressDialog;
    int menu;
    String barcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_foods, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarFoodsStore);
        toolbar.setTitle(getResources().getString(R.string.title_activity_foods));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        userIdFinal = SessionPrefs.get(getActivity()).getUserId();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        progressDialog= new ProgressDialog(getActivity());
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

        // Crear conexión al servicio REST OpenFoodFacts
        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(OpenFoodFactsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        // Crear conexión a la API de OpenFood
        mOpenFoodApi = mRestAdapter2.create(OpenFoodFactsApi.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tienda = (Store) bundle.getSerializable("Tienda");
            menu = bundle.getInt("Menu");
            if (menu == 2) {
                barcode = bundle.getString("barcode");
            }
            //toolbar.setTitle("Alimentos");
            retrieveFoods();
        }

        return view;
    }

    private void retrieveFoods() {
        Call<List<FoodStore>> call = mEyesFoodApi.getFoodsStore(tienda.getId());
        call.enqueue(new Callback<List<FoodStore>>() {
            @Override
            public void onResponse(Call<List<FoodStore>> call,
                                   Response<List<FoodStore>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "hola"+response.errorBody().toString());
                    return;
                }
                listaFoods = response.body();
                showFoodsOpenFood(listaFoods);
            }

            @Override
            public void onFailure(Call<List<FoodStore>> call, Throwable t) {
                Log.d("Falla", "Falla en la llamada a historial: getFoodsStore"+t.getMessage());
            }
        });
    }

    private void showFoodsOpenFood(final List<FoodStore> listaFoods) {
        products = new ArrayList<>();
        ArrayList<Call<ProductResponse>> productResponseCalls = new ArrayList<>();
        for (FoodStore food : listaFoods) {
            productResponseCalls.add(mOpenFoodApi.obtenerProducto(food.getBarCode()));
        }

        for (Call<ProductResponse> call2 : productResponseCalls){
            call2.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    if (response.isSuccessful()){
                        ProductResponse productResponse = response.body();
                        Product product = productResponse.getProduct();
                        product.setCodigo(productResponse.getCode());
                        //Log.d("nombreEnConsulta", product.getProduct_name());
                        products.add(product);
                        if (products.size()==listaFoods.size()){
                            showHistory(listaFoods,products);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {

                }
            });
        }
    }

    private void showHistory(List<FoodStore> listaFoods, List<Product> products) {
        for (int i = 0; i < listaFoods.size(); i++) {
            for (int j= 0; j < products.size(); j++){
                if (listaFoods.get(i).getBarCode().equals(products.get(j).getCodigo())){
                    listaFoods.get(i).setPhoto(products.get(j).getImage_front_url());
                }
            }
        }

        // Inicializar el adaptador con la fuente de datos.
        adaptadorFoods = new FoodAdapter(getContext(), listaFoods);
        //Relacionando la lista con el adaptador
        listView.setAdapter(adaptadorFoods);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = getView().findViewById(R.id.lvFoodsStore);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressDialog.setMessage("Cargando Producto");
                progressDialog.show();
                FoodStore food = adaptadorFoods.getItem(i);
/*                Log.d("myTag","AdaptadorFoods: "+food.getBarCode());
                Log.d("myTag","Products: "+products.size());*/
                Product product = busca(food.getBarCode(),products);
                compruebaHistorial(product);
            }
        });
    }

    private void compruebaHistorial(final Product product) {
        Call<ShortFood> call = mEyesFoodApi.isInHistory(userIdFinal,product.getCodigo());
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call, Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "hola"+response.errorBody().toString());
                    return;
                }
                ShortFood food = response.body();
                Log.d("myTag", "Food "+food.getBarCode());
                loadFood(product, food.getLike());
            }

            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                //Si Falla se inserta en el historial
                insertFood(product);
            }
        });
    }

    private void insertFood(final Product product) {
        Call<Food> call = mEyesFoodApi.insertInHistory(new HistoryFoodBody(userIdFinal, product.getCodigo()));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Mala respuesta en insertFood" + response.toString());
                    return;
                }
                else {
                    //Log.d("myTag", "Mostrar Producto leido");
                    loadFood(product,0);
                }
            }
            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.d("myTag", "Fallo en insertFood");
                return;
            }
        });
    }

    private void loadFood(final Product product, final int like) {
        Call<Food> call = mEyesFoodApi.getFood(product.getCodigo());
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    return;
                }
                //Si entro acá el alimento existe en la BD y lo obtengo
                Food resultado = response.body();

                //Muestro el alimento
                showFoodsScreenFinal(resultado,product, like);
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void showFoodsScreenFinal(Food resultado, Product product, int like) {
        Intent i = new Intent(getActivity(), FoodsActivity.class);
        i.putExtra("Product",product);
        i.putExtra("Alimento",resultado);
        i.putExtra("MeGusta",like);
        progressDialog.dismiss();
        startActivity(i);
    }

    private Product busca(String barCode, List<Product> listaFoods) {
        for (int i = 0; i < listaFoods.size(); i++) {
            if (listaFoods.get(i).getCodigo().equals(barCode)){
                return listaFoods.get(i);
            }
        }
        return null;
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
            Bundle bundle = new Bundle();
            bundle.putSerializable("Tienda", tienda);
            bundle.putInt("Alimentos", listaFoods.size());
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
            return true;
        }
        return true;
    }
}
