package com.example.jonsmauricio.eyesfood.ui;

import android.Manifest;
import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.OpenFoodFactsApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.HistoryFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.ProductResponse;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.facebook.login.LoginManager;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.zxing.client.android.CaptureActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivity extends AppCompatActivity
        implements OnClickListener, ItemClickListener, GoogleApiClient.OnConnectionFailedListener{

    Retrofit mRestAdapter, mOpenRestAdapter;
    EyesFoodApi mEyesFoodApi;
    private OpenFoodFactsApi mOpenFoodApi;

    //Obtengo id de Usuario y sesión
    private String userIdFinal;
    private String session;
    private String userRolFinal;
    //Bandera para saber si vengo de perfil y empezar la animación
    int perfil = 0;
    //Bandera para ver si seteo el título, los últimos tres items no deben setear el título
    int setTitle;

    //Instancias para el card view
    private RecyclerView recycler;
    private HistoryAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<ShortFood> historial;
    private List<NewFoodBody> pendientes;
    private List<Product> products;

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private TextView emptyStateText, nombre, correo;
    private ImageView avatar;
    private String drawerTitle;

    private String barCode;

    MaterialSearchView searchView;
    private GoogleApiClient googleApiClient;
    private FloatingActionButton fab;
    private FloatingActionsMenu fabProfile;
    //FAB del menú
    private com.getbase.floatingactionbutton.FloatingActionButton fabProfileAdd;
    private com.getbase.floatingactionbutton.FloatingActionButton fabProfileEdit;

    private int like;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userIdFinal = SessionPrefs.get(this).getUserId();
        userRolFinal = SessionPrefs.get(this).getUserId();
        session = SessionPrefs.get(this).getUserSession();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        //navigationView.setNavigationItemSelectedListener(this);

        recycler = (RecyclerView) findViewById(R.id.reciclador);
        progressBar = (ProgressBar) findViewById(R.id.pbMainProgress);
        progressDialog= new ProgressDialog(this);
        emptyStateText = (TextView) findViewById(R.id.tvHistoryEmptyState);
        nombre = headerView.findViewById(R.id.tvNameProfile);
        correo = headerView.findViewById(R.id.tvEmailProfile);
        avatar = headerView.findViewById(R.id.ivProfile);

        // Crear conexión al servicio REST EyesFood
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión al servicio REST OpenFoodFacts
        mOpenRestAdapter = new Retrofit.Builder()
                .baseUrl(OpenFoodFactsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        //  Crear conexión a la API de OpenFood
        mOpenFoodApi = mOpenRestAdapter.create(OpenFoodFactsApi.class);

        drawerTitle = getResources().getString(R.string.nav_history);
        // Redirección al Login
        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabProfile = (FloatingActionsMenu) findViewById(R.id.fabProfile);

        fabProfileAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabProfileAdd);
        fabProfileEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabProfileEdit);

        fab.setOnClickListener(this);
        fabProfileAdd.setOnClickListener(this);
        fabProfileEdit.setOnClickListener(this);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        if (savedInstanceState == null) {
            selectItem(drawerTitle);
        }

        nombre.setText(SessionPrefs.get(this).getUserName());
        correo.setText(SessionPrefs.get(this).getUserEmail());
        if (SessionPrefs.get(this).getUserPref().equals("EyesFood")){
            Picasso.with(this)
                    .load(EyesFoodApi.BASE_URL_PHOTO + SessionPrefs.get(this).getUserPhoto())
                    .into(avatar);
        }else{
            Picasso.with(this)
                    .load(SessionPrefs.get(this).getUserPhoto())
                    .into(avatar);
        }
        //Toast.makeText(getApplicationContext(), "showHistory", Toast.LENGTH_SHORT).show();
    }

    //Método de actualización
    /*@Override
    protected void onResume() {
        super.onResume();
    }*/

    //Carga alimentos en el historial
    //UserId: Id de usuario
    // TODO: 19-10-2017 Cuando haya más listas los métodos son iguales a este
    public void loadHistoryFoods(String userId, String title) {
        //Hacer el if según el título
        if(title.equals(getResources().getString(R.string.nav_history))){
            //Llama a la función que carga el historial de escaneo
            loadScan(userId);
        }
        else if(title.equals(getResources().getString(R.string.nav_uploads))){
            //Llama a la función que carga el historial de subidos
//            loadUploads(userId);
        }
        else if(title.equals(getResources().getString(R.string.nav_favorites))){
            //Llama a la función que carga los destacados
            loadFavorites(userId);
        }
        //Rechazados
        else{
            //Llama a la función que carga los rechazados
            loadRejected(userId);
        }
    }

    public void loadScan(String userId){
        Log.d("myTag","En load Scan");
        Call<List<ShortFood>> call = mEyesFoodApi.getFoodsInHistory(userId);
        call.enqueue(new Callback<List<ShortFood>>() {
            @Override
            public void onResponse(Call<List<ShortFood>> call,
                                   Response<List<ShortFood>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "hola"+response.errorBody().toString());
                    return;
                }
                historial = response.body();
                showHistory(historial);
            }

            @Override
            public void onFailure(Call<List<ShortFood>> call, Throwable t) {
                Log.d("Falla", "Falla en la llamada a historial: loadHistoryFoods"+t.getMessage());
            }
        });
    }

    //Destacados
    public void loadFavorites(String userId){
        Call<List<ShortFood>> call = mEyesFoodApi.getFoodsFavorites(userId);
        call.enqueue(new Callback<List<ShortFood>>() {
            @Override
            public void onResponse(Call<List<ShortFood>> call,
                                   Response<List<ShortFood>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "hola");
                    return;
                }

                historial = response.body();
                showHistory(historial);
            }

            @Override
            public void onFailure(Call<List<ShortFood>> call, Throwable t) {
                Log.d("Falla", "Falla en la llamada a historial: loadHistoryFoods");
            }
        });
    }

    public void loadRejected(String userId){
        Call<List<ShortFood>> call = mEyesFoodApi.getFoodsRejected(userId);
        call.enqueue(new Callback<List<ShortFood>>() {
            @Override
            public void onResponse(Call<List<ShortFood>> call,
                                   Response<List<ShortFood>> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "hola");
                    return;
                }

                historial = response.body();
                showHistory(historial);
            }

            @Override
            public void onFailure(Call<List<ShortFood>> call, Throwable t) {
                Log.d("Falla", "Falla en la llamada a historial: loadHistoryFoods");
            }
        });
    }

    //Muestra el historial
    //historial: Lista de alimentos en el historial
    public void showHistory(final List<ShortFood> historial2) {
        products = new ArrayList<>();
        ArrayList<Call<ProductResponse>> productResponseCalls = new ArrayList<>();
        for (ShortFood food : historial2) {
            productResponseCalls.add(mOpenFoodApi.obtenerProducto(food.getBarCode()));
        }
        Log.d("myTag", "En show History ");
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
                        if (products.size()==historial2.size()){
                            showHistory2(historial2,products);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {

                }
            });
        }
    }

    private void showHistory2(List<ShortFood> historial, List<Product> products) {
        for (int i = 0; i < historial.size(); i++) {
            for (int j= 0; j < products.size(); j++){
                if (historial.get(i).getBarCode().equals(products.get(j).getCodigo())){
                    historial.get(i).setName(products.get(j).getProduct_name());
                    historial.get(i).setOfficialPhoto(products.get(j).getImage_front_url());
                }
            }
        }
/*         if(historial.isEmpty()){
            showEmptyState(true);
            showProgress(false);
            return;
        }

        showEmptyState(false);*/
        // Obtener el Recycler
        // TODO: 20-11-2017 Aquí lo obtengo pero ya está obtenido en onCreate
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        //Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        //Crear un nuevo adaptador
        adapter = new HistoryAdapter(historial, this);
        recycler.setAdapter(adapter);
        adapter.setClickListener(this);
        showProgress(false,"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:{
                //Si no hay permiso se pide
                if (ContextCompat.checkSelfPermission(HistoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HistoryActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
                //Si hay permiso se va al escáner
                else{
                    Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
                    intent.setAction("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SAVE_HISTORY", false);
                    startActivityForResult(intent, 0);
                }
                break;
            }
            case R.id.fabProfileAdd:{
                showSelectedDialog(2);
                break;
            }
            case R.id.fabProfileEdit:{
                showSelectedDialog(3);
                break;
            }
        }
    }

    //OnClick para la lista de cards, se necesita sobreescribir porque la clase implementa este método
    //Sobreescribe itemClickListener
    @Override
    public void onClick(View view, int position) {
        ShortFood food = historial.get(position);
        like = food.getLike();
        loadFoodsFromHistory(food.getBarCode());
    }

    //Retorna un alimento al pinchar en el historial
    //Barcode: Código de barras del alimento a retornar
    public void loadFoodsFromHistory(String barcode) {
        progressDialog.setMessage("Cargando Producto");
        progressDialog.show();
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
                showFoodsScreen(product);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                noFood();
            }
        });
    }

    //Nuevo ShowFoods para consulta a OpenFoods
    private void showFoodsScreen(final Product product) {
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
                showFoodsScreenFinal(resultado,product);
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                progressDialog.dismiss();
                noFood();
            }
        });
    }

    private void showFoodsScreenFinal(Food resultado, Product product) {
        Intent i = new Intent(this, FoodsActivity.class);
        i.putExtra("Product",product);
        i.putExtra("Alimento",resultado);
        i.putExtra("MeGusta",like);
        progressDialog.dismiss();
        startActivity(i);
    }

    //Retorna un alimento
    //Token: Token de autorización
    //Barcode: Código de barras del alimento a retornar
    public void loadFoods(final String barcode) {
        Call<ProductResponse> call = mOpenFoodApi.obtenerProducto(barcode);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (!response.isSuccessful()) {
                    // TODO: Procesar error de API
                    Log.d("myTag", "Error api OpenFood");
                    return;
                }
                final Product product = response.body().getProduct();
                if (response.body().getProduct()!=null){product.setCodigo(response.body().getCode());}
                //Si existe en OpenFood, se procede a ver si existe en EyesFood
                Call<Food> call2 = mEyesFoodApi.getFood(barcode);
                call2.enqueue(new Callback<Food>() {
                    @Override
                    public void onResponse(Call<Food> call, Response<Food> response) {
                        if (!response.isSuccessful()) {
                            // TODO: Procesar error de API
                            Log.d("myTag", "Error: API EYESFOOD");
                            return;
                        }
                        //Si el producto existe en BD EyesFood, se obtiene
                        Log.d("myTag", "Nombre Producto: "+product.getProduct_name());
                        isFoodInHistory(userIdFinal, product);
                    }

                    @Override
                    public void onFailure(Call<Food> call, Throwable t) {
                        //Si existe en OpenFood pero no en EyesFood, se crea
                        if (product==null){
                            Log.d("myTag", "No existe producto en OpenFoods: ");
                            noFood();
                        }else{
                            Log.d("myTag", "Existe: "+product.getProduct_name());
                            createProduct(product);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressDialog.dismiss();
                noFood();
            }
        });
    }

    private void createProduct(final Product product) {
        Call<Food> call = mEyesFoodApi.newFood(new Food(product.getCodigo(), null, 0,"" , 0, product.getProduct_name()));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Falla en ingreso" + response.message());
                    return;
                }
                Log.d("myTag", "Ingreso");
                //Si se ingresa a EyesFood, se procede a ingresar al historial
                isFoodInHistory(userIdFinal,product);
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    //Comprueba si el alimento consultado está en el historial del usuario
    private void isFoodInHistory(final String userIdFinal, final Product product) {
        Call<ShortFood> call = mEyesFoodApi.isInHistory(userIdFinal, product.getCodigo());
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call, Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Mala respuesta. " + response.toString());
                    return;
                }
                //El alimento está en el historial
                //El alimento no está en ninguna lista, lista = 0, lo inserto
                Log.d("myTag", "Ya existe en el historial");
                showFoodsScreen(product);
            }

            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                //El alimento no está y lo inserto
                Log.d("myTag", "No esta, lo inserto");
                insertFood(userIdFinal, product);
            }
        });
    }

    //Muestra el diálogo si es que no existe el alimento para agregarlo
    // TODO: 19-10-2017 Actualizar agregar alimento
    public void noFood(){
        new AlertDialog.Builder(this)
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_new_foods_question))
                .setMessage(getResources().getString(R.string.message_new_foods_question))
                .setPositiveButton(getResources().getString(R.string.possitive_dialog), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showNewFoodsDialog();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.negative_dialog), null)
                .show();
        progressDialog.dismiss();
    }

    private void showNewFoodsDialog(){
        progressDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("barCode", barCode);
        // set Fragmentclass Arguments

        FragmentManager fragmentManager = getSupportFragmentManager();
        NewFoodsDialogFragment newFragment = new NewFoodsDialogFragment();
        newFragment.setArguments(bundle);

        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    //Pide el permiso para acceder a la cámara
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])){
            showDialogs();
        }else{
            if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            } else{
                showDialogs();
            }
        }
    }

    public void showDialogs(){
        new AlertDialog.Builder(this)
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_foods_permission_rationale_camera))
                .setMessage(getResources().getString(R.string.message_history_permission_rationale_camera))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }

                })
                .show();
    }

    //Procesa lo obtenido por el escáner
    //TODO: quitar barcode de mantequilla para scanner
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            //Si obtiene el código
            if (resultCode == RESULT_OK) {
                barCode = intent.getStringExtra("SCAN_RESULT");
                //Mantequilla
                //barCode = "7802920001326";
                //Iansa cerok
                //barCode = "7801505000877";
                //Log.d("myTag","Barcode = "+barCode);
                progressDialog.setMessage("Cargando Producto");
                progressDialog.show();
                loadFoods(barCode);
            }
            //Si no obtiene el código
            else if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //Inserta un alimento en el historial
    public void insertFood(String userId, final Product barcode){
        Call<Food> call = mEyesFoodApi.insertInHistory(new HistoryFoodBody(userIdFinal, barcode.getCodigo()));
        //Call<Food> call = mEyesFoodApi.insertNoScan(new InsertFromLikeBody(userId, barcode,0));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Mala respuesta en insertFood" + response.toString());
                    return;
                }
                else {
                    Log.d("myTag", "Mostrar Producto leido");
                    progressDialog.setMessage("Cargando Producto");
                    showFoodsScreen(barcode);
                }
            }
            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.d("myTag", "Fallo en insertFood");
                return;
            }
        });
    }

    //Modifica la bandera del escaneo
    public void modifyScan(String userId, String barcode){
        Call<ShortFood> call = mEyesFoodApi.modifyHistoryScan(userId, barcode);
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call, Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                else {
                    Log.d("myTag", "Éxito en modifyScan");
                }
            }
            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                Log.d("myTag", "Fallo en modifyScan " + t.getMessage());
                return;
            }
        });
    }

    /*//Actualiza la fecha de un alimento en el historial
    public void updateHistory(String userId, String barcode){
        Call<ShortFood> call = mEyesFoodApi.modifyHistory(userId, barcode);
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call, Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en updateHistory " + response.errorBody());
                    return;
                }
                else {
                    Log.d("myTag", "Éxito en updateHistory");
                }
            }
            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                Log.d("myTag", "Fallo en updateHistory "+ t.getMessage());
                return;
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(null)
                    .setTitle("Salir")
                    .setMessage("¿Está seguro que desea salir de la aplicación?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);

        MenuItem item = menu.findItem(R.id.searchHistory);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("query", query);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(session.equals("Facebook")) {
                LoginManager.getInstance().logOut();
                Log.d("myTag","facebook");
            }
            else if(session.equals("Gmail")) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
                Log.d("myTag","gmail");
            }
            Log.d("myTag","eyesfood");
            SessionPrefs.get(HistoryActivity.this).logOut();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bt_nav_history) {
            drawerSelection = 1;
        } else if (id == R.id.bt_nav_upload) {
            drawerSelection = 2;
        } else if (id == R.id.bt_nav_favorite) {
            drawerSelection = 3;
        } else if (id == R.id.bt_nav_rejected) {
            drawerSelection = 4;
        } else if (id == R.id.bt_nav_profile) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                            //menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        if (title.equals("Subidos")){
                            Intent i = new Intent(getApplicationContext(), UploadActivity.class);
                            i.putExtra("modo",1);
                            startActivity(i);
                        }else if(title.equals("Editados")){
                            Intent i = new Intent(getApplicationContext(), UploadActivity.class);
                            i.putExtra("modo",2);
                            startActivity(i);
                        }else{
                            selectItem(title);
                        }
                        return true;
                    }
                }
        );
    }

    private void selectItem(String title) {
        //Lo esconde el profile así que aquí lo reestablezco
        fab.setVisibility(View.VISIBLE);
        fabProfile.collapse();
        fabProfile.setVisibility(View.GONE);
        showProgress(true,"");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment, fragmentHelp, fragmentExpert;
        // Enviar título como arguemento del fragmento
        if(title.equals(getResources().getString(R.string.nav_profile))){
            fab.setVisibility(View.GONE);
            fabProfile.setVisibility(View.VISIBLE);
            animateFabMenu(fabProfile);
            setTitle = 1;
            perfil = 1;
            showProgress(false,"PERFIL");
            Bundle args = new Bundle();
            args.putString(ProfileFragment.ARG_SECTION_TITLE, title);

            Fragment fragmentProfile = ProfileFragment.newInstance(title);
            fragmentProfile.setArguments(args);

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.llMainActivity, fragmentProfile, "fragmento_perfil")
                    .commit();
        }
        else if(title.equals(getResources().getString(R.string.nav_experts))){
            showSelectedDialog(0);
            //Si vengo del perfil oculto el recycler
            if (perfil == 1) {
                showProgress(false,"PERFIL");
                fab.setVisibility(View.GONE);
                fabProfile.setVisibility(View.VISIBLE);
            }
            else{
                showProgress(false,"");
            }
            setTitle = 0;
        }
        else if(title.equals(getResources().getString(R.string.nav_settings))){
            setTitle = 0;
        }
        else if(title.equals(getResources().getString(R.string.nav_help))){
            showSelectedDialog(1);
            //Si vengo del perfil oculto el recycler y muestro el fab correspondiente
            if (perfil == 1) {
                showProgress(false,"PERFIL");
                fab.setVisibility(View.GONE);
                fabProfile.setVisibility(View.VISIBLE);
            }
            else{
                showProgress(false,"");
            }
            setTitle = 0;
        }else if(title.equals(getResources().getString(R.string.nav_stores))){
            showSelectedDialog(4);
            //Si vengo del perfil oculto el recycler
            if (perfil == 1) {
                showProgress(false,"PERFIL");
                fab.setVisibility(View.GONE);
                fabProfile.setVisibility(View.VISIBLE);
            }
            else{
                showProgress(false,"");
            }
            setTitle = 0;
        }
        else{
            if(perfil == 1){
                //Si vengo de perfil animo el fab
                animateFab(fab);
                perfil = 0;
            }
            setTitle = 1;
            //Llamar a show foods con otros argumentos
            fragment = fragmentManager.findFragmentByTag("fragmento_perfil");
            fragmentExpert = fragmentManager.findFragmentByTag("fragmento_expertos");
            fragmentHelp = fragmentManager.findFragmentByTag("fragmento_ayuda");

            //Si está el fragmento del perfil se elimina
            if(fragment != null){
                Log.d("fragmento","Está el perfil");
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
            if(fragmentExpert != null){
                Log.d("fragmento","Está expertos");
                fragmentManager.beginTransaction().remove(fragmentExpert).commit();
            }
            if(fragmentHelp != null){
                Log.d("fragmento","Está ayudas");
                fragmentManager.beginTransaction().remove(fragmentHelp).commit();
            }
            //Si no está se hacen las llamadas sin eliminar nada
            //Este se llama con el título de la vista para seleccionar la lista
            loadHistoryFoods(userIdFinal, title);
        }
        if(setTitle == 1) {
            // Setear título actual
            // TODO: 23-11-2017 toolbar.setTitle no funcionaba la primera vez que entraba a la aplicación
            setTitle(title);
            toolbar.setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showProgress(boolean show, String bandera) {
        if(show) {
            progressBar.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        else{
            if(bandera.equals("PERFIL")){
                progressBar.setVisibility(View.GONE);
            }
            else{
                progressBar.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }

        }
    }

    private void showEmptyState(boolean show){
        if(show){
            emptyStateText.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        else{
            emptyStateText.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }

    private void showSelectedDialog(int seleccion){
        //Bundle bundle = new Bundle();

        FragmentManager fragmentManager = getSupportFragmentManager();

        ExpertsFragment expertsFragment = new ExpertsFragment();
        HelpFragment helpFragment = new HelpFragment();
        StoresFragment storeFragment = new StoresFragment();

        NewMeasureFragment newMeasureFragment = new NewMeasureFragment();
        EditMeasureFragment editMeasureFragment = new EditMeasureFragment();
        //expertsFragment.setArguments(bundle);
        //newFragmentUpload.setArguments(bundle);


        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        //transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        /*transaction.add(android.R.id.content, expertsFragment, "fragmento_expertos").addToBackStack(null);
        transaction.add(android.R.id.content, helpFragment, "fragmento_ayuda").addToBackStack(null);*/

        if(seleccion == 0){
            //transaction.replace(android.R.id.content, expertsFragment);
            transaction.add(android.R.id.content, expertsFragment, "fragmento_expertos").addToBackStack(null);
        }
        else if (seleccion ==1){
            //transaction.replace(android.R.id.content, helpFragment);
            transaction.add(android.R.id.content, helpFragment, "fragmento_ayuda").addToBackStack(null);
        }
        else if (seleccion ==2){
            //transaction.replace(android.R.id.content, helpFragment);
            transaction.add(android.R.id.content, newMeasureFragment, "fragmento_nueva_medida").addToBackStack(null);
        }
        else if (seleccion ==3){
            //transaction.replace(android.R.id.content, helpFragment);
            transaction.add(android.R.id.content, editMeasureFragment, "fragmento_editar_medida").addToBackStack(null);
        }else if (seleccion ==4){
            Bundle bundle = new Bundle();
            bundle.putInt("Menu", 1);
            storeFragment.setArguments(bundle);
            //transaction.replace(android.R.id.content, expertsFragment);
            transaction.add(android.R.id.content, storeFragment, "fragmento_tiendas").addToBackStack(null);
        }
        transaction.commit();
    }

    private void animateFab(final FloatingActionButton fab) {
        fab.setScaleX(0);
        fab.setScaleY(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.overshoot);

            fab.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(400)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fab.animate()
                                    .scaleY(1)
                                    .scaleX(1)
                                    .setInterpolator(interpolador)
                                    .setDuration(600)
                                    .start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

    private void animateFabMenu(final FloatingActionsMenu fab){
        fab.setScaleX(0);
        fab.setScaleY(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.overshoot);

            fab.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(interpolador)
                    .setDuration(600)
                    .setStartDelay(400)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fab.animate()
                                    .scaleY(1)
                                    .scaleX(1)
                                    .setInterpolator(interpolador)
                                    .setDuration(600)
                                    .start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }
}