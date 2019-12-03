package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.InsertMeasureBody;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewMeasureFragment extends DialogFragment {

    Retrofit mRestAdapter;
    UserDataApi mUserDataApi;

    private String userIdFinal;

    private EditText newWeight, newFat, newWaist, newA1c, newGluPre, newGluPost, newPressure;
    private String Weight, Fat, Waist, A1c, GluPre, GluPost, Pressure;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // Inflate the layout to use as dialog or embedded fragment
    View view = inflater.inflate(R.layout.fragment_new_measure, container, false);

    Toolbar toolbar = view.findViewById(R.id.toolbarNewMeasure);
    toolbar.setTitle(getResources().getString(R.string.new_measure_title));

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
            .baseUrl(UserDataApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Crear conexión a la API de EyesFood
    mUserDataApi = mRestAdapter.create(UserDataApi.class);

    return view;
}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        newWeight = view.findViewById(R.id.etNewMeasureWeight);
        newFat = view.findViewById(R.id.etNewMeasureFat);
        newWaist = view.findViewById(R.id.etNewMeasureWaist);
        newA1c = view.findViewById(R.id.etNewMeasureA1C);
        newGluPre = view.findViewById(R.id.etNewMeasureGlucosaPre);
        newGluPost = view.findViewById(R.id.etNewMeasureGlucosaPost);
        newPressure = view.findViewById(R.id.etNewMeasurePresionArterial);

        userIdFinal = SessionPrefs.get(getContext()).getUserId();
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
            String selection = "";
            Weight = newWeight.getText().toString();
            Fat = newFat.getText().toString();
            Waist = newWaist.getText().toString();
            A1c = newA1c.getText().toString();
            GluPre = newGluPre.getText().toString();
            GluPost = newGluPost.getText().toString();
            Pressure = newPressure.getText().toString();

            if(Weight.equals("") && Fat.equals("") && Waist.equals("") && A1c.equals("") && GluPre.equals("") &&
                    GluPost.equals("") && Pressure.equals("")){
                showEmptySolitudeDialog();
            }
            else if(Weight.equals("0") || Fat.equals("0") || Waist.equals("0") || A1c.equals("0") || GluPre.equals("0") ||
                    GluPost.equals("0") || Pressure.equals("0")){
                showWrongSolitudeDialog();
            }
            else{
                if(!Weight.equals("") && !Weight.equals("0")){
                    selection = "weight";
                    sendMeasure(Weight, selection);
                }
                if(!Fat.equals("") && !Fat.equals("0")){
                    selection = "fat";
                    sendMeasure(Fat, selection);
                }
                if(!Waist.equals("") && !Waist.equals("0")){
                    selection = "waist";
                    sendMeasure(Waist, selection);
                }
                if(!A1c.equals("") && !A1c.equals("0")){
                    selection = "a1c";
                    sendMeasure(A1c, selection);
                }
                if(!GluPre.equals("") && !GluPre.equals("0")){
                    selection = "glupre";
                    sendMeasure(GluPre, selection);
                }
                if(!GluPost.equals("") && !GluPost.equals("0")){
                    selection = "glupost";
                    sendMeasure(GluPost, selection);
                }
                if(!Pressure.equals("") && !Pressure.equals("0")){
                    selection = "pressure";
                    sendMeasure(Pressure, selection);
                }
                showSuccesDialog();
                refreshProfile();
            }
            return true;

        } else if(id == android.R.id.home){
            refreshProfile();
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshProfile(){
        Bundle args = new Bundle();
        args.putString(ProfileFragment.ARG_SECTION_TITLE, "Mi perfil");

        Fragment fragmentProfile = ProfileFragment.newInstance("Mi perfil");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentProfile.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.llMainActivity, fragmentProfile, "fragmento_perfil")
                .commit();
    }

    private void sendMeasure(String measure, String selection) {
        Log.d("insert","Usuario: "+userIdFinal+ " Medida: "+measure);
        InsertMeasureBody body = new InsertMeasureBody(userIdFinal, measure);
        Call<Measure> call = null;
        switch(selection){
            case "weight":
                Log.d("insert","Usuario: "+body.getUserId()+ " Medida: "+body.getMeasure());
                call = mUserDataApi.insertWeight(body);
                break;
            case "fat":
                call = mUserDataApi.insertFat(body);
                break;
            case "waist":
                call = mUserDataApi.insertWaist(body);
                break;
            case "a1c":
                call = mUserDataApi.insertA1c(body);
                break;
            case "glupre":
                call = mUserDataApi.insertPreGlu(body);
                break;
            case "glupost":
                call = mUserDataApi.insertPostGlu(body);
                break;
            case "pressure":
                call = mUserDataApi.insertPressure(body);
                break;
        }

        call.enqueue(new Callback<Measure>() {
            @Override
            public void onResponse(Call<Measure> call,
                                   Response<Measure> response) {
                if (!response.isSuccessful()) {
                    Log.d("insert","no succesfull "+response.errorBody());
                    return;
                }
                Log.d("insert","succesfull");

            }

            @Override
            public void onFailure(Call<Measure> call, Throwable t) {
                Log.d("insert","failure "+t.getMessage());

            }
        });
    }

    public void showEmptySolitudeDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_failed_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_failed_solitude_new_foods))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), null)
                .show();
    }

    public void showWrongSolitudeDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_failed_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_wrong_solitude_new_measure))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), null)
                .show();
    }

    public void showSuccesDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_success_new_measures))
                .setMessage(getResources().getString(R.string.message_success_new_measures))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }

                })
                .show();
    }
}