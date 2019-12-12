package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.data.api.model.*;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.example.jonsmauricio.eyesfood.data.api.*;
import com.example.jonsmauricio.eyesfood.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;

    public static final int GMAIL_RESULT_CODE = 777;

    Retrofit mRestAdapter;

    //Gmail y Facebook
    private SignInButton signInButtonGmail;
    //private LoginButton loginButtonFacebook;
    private CallbackManager callbackManager;
    private String session;

    // UI references.
    private ImageView ivLoginLogo;
    private EditText mUserIdView;
    private EditText mPasswordView;
    private TextInputLayout mFloatEmail;
    private TextInputLayout mFloatLabelPassword;
    private View mProgressView;
    private View mLoginFormView;
    private EyesFoodApi mEyesFoodApi;
    TextView mSignUp;
    TextView loginProgressText;

    private String nombre;
    private String apellido;
    private String correo;
    private Uri foto;
    private String fotoString;

    //Método que abre la pantalla principal
    private void showHistoryScreen() {
        startActivity(new Intent(this, HistoryActivity.class));
        showProgress(false);
        finish();
    }

    //Muestra el error en inicio de sesión
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        ivLoginLogo = (ImageView) findViewById(R.id.image_logo);
        mUserIdView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFloatEmail = (TextInputLayout) findViewById(R.id.float_label_user_id);
        mFloatLabelPassword = (TextInputLayout) findViewById(R.id.float_label_password);
        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mSignUp = (TextView) findViewById(R.id.link_signup);
        loginProgressText = (TextView) findViewById(R.id.login_progress_text);

        /*loginButtonFacebook = (LoginButton) findViewById(R.id.bt_login_facebook);
        loginButtonFacebook.setReadPermissions(Arrays.asList("email"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {*/
            /*@Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook","Success");
                session = "Facebook";
                requestEmail(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Facebook","Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook","Error");
            }
        });*/

        /*ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };*/

        signInButtonGmail = (SignInButton) findViewById(R.id.bt_login_gmail);
        signInButtonGmail.setSize(SignInButton.SIZE_WIDE);
        signInButtonGmail.setColorScheme(SignInButton.COLOR_DARK);

        //Setup
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (!isOnline()) {
                        showLoginError(getString(R.string.error_network));
                        return false;
                    }
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButtonGmail.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
    }

 /*   private void displayProfileInfo(Profile currentProfile) {
        nombre = currentProfile.getFirstName();
        apellido = currentProfile.getLastName();
        fotoString = currentProfile.getProfilePictureUri(100, 100).toString();

        Log.d("Facebook", "Profile Tracker: "+ nombre + " " + apellido + " " + fotoString);

    }*/

    private void requestEmail(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    correo = object.getString("email");
                    findUser(correo);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //Sustituye la acción realizada al pulsar el botón back
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(null)
                .setTitle("Salir")
                .setMessage("¿Está seguro que desea salir de la aplicación?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.email_sign_in_button:{
                if (!isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                session = "EyesFood";
                attemptLogin();
                break;
            }
            case R.id.link_signup:{
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            }
            case R.id.bt_login_gmail:{
                if (!isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                session = "Gmail";
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, GMAIL_RESULT_CODE);
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mFloatEmail.setError(null);
        mFloatLabelPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mUserIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_invalid_password));
            focusView = mFloatLabelPassword;
            cancel = true;
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(email)) {
            mFloatEmail.setError(getString(R.string.error_field_required));
            focusView = mFloatEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mFloatEmail.setError(getString(R.string.error_invalid_email));
            focusView = mFloatEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Mostrar el indicador de carga y luego iniciar la petición asíncrona.
            showProgress(true);
            Call<User> loginCall = mEyesFoodApi.login(new LoginBody(email, password));
            loginCall.enqueue(new Callback<User>() {
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
                        Log.d("LoginActivity", "Error:"+response.message());
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

                    // Guardar afiliado en preferencias
                    //body: Cuerpo deserializado de la petición exitosa
                    User usuario = response.body();
                    usuario.setSession(session);
                    SessionPrefs.get(LoginActivity.this).saveUser(usuario);

                    // Ir a la pantalla principal
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

    //Verifica si el email es correcto
    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Verifica si el password es correcto
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        loginProgressText.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        ivLoginLogo.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GMAIL_RESULT_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            // TODO: 06-11-2017 Tomar nombre, apellido y email
            GoogleSignInAccount account = result.getSignInAccount();
            //Display name: Nombre completo
            //ID es gigante con 21 números, entonces lo hago yo
            nombre = account.getGivenName();
            apellido = account.getFamilyName();
            correo = account.getEmail();
            foto = account.getPhotoUrl();
            fotoString = "default.png";
            if(foto != null) {
                fotoString = foto.toString();
            }

            //Consulta con el correo para ver si existe la cuenta
            findUser(correo);
        }
        else{
            Toast.makeText(this, R.string.login_gmail_failed, Toast.LENGTH_LONG).show();
        }
    }

    private void findUser(String email) {
        Call<User> call = mEyesFoodApi.findExternalUser(new GmailUserBody(email));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","no existe no es succesful");
                    Log.d("myTag",nombre + " " + apellido + " " + correo + " " + fotoString);
                    registerGmailUser(nombre, apellido, correo, fotoString);
                    return;
                }else{
                    Log.d("myTag","Existe");
                    Log.d("myTag",nombre + " " + apellido + " " + correo + " " + fotoString);
                    User user = response.body();
                    user.setEmail(correo);
                    user.setName(nombre);
                    user.setSurName(apellido);
                    user.setPhoto(fotoString);
                    user.setSession(session);
                    SessionPrefs.get(LoginActivity.this).saveUser(user);
                    // Ir a la pantalla principal
                    showHistoryScreen();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("myTag","On failure findGmailUser");
            }
        });
    }

    private void registerGmailUser(String nombre, String apellido, String correo, String fotoString) {
        Call<User> call = mEyesFoodApi.registerExternalUser(new GmailRegisterBody(nombre, apellido, correo, fotoString));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d("myTag","no succesful");
                    return;
                }
                User user = response.body();
                user.setSession(session);
                SessionPrefs.get(LoginActivity.this).saveUser(user);
                // Ir a la pantalla principal
                showHistoryScreen();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("myTag","failure" + t.getMessage());
            }
        });
    }
}

