package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;
import com.example.jonsmauricio.eyesfood.data.api.model.User;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsActivity extends AppCompatActivity {

    //Obtengo token e id de Usuario
    private String userIdFinal;
    private String userRolFinal;
    private String session;
    private String Referencia;
    User Usuario;

    private ListView listaComentarios;
    private EditText comentario;
    private ImageButton enviarComentario;
    private List<Comment> comentarios;
    private ImageView userAvatar;
    private TextView emptyState;
    Food Alimento;
    Food Product;
    Product product;
    Expert Experto;
    int MeGusta;
    int Contexto;
    private Comment Comentario;
    private ArrayAdapter<Comment> adaptadorComments;
    private List<Comment> listaRespuestas;

    Retrofit mRestAdapter;
    Retrofit mRestAdapter2;
    EyesFoodApi mEyesFoodApi;
    CommentsApi mCommentsApi;


    final String baseFotoUsuario = EyesFoodApi.BASE_URL + "img/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userIdFinal = SessionPrefs.get(this).getUserId();
        session = SessionPrefs.get(this).getUserSession();
        String userPhoto = SessionPrefs.get(this).getUserPhoto();

        listaComentarios = (ListView) findViewById(R.id.lvComments);
        comentario = (EditText) findViewById(R.id.etCommentsText);
        enviarComentario = (ImageButton) findViewById(R.id.btCommentsSend);
        userAvatar = (ImageView) findViewById(R.id.ivCommentsActivityAvatar);
        emptyState = (TextView) findViewById(R.id.tvCommentsEmptyState);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        //Cargo avatar de usuario actual
        if (session.equals("EyesFood")) {
            Picasso.with(this).load(baseFotoUsuario + userPhoto).resize(800, 800).into(userAvatar);
        } else {
            Picasso.with(this).load(userPhoto).resize(800, 800).into(userAvatar);
        }

        Bundle args = i.getBundleExtra("BUNDLE");
        comentarios = (List<Comment>) args.getSerializable("Comentarios");

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
                if (!response.isSuccessful()) {

                } else {
                    Usuario = response.body();
                    userRolFinal = Usuario.getRol();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        if (b != null) {
            Alimento = (Food) b.get("Alimento");
            Experto = (Expert) b.get("Experto");
            if (Alimento != null) {
                product = (Product) b.get("Product");
                MeGusta = (int) b.get("MeGusta");
                setTitle(Alimento.getFoodName());
                Referencia = Alimento.getBarCode();
                Contexto = 1;
            } else if (Experto != null) {
                setTitle(Experto.getName());
                Referencia = Integer.toString(Experto.getExpertId());
                Contexto = 2;
            }
        }

        listaComentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comment currentComment = adaptadorComments.getItem(i);
                if(currentComment.getColaborador().equals(userIdFinal)){
                    showDialog(Integer.parseInt(userIdFinal), currentComment);
                }
            }
        });

        enviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!comentario.getText().toString().equals("")) {
                    sendComment(Contexto, Referencia, comentario.getText().toString());
                    comentario.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                } else {
                    Toast.makeText(getApplicationContext(), "Parece que no has ingresado ningún comentario", Toast.LENGTH_LONG).show();
                }
            }

        });
        showListComments(comentarios);
    }

    private void showDialog(final int id, final Comment comment) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        //final EditText edittext = new EditText(getActivity());
        //edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Comentario");
        edittext.setText(comment.getComment());
        alert.setView(edittext);

        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newComment = edittext.getText().toString();
                editComment(comment, newComment);
            }
        });
        alert.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteComment(comment);
            }
        });
        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public void deleteComment(final Comment currentComment){
        Call<Comment> call = mCommentsApi.deleteComment(Integer.parseInt(currentComment.getId()));
        call.enqueue((new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(!response.isSuccessful()){
                }
                Toast.makeText(getApplicationContext(), "Se borro el comentario", Toast.LENGTH_LONG).show();
                loadComments(Referencia);

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        }));
    }

    public void editComment(final Comment currentComment, String newComment){
        CommentBody body = new CommentBody(currentComment.getColaborador(), currentComment.getIdColaborador(), newComment);
        Call<Comment> call = mCommentsApi.modifyComment(body, Integer.parseInt(currentComment.getId()));
        call.enqueue((new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag","Fallo en la API "+response.message());
                }
                Toast.makeText(getApplicationContext(), "Se edito el comentario", Toast.LENGTH_LONG).show();
                loadComments(Referencia);

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("Falla", t.getMessage());
            }
        }));
    }

    public void showListComments(List<Comment> lista){
        int tamanoLista = lista.size();
        if(tamanoLista == 0) {
            showEmptyState(true);
        }
        else{
            showEmptyState(false);
            // Inicializar el adaptador con la fuente de datos.
            adaptadorComments = new CommentsAdapter(this,
                    lista);

            //Relacionando la lista con el adaptador
            listaComentarios.setAdapter(adaptadorComments);

        }
    }

    public void sendComment(int contexto, String referencia, String comentario){
        Call<Comment> call = mCommentsApi.newComment(new CommentBody(userIdFinal, userRolFinal, comentario), contexto, referencia);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","Fallo en la API "+response.message());
                    return;
                }
                Log.d("myTag","Comentario enviado con exito");
                loadComments(Referencia);
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("Falla", t.getMessage());
            }
        });
    }

    //Carga los comentarios del alimento
    public void loadComments(String barcode) {
        Call<List<Comment>> call = mCommentsApi.getComments(Contexto, barcode);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","Fallo en la API "+response.message());
                    return;
                }
                comentarios = response.body();
                showListComments(comentarios);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void showEmptyState(boolean show){
        if(show){
            listaComentarios.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
        else{
            listaComentarios.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }


    //Carga el menú a la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_settings, menu);
        return true;
    }

    //Envía el alimento de vuelta a foods al presionar el botón back de la toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(Alimento != null){
                    Intent i = new Intent(this, FoodsActivity.class);
                    //Toast.makeText(getApplicationContext(), "alimento: "+Alimento.getBarCode(), Toast.LENGTH_LONG).show();
                    i.putExtra("Alimento", Alimento);
                    i.putExtra("Product",product);
                    i.putExtra("MeGusta", MeGusta);
                    startActivity(i);
                    return(true);
                }
                else if(Experto != null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    ExpertsFragment expertsFragment = new ExpertsFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, expertsFragment, "fragmento_expertos").addToBackStack(null);
                }
        }
        return(super.onOptionsItemSelected(item));
    }
}
