package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.ApiError;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Ingredient;
import com.example.jonsmauricio.eyesfood.data.api.model.LoginBody;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.api.model.User;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComplaintDialogFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    TextView tvCodigoBarras;

    EditText etNombre, etProducto, etMarca, etNeto, etPorcion, etEnergia, etProteinas, etGrasaTotal,
            etGrasaSat, etGrasaMono, etGrasaPoli, etGrasaTrans, etColesterol, etHidratos, etAzucares, etFibra, etSodio,
            etIngredientes, etAlergenos, etTrazas;

    TextInputLayout tilNombre, tilProducto, tilMarca, tilNeto, tilPorcion, tilEnergia, tilProteinas,
            tilGrasaTotal, tilGrasaSat, tilGrasaMono, tilGrasaPoli, tilGrasaTrans, tilColesterol, tilHidratos, tilAzucares,
            tilFibra, tilSodio;

    String Nombre, Producto, Marca, Porcion, Ingredientes, Neto, Energia, Proteinas, GrasaTotal, GrasaSat,
            GrasaMono, GrasaPoli, GrasaTrans, Colesterol, Hidratos, Azucares, Fibra, Sodio, Alergenos, Trazas;

    String Date = "";

    String barCode;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;

    //Obtengo token e id de Usuario
    private String userIdFinal;

    private Food Alimento;
    private Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialog_new_foods, container, false);

        Alimento = (Food) getArguments().getSerializable("Alimento");
        product = (Product) getArguments().getSerializable("Product");
        barCode = Alimento.getBarCode();

        Toolbar toolbar = view.findViewById(R.id.toolbarNewFoods);
        toolbar.setTitle(getResources().getString(R.string.title_complaint_foods));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close_black_24dp);
        }

        setHasOptionsMenu(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvCodigoBarras = getView().findViewById(R.id.tvNewFoodsInfoGeneralCodigo);
        tvCodigoBarras.append(" "+Alimento.getBarCode());

        etNombre = getView().findViewById(R.id.etNewFoodsInfoGeneralTitulo);
        tilNombre = getView().findViewById(R.id.tvNewFoodsInfoGeneralNombre);
        tilNombre.setHint(tilNombre.getHint() + " " + setFactsString(product.getProduct_name()));
        tilNombre.getEditText().setText(product.getProduct_name());
        etProducto = getView().findViewById(R.id.etNewFoodsInfoGeneralProducto);
        tilProducto = getView().findViewById(R.id.tvNewFoodsInfoGeneralProducto);
        tilProducto.setHint(tilProducto.getHint() + " " + setFactsString(product.getCategories()));
        tilProducto.getEditText().setText(product.getCategories());

        //tilProducto.setHint(tilProducto.getHint() + " " + setFactsString(Alimento.getProductId()));
        etMarca = getView().findViewById(R.id.etNewFoodsInfoGeneralMarca);
        tilMarca = getView().findViewById(R.id.tvNewFoodsInfoGeneralMarca);
        tilMarca.setHint(tilMarca.getHint() + " " + setFactsString(product.getBrands()));
        tilMarca.getEditText().setText(product.getBrands());
        //tilMarca.setHint(tilMarca.getHint() + " " + setFactsString(Alimento.getBrandCode()));
        etNeto = getView().findViewById(R.id.etNewFoodsInfoGeneralNeto);
        tilNeto = getView().findViewById(R.id.tvNewFoodsInfoGeneralNeto);
        tilNeto.setHint(tilNeto.getHint() + " " + setFactsString(product.getQuantity()));
        tilNeto.getEditText().setText(product.getQuantity());
        //tilNeto.setHint(tilNeto.getHint() + " " + setFactsInt(Alimento.getContent()) + setFactsString(Alimento.getUnit()));

        etPorcion = getView().findViewById(R.id.etNewFoodsInfoNutricionalPorcion);
        tilPorcion = getView().findViewById(R.id.tvNewFoodsInfoNutricionalPorcion);
        tilPorcion.setHint(tilPorcion.getHint() + " " + setFactsString(product.getServing_size()));
        tilPorcion.getEditText().setText(product.getServing_size());
        //tilPorcion.setHint(tilPorcion.getHint() + " " + setFactsString(Alimento.getPortion()));
        etEnergia = getView().findViewById(R.id.etNewFoodsInfoNutricionalEnergia);
        tilEnergia = getView().findViewById(R.id.tvNewFoodsInfoNutricionalEnergia);
        tilEnergia.setHint(tilEnergia.getHint() + " " + setFactsString(product.getNutriments().getEnergy_100g()));
        tilEnergia.getEditText().setText(product.getNutriments().getEnergy_100g());
        //tilEnergia.setHint(tilEnergia.getHint() + " " + setFactsFloat(Alimento.getEnergy()));
        etProteinas = getView().findViewById(R.id.etNewFoodsInfoNutricionalProteinas);
        tilProteinas = getView().findViewById(R.id.tvNewFoodsInfoNutricionalProteinas);
        tilProteinas.setHint(tilProteinas.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getProteins_100g())));
        tilProteinas.getEditText().setText(Float.toString(product.getNutriments().getProteins_100g()));
        //tilProteinas.setHint(tilProteinas.getHint() + " " + setFactsFloat(Alimento.getProtein()));
        etGrasaTotal = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaTotal);
        tilGrasaTotal = getView().findViewById(R.id.tvNewFoodsInfoNutricionalGrasaTotal);
        tilGrasaTotal.setHint(tilGrasaTotal.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getFat_100g())));
        tilGrasaTotal.getEditText().setText(Float.toString(product.getNutriments().getFat_100g()));
        //tilGrasaTotal.setHint(tilGrasaTotal.getHint() + " " + setFactsFloat(Alimento.getTotalFat()));
        etGrasaSat = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaSat);
        tilGrasaSat = getView().findViewById(R.id.tvNewFoodsInfoNutricionalGrasaSat);
        tilGrasaSat.setHint(tilGrasaSat.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getSaturatedFat100g())));
        tilGrasaSat.getEditText().setText(Float.toString(product.getNutriments().getSaturatedFat100g()));
        //tilGrasaSat.setHint(tilGrasaSat.getHint() + " " + setFactsFloat(Alimento.getSaturatedFat()));
        etGrasaMono = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaMono);
        tilGrasaMono= getView().findViewById(R.id.tvNewFoodsInfoNutricionalGrasaMono);
        tilGrasaMono.setHint(tilGrasaMono.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getMonounsaturated_fat_100g())));
        tilGrasaMono.getEditText().setText(Float.toString(product.getNutriments().getMonounsaturated_fat_100g()));
        //tilGrasaMono.setHint(tilGrasaMono.getHint() + " " + setFactsFloat(Alimento.getMonoFat()));
        etGrasaPoli = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaPoli);
        tilGrasaPoli = getView().findViewById(R.id.tvNewFoodsInfoNutricionalGrasaPoli);
        tilGrasaPoli.setHint(tilGrasaPoli.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getPolyunsaturated_fat_100g())));
        tilGrasaPoli.getEditText().setText(Float.toString(product.getNutriments().getPolyunsaturated_fat_100g()));
        //tilGrasaPoli.setHint(tilGrasaPoli.getHint() + " " + setFactsFloat(Alimento.getPoliFat()));
        etGrasaTrans = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaTrans);
        tilGrasaTrans = getView().findViewById(R.id.tvNewFoodsInfoNutricionalGrasaTrans);
        tilGrasaTrans.setHint(tilGrasaTrans.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getTrans_fat_100g())));
        tilGrasaTrans.getEditText().setText(Float.toString(product.getNutriments().getTrans_fat_100g()));
        //tilGrasaTrans.setHint(tilGrasaTrans.getHint() + " " + setFactsFloat(Alimento.getTransFat()));
        etColesterol = getView().findViewById(R.id.etNewFoodsInfoNutricionalColesterol);
        tilColesterol = getView().findViewById(R.id.tvNewFoodsInfoNutricionalColesterol);
        tilColesterol.setHint(tilColesterol.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getCholesterol_100g())));
        tilColesterol.getEditText().setText(Float.toString(product.getNutriments().getCholesterol_100g()));
        //tilColesterol.setHint(tilColesterol.getHint() + " " + setFactsFloat(Alimento.getCholesterol()));
        etHidratos = getView().findViewById(R.id.etNewFoodsInfoNutricionalHidratos);
        tilHidratos = getView().findViewById(R.id.tvNewFoodsInfoNutricionalHidratos);
        tilHidratos.setHint(tilHidratos.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getCarbohydrates_100g())));
        tilHidratos.getEditText().setText(Float.toString(product.getNutriments().getCarbohydrates_100g()));
        //tilHidratos.setHint(tilHidratos.getHint() + " " + setFactsFloat(Alimento.getCarbo()));
        etAzucares = getView().findViewById(R.id.etNewFoodsInfoNutricionalAzucares);
        tilAzucares = getView().findViewById(R.id.tvNewFoodsInfoNutricionalAzucares);
        tilAzucares.setHint(tilAzucares.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getSugars_100g())));
        tilAzucares.getEditText().setText(Float.toString(product.getNutriments().getSugars_100g()));
        //tilAzucares.setHint(tilAzucares.getHint() + " " + setFactsFloat(Alimento.getTotalSugar()));
        etFibra = getView().findViewById(R.id.etNewFoodsInfoNutricionalFibra);
        tilFibra = getView().findViewById(R.id.tvNewFoodsInfoNutricionalFibra);
        tilFibra.setHint(tilFibra.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getFiber_100g())));
        tilFibra.getEditText().setText(Float.toString(product.getNutriments().getFiber_100g()));
        //tilFibra.setHint(tilFibra.getHint() + " " + setFactsFloat(Alimento.getFiber()));
        etSodio = getView().findViewById(R.id.etNewFoodsInfoNutricionalSodio);
        tilSodio = getView().findViewById(R.id.tvNewFoodsInfoNutricionalSodio);
        tilSodio.setHint(tilGrasaTotal.getHint() + " " + setFactsString(Float.toString(product.getNutriments().getSodium_100g())));
        tilSodio.getEditText().setText(Float.toString(product.getNutriments().getSodium_100g()));
        //tilSodio.setHint(tilSodio.getHint() + " " + setFactsFloat(Alimento.getSodium()));

        etIngredientes = getView().findViewById(R.id.etNewFoodsIngredients);
        etAlergenos = getView().findViewById(R.id.etNewFoodsAllergens);
        etTrazas = getView().findViewById(R.id.etNewFoodsTraces);
        etIngredientes.setText(product.getIngredients_text());
        etAlergenos.setText(product.getAllergens());
        etTrazas.setText(product.getTraces());
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
    }

    //Métodos que setean * al contenido no informado
    public String setFactsFloat(float contenido){
        if(contenido < 0){
            return "*";
        }
        return String.valueOf(contenido);
    }

    public String setFactsInt(int contenido){
        if(contenido < 0){
            return "*";
        }
        return String.valueOf(contenido);
    }

    public String setFactsString(String contenido){
        if(contenido == null){
            return "*";
        }
        if(contenido.equals("-1")){
            return "*";
        }
        return contenido;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_foods, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_foods_complaint).setVisible(false);
        menu.findItem(R.id.action_foods_add_photos).setVisible(false);
        menu.findItem(R.id.action_foods_settings).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sendFood) {
            Nombre = etNombre.getText().toString();
            Producto = etProducto.getText().toString();
            Marca = etMarca.getText().toString();
            Neto = etNeto.getText().toString();

            Porcion = etPorcion.getText().toString();
            Energia = etEnergia.getText().toString();
            Proteinas = etProteinas.getText().toString();
            GrasaTotal = etGrasaTotal.getText().toString();
            GrasaSat = etGrasaSat.getText().toString();
            GrasaMono = etGrasaMono.getText().toString();
            GrasaPoli = etGrasaPoli.getText().toString();
            GrasaTrans = etGrasaTrans.getText().toString();
            Colesterol = etColesterol.getText().toString();
            Hidratos = etHidratos.getText().toString();
            Azucares = etAzucares.getText().toString();
            Fibra = etFibra.getText().toString();
            Sodio = etSodio.getText().toString();

            Ingredientes = etIngredientes.getText().toString();
            Alergenos = etAlergenos.getText().toString();
            Trazas = etTrazas.getText().toString();

            //Esta comprobación es la adecuada
            if(Nombre.equals("") && Producto.equals("") && Marca.equals("") && Neto.equals("") && Porcion.equals("") &&
                    Energia.equals("") && Proteinas.equals("") && GrasaTotal.equals("") &&
                    GrasaSat.equals("") && GrasaMono.equals("") && GrasaPoli.equals("") && GrasaTrans.equals("") &&
                    Colesterol.equals("") && Hidratos.equals("") && Azucares.equals("") && Fibra.equals("") && Sodio.equals("")
                    && Ingredientes.equals("") && Alergenos.equals("") && Trazas.equals("")){
                showEmptyComplaintDialog();
            }
            else{
                sendComplaint();
            }
            return true;
        } else if(id == android.R.id.home){
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendComplaint(){
        Call<Food> call = mEyesFoodApi.newFoodComplaint(new NewFoodBody(userIdFinal, barCode, Nombre, Producto, Marca,
                Neto, Porcion, "", Energia, Proteinas, GrasaTotal, GrasaSat, GrasaMono, GrasaPoli, GrasaTrans,
                Colesterol, Hidratos, Azucares, Fibra, Sodio, Ingredientes, Date, "2", Alergenos, Trazas));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","falla");
                    return;
                }
                Log.d("myTag","entro");
                showSuccesDialog();
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food complaint");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void showSuccesDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_success_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_success_solitude_new_foods))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }

                })
                .show();
    }

    public void showEmptyComplaintDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_failed_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_failed_solitude_new_foods))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), null)
                .show();
    }
}