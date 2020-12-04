package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewFoodsDialogFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    TextView tvCodigoBarras;

    EditText etNombre, etProducto, etMarca, etNeto, etPorcion, etPorcionUnit, etEnergia, etProteinas, etGrasaTotal,
            etGrasaSat, etGrasaMono, etGrasaPoli, etGrasaTrans, etColesterol, etHidratos, etAzucares, etFibra, etSodio,
            etIngredientes, etAlergenos, etTrazas;

    String Nombre, Producto, Marca, Porcion, PorcionUnit, Ingredientes, Neto, Energia, Proteinas, GrasaTotal, GrasaSat,
            GrasaMono, GrasaPoli, GrasaTrans, Colesterol, Hidratos, Azucares, Fibra, Sodio, Alergenos, Trazas;

    String Date = "";

    String barCode;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;

    //Obtengo token e id de Usuario
    private String userIdFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialog_new_foods, container, false);

        barCode = getArguments().getString("barCode");

        Toolbar toolbar = view.findViewById(R.id.toolbarNewFoods);
        toolbar.setTitle(getResources().getString(R.string.title_new_foods));

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
        tvCodigoBarras.append(" "+barCode);

        etNombre = getView().findViewById(R.id.etNewFoodsInfoGeneralTitulo);
        etProducto = getView().findViewById(R.id.etNewFoodsInfoGeneralProducto);
        etMarca = getView().findViewById(R.id.etNewFoodsInfoGeneralMarca);
        etNeto = getView().findViewById(R.id.etNewFoodsInfoGeneralNeto);

        etPorcion = getView().findViewById(R.id.etNewFoodsInfoNutricionalPorcion);
        etPorcionUnit = getView().findViewById(R.id.etNewFoodsInfoNutricionalPorcionUnit);
        etEnergia = getView().findViewById(R.id.etNewFoodsInfoNutricionalEnergia);
        etProteinas = getView().findViewById(R.id.etNewFoodsInfoNutricionalProteinas);
        etGrasaTotal = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaTotal);
        etGrasaSat = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaSat);
        etGrasaMono = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaMono);
        etGrasaPoli = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaPoli);
        etGrasaTrans = getView().findViewById(R.id.etNewFoodsInfoNutricionalGrasaTrans);
        etColesterol = getView().findViewById(R.id.etNewFoodsInfoNutricionalColesterol);
        etHidratos = getView().findViewById(R.id.etNewFoodsInfoNutricionalHidratos);
        etAzucares = getView().findViewById(R.id.etNewFoodsInfoNutricionalAzucares);
        etFibra = getView().findViewById(R.id.etNewFoodsInfoNutricionalFibra);
        etSodio = getView().findViewById(R.id.etNewFoodsInfoNutricionalSodio);

        etIngredientes = getView().findViewById(R.id.etNewFoodsIngredients);
        etAlergenos = getView().findViewById(R.id.etNewFoodsAllergens);
        etTrazas = getView().findViewById(R.id.etNewFoodsTraces);

        userIdFinal = SessionPrefs.get(getContext()).getUserId();

        getView().findViewById(R.id.tvNoInfo).setVisibility(View.GONE);
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
        menu.findItem(R.id.searchHistory).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
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
            PorcionUnit = etPorcionUnit.getText().toString();
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
                    PorcionUnit.equals("") && Energia.equals("") && Proteinas.equals("") && GrasaTotal.equals("") &&
                    GrasaSat.equals("") && GrasaMono.equals("") && GrasaPoli.equals("") && GrasaTrans.equals("") &&
                    Colesterol.equals("") && Hidratos.equals("") && Azucares.equals("") && Fibra.equals("") && Sodio.equals("")
                    && Ingredientes.equals("")  && Alergenos.equals("") && Trazas.equals("")){
                showEmptySolitudeDialog();
            }
            else{
                sendSolitude();
            }
            return true;
        } else if(id == android.R.id.home){
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendSolitude(){
        Call<Food> call = mEyesFoodApi.newFoodSolitude(new NewFoodBody(userIdFinal, barCode, Nombre, Producto, Marca,
                Neto, Porcion, PorcionUnit, Energia, Proteinas, GrasaTotal, GrasaSat, GrasaMono, GrasaPoli, GrasaTrans,
                Colesterol, Hidratos, Azucares, Fibra, Sodio, Ingredientes, Date, "2", Alergenos, Trazas));
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (!response.isSuccessful()) {
                    Log.d("Falla sendSolitude", "Falla interna en new food solitude");
                    return;
                }
                Log.d("Falla sendSolitude", "Exito");
                showSuccesDialog();
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude");
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

    public void showEmptySolitudeDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_failed_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_failed_solitude_new_foods))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), null)
                .show();
    }

}