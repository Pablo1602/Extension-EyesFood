package com.example.jonsmauricio.eyesfood.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.CommentsApi;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Diary;
import com.example.jonsmauricio.eyesfood.data.api.model.Entry;
import com.example.jonsmauricio.eyesfood.data.api.model.Expert;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class CalendarActivity extends AppCompatActivity {

    private Retrofit mRestAdapter;
    private Retrofit mRestAdapter2;
    private EyesFoodApi mEyesFoodApi;
    private CommentsApi mCommentsApi;
    private String userIdFinal, pdfname;
    private MCalendarView calendar;
    private Diary diario;
    private List<Entry> listaEntradas;
    private List<Expert> listaExpertos;
    private ArrayList<String> list;
    private ArrayAdapter<String > adapter;
    private String[] parts;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private int frag_open = 0;
    private Button export, share;
    String NOMBRE_DIRECTORIO = "Mis_Diarios";
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = findViewById(R.id.toolbarCalendario);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = findViewById(R.id.cvDiaryCalendar);
        export = findViewById(R.id.btExport);
        share = findViewById(R.id.btShare);

        // Crear conexión al servicio REST
        mRestAdapter2 = new Retrofit.Builder()
                .baseUrl(CommentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mCommentsApi = mRestAdapter2.create(CommentsApi.class);

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            diario = (Diary) b.getSerializable("Diario");
            setTitle(diario.getTitulo());
        }

        loadEntry(diario);
        retrieveExperts();
        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String fecha = date.getDayString()+"_"+date.getMonthString()+"_"+date.getYear();
                showEntry(fecha);
            }
        });
        /*calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i2+"_"+(i1+1)+"_"+i;
                showEntry(date);
            }
        });*/
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(false);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(true);
            }
        });
    }

    public void loadEntry(final Diary currentDiary) {
        Call<List<Entry>> call = mCommentsApi.getEntry(currentDiary.getId());
        call.enqueue(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call,
                                   Response<List<Entry>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaEntradas = response.body();
                paintCalendar();
            }
            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
            }
        });
    }

    private void paintCalendar(){
        String [] fecha;
        for(Entry entrada : listaEntradas){
            fecha = entrada.getFecha().split("_");
            calendar.markDate(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
        }
    }

    private void showDialog(Boolean correo){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final SearchView taskSearchview = new SearchView(this);
        final ListView searchList = new ListView(this);

        searchList.setVisibility(View.GONE);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
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

        // Permisos
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        if(!correo){
            alert.setTitle("Exportar Diario");
            alert.setMessage("Se exportara el diario "+diario.getTitulo()+" en formato PDF");

            alert.setPositiveButton("Exportar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    pdfCreator();
                    Toast.makeText(getApplicationContext(), "Se exporto el diario con exito", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            alert.setTitle("Compartir Diario");
            alert.setMessage("Indicar profesional de la salud");
            LinearLayout lay = new LinearLayout(this);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.addView(taskSearchview);
            lay.addView(searchList);
            alert.setView(lay);
            alert.setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    pdfCreator();
                    try{
                        if(taskSearchview.getQuery().toString().indexOf(" - ") > -1){
                            parts = taskSearchview.getQuery().toString().split(" - ");
                            sendmail(parts[1]);
                        }
                        else{
                            sendmail(taskSearchview.getQuery().toString());
                        }
                    }catch (Throwable t){
                        Toast.makeText(getApplicationContext(), "Error en compartir el calendario",Toast.LENGTH_LONG).show();
                    };
                }
            });
        }

        alert.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    private void pdfCreator(){
        Document documento = new Document();
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            pdfname = diario.getTitulo().replace(".","")+" "+formattedDate+".pdf";
            file = crearFichero(pdfname);
            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPDF);
            documento.open();
            documento.add(new Paragraph("Diario alimenticio: "+diario.getTitulo()+ "\n\n"));
            for (Entry e : listaEntradas) {
                documento.add(new Paragraph(e.getTitulo()+" en el día "+e.getFecha().replace("_","/")+"\n"));
                documento.add(new Paragraph(e.getTexto()+"\n\n"));
                documento.add(new Paragraph("Alimento de la entrada: "+e.getAlimento()+"\n\n"));
            }

        } catch(DocumentException e) {
        } catch(IOException e) {
        } finally {
            documento.close();
        }
    }

    public File crearFichero(String nombreFichero) {
        File ruta = getRuta();
        File fichero = null;
        if(ruta != null) {
            fichero = new File(ruta, nombreFichero);
        }
        return fichero;
    }

    public File getRuta() {
        File ruta = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);
            if(ruta != null) {
                if(!ruta.mkdirs()) {
                    if(!ruta.exists()) {
                        return null;
                    }
                }
            }
        }
        return ruta;
    }

    private void sendmail(String correo){
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            pdfCreator();
            Log.d("Email", "LA RUTA ES: " + getRuta() + "/" + pdfname);
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{correo});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Diario alimenticio");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getRuta() + "/" + pdfname));
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hola estimado usuario de EyesFood, " +
                    "quiero compartirte mi diario " + diario.getTitulo() + " contigo en formato PDF.");
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        }
        catch (Throwable t)
        {
            Toast.makeText(this, "Error en compartir el calendario",Toast.LENGTH_LONG).show();
        }
    }

    private void retrieveExperts() {
        Call<List<Expert>> call = mEyesFoodApi.getExperts();
        call.enqueue(new Callback<List<Expert>>() {
            @Override
            public void onResponse(Call<List<Expert>> call, Response<List<Expert>> response) {
                if (!response.isSuccessful()) {

                    return;
                }
                listaExpertos = response.body();
                createLisExpert(listaExpertos);
            }

            @Override
            public void onFailure(Call<List<Expert>> call, Throwable t) {
                Log.d("Falla Retrofit", "Falla en retrieveExperts");
                Log.d("Falla", t.getMessage());
            }
        });
    }

    private void createLisExpert (List<Expert> experts){
        list = new ArrayList<>();
        for(Expert expert: experts){
            list.add(expert.getName()+" "+expert.getLastName()+" - "+expert.getEmail());
        }
    }

    private void showEntry(String date){
        fragmentManager = getSupportFragmentManager();
        EntryFragment entryFragment = new EntryFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Bundle bundle = new Bundle();
        bundle.putString("idDiary", diario.getId());
        bundle.putString("fecha", date);
        bundle.putString("titulo", diario.getTitulo());
        entryFragment.setArguments(bundle);
        transaction.add(android.R.id.content, entryFragment, "fragmento_entradas").addToBackStack(null);
        transaction.commit();
        frag_open = 1;

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
                fragmentManager = getSupportFragmentManager();
                fragment = fragmentManager.findFragmentByTag("fragmento_entradas");
                //Si está el fragmento del perfil se elimina
                if(frag_open == 0){
                    finish();
                }
                if(fragment != null && frag_open == 1){
                    Log.d("fragmento","Está en entradas");
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    setTitle(diario.getTitulo());
                    frag_open = 0;
                    loadEntry(diario);
                }
        }
        return(super.onOptionsItemSelected(item));
    }

}
