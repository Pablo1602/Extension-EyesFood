package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Comment;
import com.example.jonsmauricio.eyesfood.data.api.model.CommentBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.User;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ResponseActivity extends AppCompatActivity {

    //Obtengo token e id de Usuario
    private String userIdFinal;
    private String userRolFinal;
    private String session;
    private int idComentario;
    User Usuario;

    private ListView listaRespuestas;
    private EditText respuesta;
    private ImageButton enviarRespuesta;
    private List<Comment> respuestas;
    private ImageView userAvatar;
    private TextView emptyState;
    private ArrayAdapter<Comment> adaptadorComments;
    private Comment comentario;
    List<Comment> listaComentarios;

    Retrofit mRestAdapter;
    Retrofit mRestAdapter2;
    EyesFoodApi mEyesFoodApi;
    CommentsApi mCommentsApi;


    final String baseFotoUsuario = EyesFoodApi.BASE_URL+"img/users/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarResponse);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);;

        userIdFinal = SessionPrefs.get(this).getUserId();
        userRolFinal = SessionPrefs.get(this).getUserRol();
        session = SessionPrefs.get(this).getUserSession();
        String userPhoto = SessionPrefs.get(this).getUserPhoto();

        listaRespuestas = findViewById(R.id.lvComments);
        respuesta = findViewById(R.id.etCommentsText);
        enviarRespuesta = findViewById(R.id.btCommentsSend);
        userAvatar = findViewById(R.id.ivCommentsActivityAvatar);
        emptyState = findViewById(R.id.tvCommentsEmptyState);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        //Cargo avatar de usuario actual
        if(session.equals("EyesFood")) {
            Picasso.with(this).load(baseFotoUsuario + userPhoto).resize(800, 800).into(userAvatar);
        }
        else{
            Picasso.with(this).load(userPhoto).resize(800, 800).into(userAvatar);
        }

        Bundle args = i.getBundleExtra("BUNDLE");
        respuestas = (List<Comment>) args.getSerializable("Respuestas");
        showListResponses(respuestas);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        Call<User> call = mEyesFoodApi.getUser(userIdFinal);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){

                }
                else{
                    Usuario = response.body();
                    userRolFinal = Usuario.getRol();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        if(b != null){
            comentario = (Comment) b.get("Comentario");
            //toolbar.setTitle(comentario.getComment());
            idComentario = Integer.parseInt(comentario.getId());
            setTitle(comentario.getComment());
        }


        enviarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!respuesta.getText().toString().equals("")){
                    sendResponse(idComentario, respuesta.getText().toString());
                    respuesta.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, 0);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Parece que no has ingresado ningún comentario", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    public void showListResponses(List<Comment> lista){
        int tamanoLista = lista.size();
        if(tamanoLista == 0) {
            showEmptyState(true);
        }
        else{
            showEmptyState(false);
            // Inicializar el adaptador con la fuente de datos.
            adaptadorComments = new ResponsesAdapter(this, lista, 0);

            //Relacionando la lista con el adaptador
            listaRespuestas.setAdapter(adaptadorComments);
        }
    }

    public void sendResponse(final int idComentario, String comentario){
        Call<Comment> call = mCommentsApi.newResponse(new CommentBody(userIdFinal, userRolFinal, comentario), idComentario);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (!response.isSuccessful()) {

                    return;
                }
                Log.d("myTag","Respuesta publicada con exito");
                loadResponses(idComentario);

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en new food solitude con "+t.getMessage());
            }
        });
    }

    //Carga los comentarios del alimento
    public void loadResponses(int idComentario) {
        Call<List<Comment>> call = mCommentsApi.getResponses(idComentario);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestas = response.body();
                showListResponses(respuestas);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }

    public void showEmptyState(boolean show){
        if(show){
            listaRespuestas.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
        else{
            listaRespuestas.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    /*
    //Carga el menú a la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_settings, menu);
        return true;
    }*/

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
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }



    //Envía el alimento de vuelta a foods al presionar el botón back de la toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*Toast.makeText(getApplicationContext(), "#"+comentario.getReferencia()+"#", Toast.LENGTH_LONG).show();
                loadComments(comentario.getReferencia());
                loadFood(comentario.getReferencia());
                //Toast.makeText(getApplicationContext(), "alimento: "+alimento.getBarCode(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, CommentsActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("Comentarios",(Serializable) listaComentarios);
                i.putExtra("BUNDLE",args);
                i.putExtra("Comentario",comentario);
                i.putExtra("Alimento",alimento);
                startActivity(i);
                return(true);*/
                finish();
        }

        return(super.onOptionsItemSelected(item));
    }
}
