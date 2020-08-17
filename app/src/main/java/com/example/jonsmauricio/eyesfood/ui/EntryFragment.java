package com.example.jonsmauricio.eyesfood.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Entry;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntryFragment extends DialogFragment {

    private ListView resultEntry;
    private List<Entry> listaEntradas;
    private List<ShortFood> listaFood;
    private ArrayList<String> list;
    private ArrayAdapter<String > adapter;
    private ArrayAdapter<Entry> adaptadorEntradas;
    private FloatingActionButton addEntry;
    private TextView emptyState;

    String date;
    String title;
    String idDiary;
    Retrofit mRestAdapter;
    Retrofit mRestAdapter2;
    EyesFoodApi mEyesFoodApi;
    CommentsApi mCommentsApi;
    String userIdFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarEntry);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.mipmap.ic_close_black_24dp);
        }

        setHasOptionsMenu(true);

        resultEntry = (ListView) view.findViewById(R.id.lvEntry);
        addEntry = (FloatingActionButton) view.findViewById(R.id.fabEntry);
        emptyState = (TextView) view.findViewById(R.id.tvCommentsEmptyState);
        userIdFinal = SessionPrefs.get(getContext()).getUserId();

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mEyesFoodApi= mRestAdapter.create(EyesFoodApi.class);

        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idDiary = bundle.getString("idDiary");
            date = bundle.getString("fecha");
            title = bundle.getString("titulo");
            toolbar.setTitle(title);
        }

        resultEntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Entry currentEntry = adaptadorEntradas.getItem(i);
                showDialogEdit(currentEntry);
            }
        });

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd(date);
            }
        });
        retrieveFood();
        retrieveEntry();
        return view;
    }
    @SuppressLint("WrongConstant")
    private void showDialogAdd(final String date) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final EditText edittext1 = new EditText(getContext());
        final EditText edittext2 = new EditText(getContext());
        final SearchView taskSearchview = new SearchView(getContext());
        final ListView searchList = new ListView(getContext());
        //final EditText edittext = new EditText(getActivity());
        //edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Nuevo Registro");
        edittext1.setText("Nuevo titulo");
        edittext2.setText("Nuevo texto");
        taskSearchview.setQueryHint("Buscar alimento");

        LinearLayout lay = new LinearLayout(getContext());
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(edittext1);
        lay.addView(edittext2);
        lay.addView(taskSearchview);
        lay.addView(searchList);
        alert.setView(lay);

        searchList.setVisibility(View.GONE);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        searchList.setAdapter(adapter);

        taskSearchview.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchList.setVisibility(View.VISIBLE);
            }
        });

        taskSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(list.contains(s)){
                    adapter.getFilter().filter(s);
                }
                searchList.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taskSearchview.setQuery((CharSequence) adapterView.getItemAtPosition(i), false);
                searchList.setVisibility(View.GONE);
            }
        });

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newTitle = edittext1.getText().toString();
                String newText = edittext2.getText().toString();
                String newFood = taskSearchview.getQuery().toString();
                newEntry(newTitle, newText, newFood, date);
            }
        });
        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void showDialogEdit(final Entry Entrada) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final EditText edittext1 = new EditText(getContext());
        final EditText edittext2 = new EditText(getContext());
        final SearchView taskSearchview = new SearchView(getContext());
        final ListView searchList = new ListView(getContext());
        //final EditText edittext = new EditText(getActivity());
        //edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        alert.setTitle("Editar Registro");
        edittext1.setText(Entrada.getTitulo());
        edittext2.setText(Entrada.getTexto());
        taskSearchview.setQuery(Entrada.getAlimento(),false);

        LinearLayout lay = new LinearLayout(getContext());
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(edittext1);
        lay.addView(edittext2);
        lay.addView(taskSearchview);
        lay.addView(searchList);
        alert.setView(lay);

        searchList.setVisibility(View.GONE);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        searchList.setAdapter(adapter);

        taskSearchview.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchList.setVisibility(View.VISIBLE);
            }
        });

        taskSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(list.contains(s)){
                    adapter.getFilter().filter(s);
                }
                searchList.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taskSearchview.setQuery((CharSequence) adapterView.getItemAtPosition(i), false);
                searchList.setVisibility(View.GONE);
            }
        });


        alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newTitle = edittext1.getText().toString();
                String newText = edittext2.getText().toString();
                String newFood = taskSearchview.getQuery().toString();
                editEntry(Entrada.getId(), newTitle, newText, newFood);
            }
        });
        alert.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteEntry(Entrada);
            }
        });
        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void retrieveFood(){
        Call<List<ShortFood>> call = mEyesFoodApi.getFoodsInHistory(userIdFinal);
        call.enqueue(new Callback<List<ShortFood>>() {
            @Override
            public void onResponse(Call<List<ShortFood>> call, Response<List<ShortFood>> response) {
                if(!response.isSuccessful()){
                    Log.d("Falla", "Falla en retrieveFood "+ response.errorBody().toString());
                    return;
                }
                listaFood = response.body();
                createListFood(listaFood);
            }

            @Override
            public void onFailure(Call<List<ShortFood>> call, Throwable t) {
                Log.d("Error Retrofit", "Error en retrieveFood");
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void createListFood (List<ShortFood> foods){
        list = new ArrayList<>();
        for(ShortFood food: foods){
            list.add(food.getName());
        }
    }

    private void retrieveEntry(){
        Call<List<Entry>> call = mCommentsApi.getEntryDate(idDiary, date);
        call.enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Falla", "Falla en retrieveEntry "+ response.errorBody().toString());
                    return;
                }
                Log.d("Entrada", "Correcto en entry");
                listaEntradas = response.body();
                adaptadorEntradas = new EntryAdapter(getContext(), listaEntradas);
                resultEntry.setAdapter(adaptadorEntradas);
                if(!listaEntradas.isEmpty()){
                    showEmptyState(false);
                }
                else{
                    showEmptyState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                Log.d("Error Retrofit", "Error en retrieveEntry");
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void newEntry(String titulo, String texto, String alimento, String fecha){
        Entry entrada = new Entry("",titulo,texto,alimento,fecha);
        Call<Entry> call = mCommentsApi.newEntry(entrada, Integer.parseInt(idDiary));
        call.enqueue(new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "Sin exito en newEntry " + response.errorBody().toString());
                    return;
                }
                Toast.makeText(getContext(), "Se creo la entrada", Toast.LENGTH_LONG).show();
                retrieveEntry();
            }

            @Override
            public void onFailure(Call<Entry> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en newEntry");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    public void editEntry(final String id, final String newTitle, final String newText, final String newFood){
        Entry newEntry = new Entry(id, newTitle, newText, newFood, date);
        Call<Entry> call = mCommentsApi.modifyEntry(newEntry, Integer.parseInt(id));
        call.enqueue(new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                if(!response.isSuccessful()){
                    Log.d("myTag", "Sin exito en editEntry " + response.errorBody().toString());
                    return;
                }
                Toast.makeText(getContext(), "Se edita la entrada", Toast.LENGTH_LONG).show();
                retrieveEntry();
            }
            @Override
            public void onFailure(Call<Entry> call, Throwable t) {
                Log.d("myTag", "Fallo en editEntry "+ t.getMessage() + " " + t.getLocalizedMessage());
                t.printStackTrace();
                return;
            }
        });
    }

    public void deleteEntry(final Entry currentEntry){
        Call<Entry> call = mCommentsApi.deleteEntry(Integer.parseInt(currentEntry.getId()));
        call.enqueue((new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                if(!response.isSuccessful()){
                }
                Toast.makeText(getContext(), "Se borro la entrada", Toast.LENGTH_LONG).show();
                retrieveEntry();
            }
            @Override
            public void onFailure(Call<Entry> call, Throwable t) {

            }
        }));
    }

    public void showEmptyState(boolean show){
        if(show){
            resultEntry.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
        else{
            resultEntry.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
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
            dismiss();
        }
        return true;
    }
}