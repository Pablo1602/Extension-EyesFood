package com.example.jonsmauricio.eyesfood.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.OpenFoodFactsApi;
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Allergy;
import com.example.jonsmauricio.eyesfood.data.api.model.Comment;
import com.example.jonsmauricio.eyesfood.data.api.model.Counter;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodImage;
import com.example.jonsmauricio.eyesfood.data.api.model.InsertFromLikeBody;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Nutriments;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.Recommendation;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.api.model.Store;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    Clase controladora de la actividad de alimentos
*/

// TODO: 16-11-2017 Ver el color de los botones no presionados

public class FoodsActivity extends AppCompatActivity implements View.OnClickListener {

    //Para la info general
    TextView infoGeneralNombre, infoGeneralProducto, infoGeneralCodigo, infoGeneralMarca, infoGeneralNeto,
            infoGeneralFecha, tvIngredientes, infoGeneralAlergenos, infoGeneralTrazas;

    ImageView ivNutricion, ivNova;

    //Para la info nutricional
    TextView porcion, porcionEnvase, energia100, energiaPorcion, proteinas100, proteinasPorcion, grasaTotal100,
    grasaTotalPorcion, grasaSaturada100, grasaSaturadaPorcion, grasaMono100, grasaMonoPorcion, grasaPoli100, grasaPoliPorcion,
    grasaTrans100, grasaTransPorcion, colesterol100, colesterolPorcion, hidratos100, hidratosPorcion, azucares100,
    azucaresPorcion, fibra100, fibraPorcion, sodio100, sodioPorcion;

    String CodigoBarras;
    //RatingBar infoGeneralRating;
    private String userIdFinal;

    //Para los botonos
    Button additives, recommendations, images, like, dislike, edits, stores;

    private List<Recommendation> listaRecomendaciones;
    private ArrayList<FoodImage> listaImagenes;
    private List<Comment> listaComentarios;
    private List<Store> listaTiendas;
    ImageView ivFoodPhoto;

    Retrofit mRestAdapter, mRestAdapter2, mRestAdapter3;
    private UserDataApi mUserDataApi;
    private EyesFoodApi mEyesFoodApi;
    private CommentsApi mCommentsApi;
    private Food Alimento;
    private Product product;
    private NewFoodBody pendiente;
    private int MeGusta;

    private Counter likesCounter;
    private int likesCount;
    private Counter dislikesCounter;
    private int dislikesCount;
    private String tipo;
    private String alergeno;
    private String traza;
    private String gradoNutri;
    private int nova;

    private Allergy alergiasUser;

    //Permissions
    private static final int PERMISSION_CODE = 123;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private List<NewFoodBody> listaEdits;

    //IP de usach alumnos:
    //private final String baseFotoAlimento = "http://158.170.214.219/api.eyesfood.cl/v1/img/food/";
    //URL Base para cargar las fotos
    //final String baseFotoAlimento = EyesFoodApi.BASE_URL+"img/food/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFoods);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFoods);
        FloatingActionButton fabPhoto = (FloatingActionButton) findViewById(R.id.fabPhotoFoods);
        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEditFoods);

        fab.setOnClickListener(this);
        fabPhoto.setOnClickListener(this);
        fabEdit.setOnClickListener(this);

        //Para la info general
        infoGeneralNombre = (TextView) findViewById(R.id.tvFoodsInfoGeneralNombre);
        infoGeneralProducto = (TextView) findViewById(R.id.tvFoodsInfoGeneralProducto);
        infoGeneralCodigo = (TextView) findViewById(R.id.tvFoodsInfoGeneralCodigo);
        infoGeneralMarca = (TextView) findViewById(R.id.tvFoodsInfoGeneralMarca);
        infoGeneralNeto = (TextView) findViewById(R.id.tvFoodsInfoGeneralNeto);
        infoGeneralFecha = (TextView) findViewById(R.id.tvFoodsInfoGeneralFecha);
        //infoGeneralRating = (RatingBar) findViewById(R.id.rbFoodsRating);
        infoGeneralAlergenos = (TextView) findViewById(R.id.tvFoodsInfoGeneralAllergy);
        infoGeneralTrazas = (TextView) findViewById(R.id.tvFoodsInfoGeneralTrace);
        ivNutricion = (ImageView) findViewById(R.id.ivFoodsNutrition);
        ivNova = (ImageView) findViewById(R.id.ivFoodsNova);

        //Para la info nutricional
        porcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalPorcion);
        porcionEnvase = (TextView) findViewById(R.id.tvFoodsInfoNutricionalPorcionEnvase);
        energia100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalEnergia100);
        energiaPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalEnergiaPorcion);
        proteinas100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalProteinas100);
        proteinasPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalProteinasPorcion);
        grasaTotal100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaTotal100);
        grasaTotalPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaTotalPorcion);
        grasaSaturada100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaSaturada100);
        grasaSaturadaPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaSaturadaPorcion);
        grasaMono100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaMono100);
        grasaMonoPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaMonoPorcion);
        grasaPoli100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaPoli100);
        grasaPoliPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaPoliPorcion);
        grasaTrans100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaTrans100);
        grasaTransPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalGrasaTransPorcion);
        colesterol100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalColesterol100);
        colesterolPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalColesterolPorcion);
        hidratos100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalHidratos100);
        hidratosPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalHidratosPorcion);
        azucares100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalAzucares100);
        azucaresPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalAzucaresPorcion);
        fibra100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalFibra100);
        fibraPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalFibraPorcion);
        sodio100 = (TextView) findViewById(R.id.tvFoodsInfoNutricionalSodio100);
        sodioPorcion = (TextView) findViewById(R.id.tvFoodsInfoNutricionalSodioPorcion);

        //Para los ingredientes
        tvIngredientes = (TextView) findViewById(R.id.tvFoodsIngredients);

        //Para los botones
        stores = (Button) findViewById(R.id.btFoodsStores);
        additives = (Button) findViewById(R.id.btFoodsAdditives);
        recommendations = (Button) findViewById(R.id.btFoodsRecommendations);
        images = (Button) findViewById(R.id.btFoodsImages);
        like = (Button) findViewById(R.id.btFoodsLike);
        dislike = (Button) findViewById(R.id.btFoodsDisLike);
        edits = (Button) findViewById(R.id.btFoodsEdits);
        stores.setOnClickListener(this);
        additives.setOnClickListener(this);
        recommendations.setOnClickListener(this);
        images.setOnClickListener(this);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);
        edits.setOnClickListener(this);

        ivFoodPhoto = (ImageView) findViewById(R.id.image_paralax);
        final CollapsingToolbarLayout collapser = (CollapsingToolbarLayout) findViewById(R.id.collapser);

        userIdFinal = SessionPrefs.get(this).getUserId();

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestAdapter3 = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        // Crear conexión a la API de Comentarios
        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        // Crear conexión a la API de Usuario data
        mUserDataApi = mRestAdapter3.create(UserDataApi.class);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        //Recibe los datos enviados por el scanner o lista
        if(b!=null)
        {

            Alimento = (Food) b.get("Alimento");
            product = (Product)b.get("Product");
            pendiente = (NewFoodBody) b.get("pendiente");
            if (Alimento==null && product == null){
                collapser.setTitle(pendiente.getName());
                CodigoBarras = pendiente.getBarcode();
                //pendiente.setAllergens(pendiente.getAllergens().replace("(es)","").replace("en:","").replace("es:","").replace("(fr)",""));
                //pendiente.setTraces(pendiente.getTraces().replace("(es)","").replace("en:","").replace("es:","").replace("(fr)",""));
                fab.setVisibility(View.GONE);
                fabPhoto.setVisibility(View.GONE);
                fabEdit.setVisibility(View.GONE);
                additives.setVisibility(View.GONE);
                recommendations.setVisibility(View.GONE);
                images.setVisibility(View.GONE);
                like.setVisibility(View.GONE);
                dislike.setVisibility(View.GONE);
                //infoGeneralRating.setVisibility(View.GONE);
                tipo = "2";
            }else{
                collapser.setTitle(product.getProduct_name()); // Cambiar título
                //setTitle(Nombre);
                CodigoBarras = product.getCodigo();
                product.setAllergens(product.getAllergens().replace("(es)","").replace("en:","").replace("es:","").replace("(fr)",""));
                product.setTraces(product.getTraces().replace("(es)","").replace("en:","").replace("es:","").replace("(fr)",""));
                MeGusta = (int) b.get("MeGusta");
                Log.d("myTag","Like: "+MeGusta);
                tipo = "1";
            }
            //showFood(Alimento);
        }
        prepareShowFood(userIdFinal,product,Alimento);
        showNutritionFacts(product);
        loadIngredients();
        getLikesCount(CodigoBarras, like);
        getDisLikesCount(CodigoBarras, dislike);

        if(MeGusta == 1) {
            like.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        }
        else if(MeGusta == 2){
            dislike.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        }
        ivNutricion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cl.openfoodfacts.org/nutriscore"));
                startActivity(browserIntent);
            }
        });

        ivNova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cl.openfoodfacts.org/nova"));
                startActivity(browserIntent);
            }
        });
    }

    public void prepareShowFood(String userId, final Product product, final Food Alimento){
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
                showFood(product,Alimento,alergiasUser);
            }
            @Override
            public void onFailure(Call<Allergy> call, Throwable t) {
                Log.d("FLAGSEARCH","loadAllergy Fallo en API "+t.getMessage());
            }
        });
    }

    //Carga los datos del alimento al iniciar la pantalla
    //alimento: Alimento a cargar
    private void showFood(Product product, Food alimento, Allergy alergiasUser) {
        Boolean getLeche = false, getGluten = false, checkLeche = false, checkGluten = false;
        if (alergiasUser.getGluten() == 1){
            checkGluten = true;
        }
        if (alergiasUser.getLeche() == 1){
            checkLeche = true;
        }
        if (product==null || alimento==null){
            infoGeneralNombre.setText(pendiente.getName());
            infoGeneralProducto.setText(pendiente.getProduct());
            //infoGeneralRating.setRating(alimento.getFoodHazard());
            infoGeneralCodigo.append(" "+pendiente.getBarcode());
            infoGeneralMarca.append(" "+pendiente.getBrand());
            infoGeneralNeto.append(" "+pendiente.getContent());
            if (pendiente.getAllergens() == null){
                infoGeneralAlergenos.append(" ");
            }
            else{
                infoGeneralAlergenos.append(" "+pendiente.getAllergens());
                if (checkGluten){
                    if (pendiente.getAllergens().toLowerCase().contains("gluten")){
                        infoGeneralAlergenos.setTextColor(Color.rgb(179,040,033));
                        getGluten = true;
                    }
                }
                if (checkLeche){
                    if (pendiente.getAllergens().toLowerCase().contains("leche") || pendiente.getAllergens().toLowerCase().contains("lactosa")){
                        infoGeneralAlergenos.setTextColor(Color.rgb(179,040,033));
                        getLeche = true;
                    }
                }
            }
            if (pendiente.getTraces() == null){
                infoGeneralTrazas.append(" ");
            }
            else{
                infoGeneralTrazas.append(" "+pendiente.getTraces());
                if (checkGluten){
                    if (pendiente.getTraces().toLowerCase().contains("gluten")){
                        infoGeneralTrazas.setTextColor(Color.rgb(179,040,033));
                        getGluten = true;
                    }
                }
                if (checkLeche){
                    if (pendiente.getTraces().toLowerCase().contains("leche") || pendiente.getTraces().toLowerCase().contains("lactosa")){
                        infoGeneralTrazas.setTextColor(Color.rgb(179,040,033));
                        getLeche = true;
                    }
                }
            }
            //SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Date d = new Date(pendiente.getDate()*1000);
            infoGeneralFecha.setText("Fecha Solicitud: "+pendiente.getDate());
        }else{
            Picasso.with(this)
                    .load(product.getImage_front_url())
                    .into(ivFoodPhoto);

            infoGeneralNombre.setText(product.getProduct_name());
            infoGeneralProducto.setText(product.getCategories());
            //infoGeneralRating.setRating(alimento.getFoodHazard());
            infoGeneralCodigo.append(" "+product.getCodigo());
            infoGeneralMarca.append(" "+product.getBrands());
            infoGeneralNeto.append(" "+product.getQuantity());

            if (product.getAllergens() == null){
                infoGeneralAlergenos.append(" ");
            }
            else{
                infoGeneralAlergenos.append(" "+product.getAllergens());
                if (checkGluten){
                    if (product.getAllergens().toLowerCase().contains("gluten")){
                        infoGeneralAlergenos.setTextColor(Color.rgb(179,040,033));
                        getGluten = true;
                    }
                }
                if (checkGluten){
                    if (product.getAllergens().toLowerCase().contains("leche") || product.getAllergens().toLowerCase().contains("lactosa")){
                        infoGeneralAlergenos.setTextColor(Color.rgb(179,040,033));
                        getLeche = true;
                    }
                }
            }

            if (product.getTraces() == null){
                infoGeneralTrazas.append(" ");
            }
            else{
                infoGeneralTrazas.append(" "+product.getTraces());
                if (checkGluten){
                    if (product.getTraces().toLowerCase().contains("gluten")){
                        infoGeneralTrazas.setTextColor(Color.rgb(179,040,033));
                        getGluten = true;
                    }
                }
                if (checkLeche){
                    if (product.getTraces().toLowerCase().contains("leche") || product.getTraces().toLowerCase().contains("lactosa")){
                        infoGeneralTrazas.setTextColor(Color.rgb(179,040,033));
                        getLeche = true;
                    }
                }
            }
            if (product.getNutritionGrades() != null){
                switch(product.getNutritionGrades()) {
                    case "a":
                        ivNutricion.setColorFilter(Color.rgb(24, 106, 59));
                        ivNutricion.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_24dp);
                        break;
                    case "b":
                        ivNutricion.setColorFilter(Color.rgb( 46, 204, 113 ));
                        ivNutricion.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        break;
                    case "c":
                        ivNutricion.setColorFilter(Color.rgb( 247, 220, 111 ));
                        ivNutricion.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp);
                        break;
                    case "d":
                        ivNutricion.setColorFilter(Color.rgb(235, 152, 78));
                        ivNutricion.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        break;
                    case "e":
                        ivNutricion.setColorFilter(Color.rgb(231, 76, 60));
                        ivNutricion.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
                        break;
                    default:
                        // code block
                }
            }
            if (product.getNova() != null){
                switch(product.getNova()) {
                    case "1":
                        ivNova.setColorFilter(Color.rgb(24, 106, 59));
                        ivNova.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_24dp);
                        break;
                    case "2":
                        ivNova.setColorFilter(Color.rgb( 46, 204, 113 ));
                        ivNova.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        break;
                    case "3":
                        ivNova.setColorFilter(Color.rgb( 247, 220, 111 ));
                        ivNova.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        break;
                    case "4":
                        ivNova.setColorFilter(Color.rgb(235, 152, 78));
                        ivNova.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
                        break;
                    default:
                        // code block
                }
            }
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date(product.getLast_modified_t()*1000);
            infoGeneralFecha.append(" "+f.format(d));
            if (getLeche && getGluten && checkGluten && checkLeche){
                popupMessage("El alimento parece que contiene leche o lactosa o gluten");
            }
            else if (getLeche && checkLeche){
                popupMessage("El alimento parece que contiene leche o lactosa");
            }
            else if (getGluten && checkGluten){
                popupMessage("El alimento parece que contiene gluten");
            }
        }
    }

    private void popupMessage(String message){
        new AlertDialog.Builder(this)
                .setIcon(null)
                .setTitle("Alerta")
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                })
                .show();
    }

    //Muestra la información nutricional del alimento
    private void showNutritionFacts(Product product) {
        if (product == null){
            porcion.append(pendiente.getPortion());
            setTextNutrition(pendiente.getEnergy(), "", energia100, energiaPorcion);
            setTextNutrition(pendiente.getProtein(), "", proteinas100, proteinasPorcion);
            setTextNutrition(pendiente.getTotalFat(), "", grasaTotal100, grasaTotalPorcion);
            setTextNutrition(pendiente.getSaturatedFat(), "", grasaSaturada100, grasaSaturadaPorcion);
            setTextNutrition(pendiente.getMonoFat(), "", grasaMono100, grasaMonoPorcion);
            setTextNutrition(pendiente.getPoliFat(), "", grasaPoli100, grasaPoliPorcion);
            setTextNutrition(pendiente.getTransFat(), "", grasaTrans100, grasaTransPorcion);
            setTextNutrition(pendiente.getCholesterol(), "", colesterol100, colesterolPorcion);
            setTextNutrition(pendiente.getCarbo(), "", hidratos100, hidratosPorcion);
            setTextNutrition(pendiente.getTotalSugar(), "", azucares100, azucaresPorcion);
            setTextNutrition(pendiente.getFyber(), "", fibra100, fibraPorcion);
            setTextNutrition(pendiente.getSodium(), "", sodio100, sodioPorcion);
        }else{
            Nutriments nutriments = product.getNutriments();
            String portion = product.getServing_size();
            porcion.append(" "+portion);
            porcionEnvase.append(" " + Float.toString(calculatePortions(product.getQuantity(), portion)));
            setTextNutrition(nutriments.getEnergy_100g(), nutriments.getEnergy_serving(), energia100, energiaPorcion);
            setTextNutrition2(nutriments.getProteins_100g(), nutriments.getProteins_serving(), proteinas100, proteinasPorcion);
            setTextNutrition2(nutriments.getFat_100g(), nutriments.getFat_serving(), grasaTotal100, grasaTotalPorcion);
            setTextNutrition2(nutriments.getSaturatedFat100g(), nutriments.getSaturatedFatServing(), grasaSaturada100, grasaSaturadaPorcion);
            setTextNutrition2(nutriments.getMonounsaturated_fat_100g(), nutriments.getMonounsaturated_fat_serving(), grasaMono100, grasaMonoPorcion);
            setTextNutrition2(nutriments.getPolyunsaturated_fat_100g(), nutriments.getPolyunsaturated_fat_serving(), grasaPoli100, grasaPoliPorcion);
            setTextNutrition2(nutriments.getTrans_fat_100g(), nutriments.getTrans_fat_serving(), grasaTrans100, grasaTransPorcion);
            setTextNutrition2(nutriments.getCholesterol_100g(), nutriments.getCholesterol_serving(), colesterol100, colesterolPorcion);
            setTextNutrition2(nutriments.getCarbohydrates_100g(), nutriments.getCarbohydrates_serving(), hidratos100, hidratosPorcion);
            setTextNutrition2(nutriments.getSugars_100g(), nutriments.getSugars_serving(), azucares100, azucaresPorcion);
            setTextNutrition2(nutriments.getFiber_100g(), nutriments.getFiber_serving(), fibra100, fibraPorcion);
            setTextNutrition2(nutriments.getSodium_100g(), nutriments.getSodium_serving(), sodio100, sodioPorcion);
        }
    }

    private void setTextNutrition2(float content100, float portion, TextView tv100, TextView tvPortion) {
        DecimalFormat formato1 = new DecimalFormat("0.000");
        if (Float.toString(content100).length()>4){
            tv100.setText(formato1.format(content100));
        }else{
            tv100.setText(Float.toString(content100));
        }

        if (Float.toString(portion).length()>4){
            tvPortion.setText(formato1.format(portion));
        }else{
            tvPortion.setText(Float.toString(portion));
        }
    }

    //Calcula la cantidad de porciones por envase
    private float calculatePortions(String quantity, String portion) {
        Log.d("myTag","Cantidad: "+quantity+" Porcion: "+portion);
        float portions = 0;
        if (portion!=null){
            quantity = quantity.replace(",",".");
            String[] cantidad =  quantity.split(" ");
            List<String> porcion = separaPorcion(portion);
            int porc = Integer.parseInt(porcion.get(0));
            float cant;
            if (cantidad.length==2){
                cant = Float.parseFloat(cantidad[0]);
            }else{
                List<String> cantidad2 = separaPorcion(quantity);
                cant = Float.parseFloat(cantidad2.get(0));
            }
            if(cant<porc){
                portions = (cant*1000)/porc;
            }else{
                portions = cant/porc;
            }
        }
        return portions;
    }

    //Metodo para separar el numero de la unidad de medida de la porcion
    private List<String> separaPorcion(String portion) {
        List<String> chunks = new LinkedList<String>();
        Pattern VALID_PATTERN = Pattern.compile("[0-9]+|[a-z]+|[A-Z]+");
        Matcher matcher = VALID_PATTERN.matcher(portion);
        while (matcher.find()) {
            chunks.add( matcher.group());
        }
        return chunks;
    }

    public void setTextNutrition(String content100, String portion, TextView tv100, TextView tvPortion){
        tv100.setText(content100);
        tvPortion.setText(portion);
    }

    //Carga los ingredientes del alimento
    //token: Autorización
    //barcode: Código de barras del alimento
    public void loadIngredients() {
        if (product == null){
            tvIngredientes.setText(pendiente.getIngredients());
        }else{
            tvIngredientes.setText(product.getIngredients_text());
        }
    }

    //Carga las recomendaciones del alimento
    public void loadRecommendations(String barcode) {
        Call<List<Recommendation>> call = mEyesFoodApi.getRecommendations(barcode);
        call.enqueue(new Callback<List<Recommendation>>() {
            @Override
            public void onResponse(Call<List<Recommendation>> call,
                                   Response<List<Recommendation>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaRecomendaciones = response.body();
                showRecommendations(listaRecomendaciones);
            }

            @Override
            public void onFailure(Call<List<Recommendation>> call, Throwable t) {
            }
        });
    }

    public void showRecommendations(List<Recommendation> lista){
        if(lista.size()>0) {
            Intent intent = new Intent(this, RecommendationsActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("Recomendaciones",(Serializable) lista);
            intent.putExtra("BUNDLE",args);

            intent.putExtra("Alimento",Alimento);
            intent.putExtra("Product",product);
            intent.putExtra("MeGusta",MeGusta);
            startActivity(intent);
        }
        else{
            hacerToast(getResources().getString(R.string.dialog_no_recommendations));
        }
    }

    //Carga la lista de aditivos completa
    public void loadAdditivesFull(String barcode){
        //RapidApiConnect connect = new RapidApiConnect("default-application_3751123", "0709b89ebdmshda793b75aa8303fp17aa1djsn675c30a85042");
        /*HttpResponse<JsonNode> response = Unirest.get("https://vx-e-additives.p.rapidapi.com/additives/270?locale=en")
                .header("X-RapidAPI-Host", "vx-e-additives.p.rapidapi.com")
                .header("X-RapidAPI-Key", "0709b89ebdmshda793b75aa8303fp17aa1djsn675c30a85042")
                .asJson();*/
        //A futuro consultar api de aditivos
        //https://rapidapi.com/petarov/api/e-additives
        /*Call<List<Additive>> call = mEyesFoodApi.getFullAdditives(barcode);
        call.enqueue(new Callback<List<Additive>>() {
            @Override
            public void onResponse(Call<List<Additive>> call,
                                   Response<List<Additive>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaAditivosFull = response.body();
                showAdditives(listaAditivosFull);
            }

            @Override
            public void onFailure(Call<List<Additive>> call, Throwable t) {
                Log.d("Falla", "Falla en la llamada de aditivos: loadAdditives");
            }
        });*/
        Log.d("myTag", "Aditivos " + product.getAdditives_tags().toString());
        if(product.getAdditives_tags().size()>0) {
            Intent intent = new Intent(this, AdditivesActivity.class);
            Bundle args = new Bundle();
            intent.putExtra("BUNDLE",args);
            intent.putExtra("Alimento",Alimento);
            intent.putExtra("Product",product);
            intent.putExtra("MeGusta",MeGusta);
            startActivity(intent);
        }
        else{
            hacerToast(getResources().getString(R.string.dialog_no_additives));
        }
    }

    //Carga las recomendaciones del alimento
    public void loadImages(String barcode) {
        listaImagenes = new ArrayList<>();
        FoodImage nueva = new FoodImage(product.getImage_front_url());
        FoodImage nueva2 = new FoodImage(product.getImage_ingredients_url());
        FoodImage nueva3 = new FoodImage(product.getImage_nutrition_url());

        listaImagenes.add(nueva);
        listaImagenes.add(nueva2);
        listaImagenes.add(nueva3);

        showImages(listaImagenes);
    }

    public void showImages(ArrayList<FoodImage> lista){
        if(lista.size()>0) {
            Intent intent = new Intent(this, ImagesActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("Imagenes",lista);
            intent.putExtra("BUNDLE",args);

            intent.putExtra("Alimento",Alimento);
            intent.putExtra("Product",product);
            intent.putExtra("MeGusta",MeGusta);
            startActivity(intent);
        }
        else{
            hacerToast(getResources().getString(R.string.dialog_no_images));
        }
    }

    //Carga los comentarios del alimento
    public void loadComments(String barcode) {
        // idContexto = 1 -> alimento
        Call<List<Comment>> call = mCommentsApi.getComments(1, barcode);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaComentarios = response.body();
                showComments(listaComentarios);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }

    public void showComments(List<Comment> lista){
        Intent intent = new Intent(this, CommentsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("Comentarios",(Serializable) lista);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("Alimento",Alimento);
        intent.putExtra("Product",product);
        intent.putExtra("MeGusta",MeGusta);

        startActivity(intent);
    }

    //Carga el menú a la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Alimento!=null && product != null){
            getMenuInflater().inflate(R.menu.menu_foods, menu);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fabFoods: {
                loadComments(CodigoBarras);
                break;
            }
            case R.id.btFoodsStores: {
                loadStores(CodigoBarras);
                break;
            }
            case R.id.btFoodsEdits: {
                loadEdits(CodigoBarras);
                break;
            }
            case R.id.btFoodsAdditives: {
                loadAdditivesFull(CodigoBarras);
                break;
            }
            case R.id.btFoodsRecommendations: {
                loadRecommendations(CodigoBarras);
                break;
            }
            case R.id.btFoodsImages: {
                loadImages(CodigoBarras);
                break;
            }
            case R.id.fabEditFoods:{
                showSelectedDialog(0);
                break;
            }
            case R.id.fabPhotoFoods:{
                if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
                    showSelectedDialog(1);
                }
                else{
                    if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
                    }
                    if (ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
                    }
                    if (ContextCompat.checkSelfPermission(this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
                    }
                }
                break;
            }

            case R.id.btFoodsLike: {
                Log.d("myTag",String.valueOf(MeGusta));
                if(MeGusta == 2){
                    //Si es 2 cambio el color a normal al dislike, acento para like
                    dislike.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.button_not_pressed, null));
                    dislikesCount--;
                    dislike.setText(String.valueOf(dislikesCount));
                    //dislike.setBackgroundResource(android.R.drawable.btn_default);
                    like.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                    likesCount++;
                    like.setText(String.valueOf(likesCount));
                    MeGusta = 1;
                    //Hacer patch
                }
                else if(MeGusta == 1){
                    //Si es 1 cambio el color a normal al like
                    like.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.button_not_pressed, null));
                    likesCount--;
                    like.setText(String.valueOf(likesCount));
                    MeGusta=0;
                    //Hacer patch
                }
                else{
                    //Si es 0 cambio el color a acento a like
                    like.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                    likesCount++;
                    like.setText(String.valueOf(likesCount));
                    MeGusta = 1;
                }
                //Veo si está en el historial
                isFoodInHistory(userIdFinal, CodigoBarras);
                break;
            }
            case R.id.btFoodsDisLike: {
                Log.d("myTag",String.valueOf(MeGusta));
                if(MeGusta == 2){
                    dislike.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.button_not_pressed, null));
                    dislikesCount--;
                    dislike.setText(String.valueOf(dislikesCount));
                    MeGusta = 0;
                    //Hacer patch
                }
                else if(MeGusta == 1){
                    like.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.button_not_pressed, null));
                    likesCount--;
                    like.setText(String.valueOf(likesCount));
                    dislike.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                    dislikesCount++;
                    dislike.setText(String.valueOf(dislikesCount));
                    MeGusta = 2;
                    //Hacer patch
                }
                else{
                    //Si es 0 cambio el color a acento a like
                    dislike.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                    dislikesCount++;
                    dislike.setText(String.valueOf(dislikesCount));
                    MeGusta = 2;
                }
                //Veo si está en el historial
                isFoodInHistory(userIdFinal, CodigoBarras);
                break;
            }
        }
    }

    private void loadStores(final String codigoBarras) {
        Call<List<Store>> call = mEyesFoodApi.getStoresProduct(codigoBarras);
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaTiendas = response.body();
                showListStores(listaTiendas, codigoBarras);
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    private void showListStores(List<Store> listaTiendas, String codigoBarras) {
        if (listaTiendas.size()>0){
            FragmentManager fragmentManager = getSupportFragmentManager();
            StoresFragment storeFragment = new StoresFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            Bundle bundle = new Bundle();
            bundle.putInt("Menu", 2);
            bundle.putString("barcode", codigoBarras);
            storeFragment.setArguments(bundle);
            transaction.add(android.R.id.content, storeFragment, "fragmento_tiendas").addToBackStack(null);
            transaction.commit();
        }else{
            hacerToast("No existen tiendas con este alimento registrado");
        }
    }

    private void loadEdits(String codigoBarras) {
        Call<List<NewFoodBody>> call = mEyesFoodApi.getEdits(codigoBarras);
        call.enqueue(new Callback<List<NewFoodBody>>() {
            @Override
            public void onResponse(Call<List<NewFoodBody>> call, Response<List<NewFoodBody>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Error loadEdits" + response.message());
                    return;
                }
                listaEdits = response.body();
                showEdits(listaEdits);
            }

            @Override
            public void onFailure(Call<List<NewFoodBody>> call, Throwable t) {
                Log.d("myTag", "Error loadEdits" + t.getMessage());
                showEdits(null);
            }
        });
    }

    private void showEdits(List<NewFoodBody> listaEdits) {
        if (listaEdits!=null && listaEdits.size()>0){
            Intent i = new Intent(getApplicationContext(), UploadActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("Edits",(Serializable) listaEdits);
            i.putExtra("BUNDLE",args);
            i.putExtra("modo",3);
            startActivity(i);
        }else{
            hacerToast(getResources().getString(R.string.dialog_no_edits));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_foods_comments:{
                loadComments(CodigoBarras);
                break;
            }
            case R.id.action_foods_complaint:{
                showSelectedDialog(0);
                break;
            }
            case R.id.action_foods_add_photos:{
                if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
                    showSelectedDialog(1);
                }
                else{
                    if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
                    }
                    if (ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
                    }
                    if (ContextCompat.checkSelfPermission(this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
                    }
                }
                break;
            }
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int i = 0;
        int permisos = 0;
        for(String permission: permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                showDialogs(i);
            }else{
                if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                    permisos++;
                    if(permisos==3){
                        Toast.makeText(this, getResources().getString(R.string.success_permission_add_photos), Toast.LENGTH_LONG).show();
                    }
                } else{
                    showDialogs(i);
                }
            }
            i++;
        }
    }

    public void showDialogs(int seleccion){
        if(seleccion == 0 || seleccion == 1){
            new AlertDialog.Builder(this)
                    .setIcon(null)
                    .setTitle(getResources().getString(R.string.title_foods_permission_rationale_storage))
                    .setMessage(getResources().getString(R.string.message_foods_permission_rationale_storage))
                    .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }

                    })
                    .show();
        }
        else{
            new AlertDialog.Builder(this)
                    .setIcon(null)
                    .setTitle(getResources().getString(R.string.title_foods_permission_rationale_camera))
                    .setMessage(getResources().getString(R.string.message_foods_permission_rationale_camera))
                    .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }

                    })
                    .show();
        }

    }

    public void hacerToast(String contenido){
        Toast.makeText(this, contenido, Toast.LENGTH_LONG).show();
    }

    private void showSelectedDialog(int seleccion){
        Bundle bundle = new Bundle();
        bundle.putSerializable("Alimento", Alimento);
        bundle.putSerializable("Product", product);
        bundle.putSerializable("tipo", tipo);
        // set Fragmentclass Arguments

        FragmentManager fragmentManager = getSupportFragmentManager();
        ComplaintDialogFragment newFragmentComplaint = new ComplaintDialogFragment();
        UploadImageDialogFragment newFragmentUpload = new UploadImageDialogFragment();
        newFragmentComplaint.setArguments(bundle);
        newFragmentUpload.setArguments(bundle);


        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        //transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        transaction.add(android.R.id.content, newFragmentUpload).addToBackStack(null);
        transaction.add(android.R.id.content, newFragmentComplaint).addToBackStack(null);

        if(seleccion == 0){
            transaction.replace(android.R.id.content, newFragmentComplaint);
        }
        else{
            transaction.replace(android.R.id.content, newFragmentUpload);
        }
        transaction.commit();
    }

    //Actualiza los me gusta del alimento
    public void getLikesCount(String barcode, final Button likes){
        Call<Counter> call = mEyesFoodApi.getLikes(barcode);
        call.enqueue(new Callback<Counter>() {
            @Override
            public void onResponse(Call<Counter> call, Response<Counter> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en getLikes " + response.errorBody().toString());
                    return;
                }
                else {
                    likesCounter = response.body();
                    likesCount = likesCounter.getCount();
                    likes.setText(String.valueOf(likesCount));
                }
            }
            @Override
            public void onFailure(Call<Counter> call, Throwable t) {
                Log.d("myTag", "Fallo en getLikes "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    //Actualiza los me gusta del alimento
    public void getDisLikesCount(String barcode, final Button dislikes){

        Call<Counter> call = mEyesFoodApi.getDislikes(barcode);
        call.enqueue(new Callback<Counter>() {
            @Override
            public void onResponse(Call<Counter> call, Response<Counter> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en getDisLikes " + response.errorBody().toString());
                    return;
                }
                else {
                    dislikesCounter = response.body();
                    dislikesCount = dislikesCounter.getCount();
                    dislikes.setText(String.valueOf(dislikesCount));
                }
            }
            @Override
            public void onFailure(Call<Counter> call, Throwable t) {
                Log.d("myTag", "Fallo en getDisLikes "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    //Actualiza los me gusta del alimento
    public void updateLikeHistory(String userId, final String barcode, int like){
        Call<ShortFood> call = mEyesFoodApi.modifyHistoryLike(userId, barcode, like);
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call, Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en updateLikeHistory " + response.errorBody().toString());
                    return;
                }
                else {
                    Log.d("myTag", "Éxito en updateLikeHistory");
                }
            }
            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                Log.d("myTag", "Fallo en updateLikeHistory "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    //Comprueba si el alimento consultado está en el historial del usuario
    public void isFoodInHistory(final String userId, final String barcode){
        Call<ShortFood> call = mEyesFoodApi.isInHistory(userId, barcode);
        call.enqueue(new Callback<ShortFood>() {
            @Override
            public void onResponse(Call<ShortFood> call,
                                   Response<ShortFood> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","NO EXITOSO");
                    return;
                }
                //El alimento está en el historial
                Log.d("myTag","ESTÁ");
                updateLikeHistory(userIdFinal, CodigoBarras, MeGusta);
            }

            @Override
            public void onFailure(Call<ShortFood> call, Throwable t) {
                //El alimento no está y lo inserto
                Log.d("myTag","NO ESTÁ");
                insertNoScan(userIdFinal, CodigoBarras);
            }
        });
    }

    private void insertNoScan(String userIdFinal, String codigoBarras) {
        Call<Food> call = mEyesFoodApi.insertNoScan(new InsertFromLikeBody(userIdFinal, codigoBarras, MeGusta));
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
}