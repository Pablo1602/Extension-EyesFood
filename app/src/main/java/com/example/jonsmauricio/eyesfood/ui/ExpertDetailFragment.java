package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Comment;
import com.example.jonsmauricio.eyesfood.data.api.model.Consult;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Rating;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpertDetailFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    final String baseFotoAlimento = EyesFoodApi.BASE_URL+"img/experts/";
    Expert Experto;
    private ImageView expertPhoto;
    private TextView expertName, expertSpecialty, expertDescription, expertQualify;
    private RatingBar expertRating;
    Button btFoods, btConsulta;
    private Retrofit mRestAdapter;
    private Retrofit mRestAdapter2;
    private EyesFoodApi mEyesFoodApi;
    private CommentsApi mCommentsApi;
    private String userIdFinal;
    private Rating valorRating;
    private List<Rating> valorLista;
    private List<Comment> listaComentarios;
    private FloatingActionButton comentar;
    float valor;
    float reputacion;
    int votos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_expert_detail, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarExpertDetail);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        setHasOptionsMenu(true);

        expertPhoto = view.findViewById(R.id.ivExpertDetail);
        expertName = view.findViewById(R.id.tvExpertDetailName);
        expertSpecialty = view.findViewById(R.id.tvExpertDetailSpecialty);
        expertRating = view.findViewById(R.id.rbExpertDetailRating);
        expertDescription = view.findViewById(R.id.tvExpertDetailDescription);
        //expertQualify = view.findViewById(R.id.tvExpertQualify);
        /*btFoods = view.findViewById(R.id.btFoods);*/
        btConsulta = view.findViewById(R.id.btConsulta);
        userIdFinal = SessionPrefs.get(getActivity()).getUserId();
        comentar = view.findViewById(R.id.fabComment);

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mCommentsApi = mRestAdapter2.create(CommentsApi.class);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Experto = (Expert) bundle.getSerializable("Experto");
            toolbar.setTitle(Experto.getName() + " " + Experto.getLastName());
            showExpertData(Experto);
        }


        /*btFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Experto.getFoods()>0){

                }else{
                    Toast.makeText(getActivity(), "El experto no ha aprobado alimentos", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "getExpertid():" + String.valueOf(Experto.getExpertId())+"#");
                loadComments(String.valueOf(Experto.getExpertId()));
            }
        });

        btConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(null)
                        .setTitle(getResources().getString(R.string.title_new_consult_question))
                        .setMessage(getResources().getString(R.string.message_new_consult_question))
                        .setPositiveButton(getResources().getString(R.string.possitive_dialog), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                crearConsulta();
                            }

                        })
                        .setNegativeButton(getResources().getString(R.string.negative_dialog), null)
                        .show();
            }
        });
        expertRating.setRating(0);
        isRating();
        expertRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                valor = v;
                isRating();
            }
        });

        return view;
    }

    private void isRating(){
        Call<Rating> call = mEyesFoodApi.isInRating(String.valueOf(Experto.getExpertId()), userIdFinal);
        call.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "Mala respuesta en evaluarRating" + response.toString());
                    return;
                }
                // Ya existe un rating
                else {
                    if(expertRating.getRating() == 0){
                        valorRating = response.body();
                        expertRating.setRating(valorRating.getValoracion());
                    }
                    else{
                        actualizarRating();
                    }
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {
                // No existe un rating
                if(expertRating.getRating() != 0){
                    crearRating();
                }
            }
        });

    }


    private void crearRating(){
        Call<Rating> call = mEyesFoodApi.newRating(new Rating(userIdFinal, String.valueOf(Experto.getExpertId()), Float.parseFloat(String.valueOf(valor))));
        call.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "Mala respuesta en newRating" + response.toString());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {

            }
        });
    }

    private void actualizarRating(){
        Call<Rating> call = mEyesFoodApi.modifyRating(String.valueOf(Experto.getExpertId()), userIdFinal, Float.parseFloat(String.valueOf(valor)));
        call.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en actualizarRating " + response.errorBody().toString());
                    return;
                }
                else {
                    Log.d("myTag", "Se actualizo la calificación");
                    //Toast.makeText(getActivity(), "Se califico al experto con exito", Toast.LENGTH_LONG).show();
                    calcularRating();
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {

            }
        });
    }

    private void calcularRating(){
        Call<List<Rating>> call = mEyesFoodApi.getRatingExpert(String.valueOf(Experto.getExpertId()));
        call.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "no Éxito en getRatingExpert " + response.errorBody().toString());
                    return;
                } else {
                    Log.d("myTag", "Éxito en getRatingExpert");

                    // Recorrer lista y calcular promedio en value
                    valorLista = response.body();
                    reputacion = 0;
                    votos = 0;
                    for(Rating rating: valorLista){
                        reputacion = reputacion + rating.getValoracion();
                        votos ++;
                    }
                    reputacion = reputacion/votos;
                    actualizarReputacion(reputacion);
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {

            }
        });
    }

    //Funcion para actualizar la reputacion de un experto despues de calificarlo
    private void actualizarReputacion(float experto_reputacion){
        Call<Expert> call = mEyesFoodApi.modifyRatingExpert(String.valueOf(Experto.getExpertId()), experto_reputacion);
        call.enqueue(new Callback<Expert>() {
            @Override
            public void onResponse(Call<Expert> call, Response<Expert> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "No hubo exito en actualizar la reputacion" + response.errorBody().toString());
                    return;
                }
                else {
                    Log.d("myTag", "Se actualizo la calificación del experto");
                }
            }

            @Override
            public void onFailure(Call<Expert> call, Throwable t) {
                Log.d("myTag", "Error al actualizar la calificación del experto");
            }
        });
    }

    private void crearConsulta() {
        Call<Consult> call = mEyesFoodApi.insertConsult(new Consult(String.valueOf(Experto.getExpertId()), userIdFinal));
        call.enqueue(new Callback<Consult>() {
            @Override
            public void onResponse(Call<Consult> call, Response<Consult> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag", "Mala respuesta en insertConsult" + response.toString());
                    return;
                }
                else {
//                    Log.d("myTag", "Mostrar Producto leido");
//                    progressDialog.setMessage("Cargando Producto");
                    Toast.makeText(getActivity(), "Su solicitud ha sido creada con exito", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Consult> call, Throwable t) {

            }
        });
    }

    private void showExpertData(Expert experto) {
        Picasso.with(getContext())
                .load(baseFotoAlimento + experto.getPhoto())
                .into(expertPhoto);

        expertName.setText(experto.getName() + " " + experto.getLastName());
        expertSpecialty.setText("Especialidad: " + experto.getSpecialty());
        expertRating.setRating(experto.getReputation());
        expertDescription.setText(experto.getDescription());
    }

    //Carga los comentarios del alimento
    public void loadComments(String referencia) {
        // idContexto = 2 -> experto
        Log.d("myTag", "referencia:" + String.valueOf(Experto.getExpertId())+"#");
        Call<List<Comment>> call = mCommentsApi.getComments(2, referencia);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaComentarios = response.body();
                showComments(listaComentarios);
                Toast.makeText(getActivity(), "Comentarios cargados", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }

    public void showComments(List<Comment> lista){
        Intent intent = new Intent(getActivity(), CommentsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("Comentarios",(Serializable) lista);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("Experto", Experto);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.searchHistory).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            ExpertsFragment expertsFragment = new ExpertsFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(android.R.id.content, expertsFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}