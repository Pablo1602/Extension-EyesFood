package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.OpenFoodFactsApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Diary;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DiaryFragment extends DialogFragment {

    Retrofit mRestAdapter;
    Retrofit mRestAdapter2;
    EyesFoodApi mEyesFoodApi;
    CommentsApi mCommentsApi;
    String userIdFinal;

    private ListView resultDiary;
    private List<Diary> listaDiarios;
    private ArrayAdapter<Diary> adaptadorDiarios;
    private FloatingActionButton addDiary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarDiary);
        toolbar.setTitle(getResources().getString(R.string.nav_diary));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close_black_24dp);
        }

        setHasOptionsMenu(true);

        resultDiary = (ListView) view.findViewById(R.id.lvDiary);
        addDiary = (FloatingActionButton) view.findViewById(R.id.fabDiary);
        userIdFinal = SessionPrefs.get(getContext()).getUserId();

        // Crear conexión al servicio REST
        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        resultDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Diary currentDiary = adaptadorDiarios.getItem(i);
                showDialog(currentDiary);
            }
        });

        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        retrieveDiary();
        return view;
    }

    private void showDialogAdd() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final EditText edittext = new EditText(getContext());
        //final EditText edittext = new EditText(getActivity());
        //edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Nuevo Diario");
        edittext.setText("Nuevo titulo");
        alert.setView(edittext);

        alert.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newDiary = edittext.getText().toString();
                newDiary(newDiary);
            }
        });
        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void showDialog(final Diary diario) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final EditText edittext = new EditText(getContext());
        //final EditText edittext = new EditText(getActivity());
        //edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Diario");
        edittext.setText(diario.getTitulo());
        alert.setView(edittext);

        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newDiary = edittext.getText().toString();
                editDiary(diario.getId(), newDiary);
            }
        });
        alert.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteDiary(diario);
            }
        });
        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void retrieveDiary(){
        Call<List<Diary>> call = mCommentsApi.getDiary(userIdFinal);
        call.enqueue(new Callback<List<Diary>>() {
            @Override
            public void onResponse(Call<List<Diary>> call, Response<List<Diary>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Falla", "Falla en retrieveDiary "+ response.errorBody().toString());
                    return;
                }
                listaDiarios = response.body();
                adaptadorDiarios = new DiaryAdapter(getContext(), listaDiarios);
                resultDiary.setAdapter(adaptadorDiarios);
            }

            @Override
            public void onFailure(Call<List<Diary>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en retrieveDiary");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void newDiary(String titulo){
        Diary diario = new Diary("",titulo);
        Call<Diary> call = mCommentsApi.newDiary(diario, Integer.parseInt(userIdFinal));
        call.enqueue(new Callback<Diary>() {
            @Override
            public void onResponse(Call<Diary> call, Response<Diary> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "Sin exito en newDiary " + response.errorBody().toString());
                    return;
                }
                Toast.makeText(getContext(), "Se creo el diario", Toast.LENGTH_LONG).show();
                retrieveDiary();
            }

            @Override
            public void onFailure(Call<Diary> call, Throwable t) {
                Log.d("myTag", "Fallo en newDiary "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    public void editDiary(final String id, final String newTitle){
        Diary newDiary = new Diary(id, newTitle);
        Log.d("myTag", "$# id "+id+" newtitle "+newTitle+" ####");
        Call<Diary> call = mCommentsApi.modifyDiary(newDiary, Integer.parseInt(id));
        call.enqueue(new Callback<Diary>() {
            @Override
            public void onResponse(Call<Diary> call, Response<Diary> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "Sin exito en editDiary " + response.errorBody().toString());
                    return;
                }
                Toast.makeText(getContext(), "Se edito el diario", Toast.LENGTH_LONG).show();
                retrieveDiary();
            }

            @Override
            public void onFailure(Call<Diary> call, Throwable t) {
                Log.d("myTag", "Fallo en editDiary "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    public void deleteDiary(final Diary currentDiary){
        Call<Diary> call = mCommentsApi.deleteDiary(Integer.parseInt(currentDiary.getId()));
        call.enqueue((new Callback<Diary>() {
            @Override
            public void onResponse(Call<Diary> call, Response<Diary> response) {
                if(!response.isSuccessful()){
                }
                Toast.makeText(getContext(), "Se borro el diario", Toast.LENGTH_LONG).show();
                retrieveDiary();
            }

            @Override
            public void onFailure(Call<Diary> call, Throwable t) {
            }
        }));
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
            dismiss();
        }
        return true;
    }
}
