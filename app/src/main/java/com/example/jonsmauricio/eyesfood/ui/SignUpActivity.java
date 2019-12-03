package com.example.jonsmauricio.eyesfood.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.ApiError;
import com.example.jonsmauricio.eyesfood.data.api.model.SignUpBody;
import com.example.jonsmauricio.eyesfood.data.api.model.User;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit restAdapter;

    //Referencias UI
    private ImageView ivSignupLogo;
    private EditText etSignupName;
    private EditText etSignupSurname;
    private EditText etSignupEmail;
    private EditText etPlannedDate;
    private EditText etSignupPassword;
    private EditText etSignupPasswordConfirm;
    Button btSignupSignun;
    TextView tvSignupLogin;
    private View vwSignupProgress;
    private View vwSignupSignupForm;

    private EyesFoodApi eyesFoodApi;

    //Inicio sesión y voy a la pantalla principal
    private void showHistoryScreen() {
        startActivity(new Intent(this, HistoryActivity.class));
        showProgress(false);
        finish();
    }

    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    //Verifico si hay Internet
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    //TODO: Hacer verificaciones de los otros campos

    /**
     * Shows the progress UI and hides the login form.
     */
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(boolean show) {
        vwSignupProgress.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        ivSignupLogo.setVisibility(visibility);
        vwSignupSignupForm.setVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Crear conexión al servicio REST
        restAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        eyesFoodApi = restAdapter.create(EyesFoodApi.class);

        ivSignupLogo = (ImageView) findViewById(R.id.iv_signup_logo);
        etSignupName = (EditText) findViewById(R.id.et_signup_name);
        etSignupSurname = (EditText) findViewById(R.id.et_signup_surname);
        etSignupEmail = (EditText) findViewById(R.id.et_signup_email);
        etSignupEmail = (EditText) findViewById(R.id.et_signup_email);
        //etSignupDate = findViewById(R.id.et_signup_date);
        etPlannedDate = (EditText) findViewById(R.id.etPlannedDate);
        etSignupPassword = (EditText) findViewById(R.id.et_signup_password);
        etSignupPasswordConfirm = (EditText) findViewById(R.id.et_signup_password_confirm);
        btSignupSignun = (Button) findViewById(R.id.bt_signup_signup);
        tvSignupLogin = (TextView) findViewById(R.id.tv_signup_login);
        vwSignupProgress = findViewById(R.id.vw_signup_progress);
        vwSignupSignupForm = findViewById(R.id.vw_signup_signup_form);

        etPlannedDate.setOnClickListener(this);
        btSignupSignun.setOnClickListener(this);
        tvSignupLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_signup_signup:{
                if (!isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                attemptSignUp();
                break;
            }
            case R.id.tv_signup_login:{
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            }
            case R.id.etPlannedDate:
                showDatePickerDialog(etPlannedDate);
                break;
        }
    }

    //Metodo para mostrar calendario en eleccion de fecha de nacimiento
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
//        newFragment.show(this.getSupportFragmentManager(),"datePicker");

//        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private void attemptSignUp() {
        // Reset errors.
        etSignupName.setError(null);
        etSignupSurname.setError(null);
        etSignupEmail.setError(null);
        etPlannedDate.setError(null);
        etSignupPassword.setError(null);
        etSignupPasswordConfirm.setError(null);

        // Store values at the time of the login attempt.
        String name = etSignupName.getText().toString();
        String surname = etSignupSurname.getText().toString();
        String email = etSignupEmail.getText().toString();
        String date = etPlannedDate.getText().toString();
        String password = etSignupPassword.getText().toString();
        // TODO: 19-10-2017 Usar esto para confirmar el password
        String passwordConfirm = etSignupPasswordConfirm.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etSignupPassword.setError(getString(R.string.error_field_required));
            focusView = etSignupPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            etSignupPassword.setError(getString(R.string.error_invalid_password));
            focusView = etSignupPassword;
            cancel = true;
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(email)) {
            etSignupEmail.setError(getString(R.string.error_field_required));
            focusView = etSignupEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etSignupEmail.setError(getString(R.string.error_invalid_email));
            focusView = etSignupEmail;
            cancel = true;
        }

        //Verificar si la fecha de Nacimiento tiene contenido.
        if (TextUtils.isEmpty(date)) {
            etPlannedDate.setError(getString(R.string.error_field_required));
            focusView = etPlannedDate;
            cancel = true;
        }
        //TODO: Hacer verificaciones de los otros campos de texto

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Mostrar el indicador de carga y luego iniciar la petición asíncrona.
            showProgress(true);
            Call<User> signUpCall = eyesFoodApi.signUp(new SignUpBody(name, surname, email, password));
            signUpCall.enqueue(new Callback<User>() {
                @Override
                //Se ejecuta si hubo una respuesta HTTP normal
                public void onResponse(Call<User> call, Response<User> response) {
                    // Mostrar progreso
                    //showProgress(false);

                    // Procesar errores
                    //Es true si se obtienen códigos 200
                    if (!response.isSuccessful()) {
                        showProgress(false);
                        String error = "Ha ocurrido un error. Contacte al administrador";
                        //errorBody: El contenido plano de una respuesta con error
                        if (response.errorBody()
                                .contentType()
                                .subtype()
                                .equals("json")) {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                            error = apiError.getMessage();
                            Log.d("LoginActivity", apiError.getDeveloperMessage());
                        } else {
                            try {
                                // Reportar causas de error no relacionado con la API
                                Log.d("LoginActivity", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        showLoginError(error);
                        return;
                    }
                    User usuario = response.body();
                    usuario.setSession("EyesFood");
                    SessionPrefs.get(SignUpActivity.this).saveUser(usuario);

                    // Ir a la página principal
                    showHistoryScreen();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false);
                    showLoginError(t.getMessage());
                }
            });
        }
    }
}
