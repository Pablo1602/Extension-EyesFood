package com.example.jonsmauricio.eyesfood.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.jonsmauricio.eyesfood.data.api.model.User;

/*
    Clase utilizada para guardar las preferencias del usuario y mantener persistencia
*/
public class SessionPrefs {

    private static final String PREFS_NAME = "EYESFOOD_PREFS";
    private static final String PREF_USER_ID = "PREF_USER_ID";
    private static final String PREF_USER_NAME = "PREF_USER_NAME";
    private static final String PREF_USER_SURNAME = "PREF_USER_SURNAME";
    private static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    private static final String PREF_USER_PHOTO = "PREF_USER_PHOTO";
    private static final String PREF_USER_REPUTATION = "PREF_USER_REPUTATION";
    private static final String PREF_USER_DATE_BIRTH = "PREF_USER_DATE_BIRTH";
    private static final String PREF_USER_GENDER = "PREF_USER_GENDER";
    private static final String PREF_USER_HEIGHT = "PREF_USER_HEIGHT";
    private static final String PREF_USER_COUNTRY = "PREF_USER_COUNTRY";
    private static final String PREF_USER_ROL = "PREF_USER_ROL";
    //normal, facebook o gmail
    private static final String PREF_USER_SESSION = "PREF_USER_SESSION";

    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_USER_ID, null));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void saveUser(User user) {
        if (user != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_USER_ID, user.getId());
            editor.putString(PREF_USER_ROL, user.getRol());
            editor.putString(PREF_USER_NAME, user.getName());
            editor.putString(PREF_USER_SURNAME, user.getSurName());
            editor.putString(PREF_USER_EMAIL, user.getEmail());
            editor.putString(PREF_USER_PHOTO, user.getPhoto());
            editor.putString(PREF_USER_REPUTATION, user.getReputation());
            editor.putString(PREF_USER_DATE_BIRTH, user.getDateBirth());
            editor.putString(PREF_USER_GENDER, user.getGender());
            editor.putString(PREF_USER_HEIGHT, user.getHeight());
            editor.putString(PREF_USER_COUNTRY, user.getCountry());
            editor.putString(PREF_USER_SESSION, user.getSession());
            editor.apply();

            mIsLoggedIn = true;
        }
    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USER_ID, null);
        editor.putString(PREF_USER_ROL, null);
        editor.putString(PREF_USER_NAME, null);
        editor.putString(PREF_USER_SURNAME, null);
        editor.putString(PREF_USER_EMAIL, null);
        editor.putString(PREF_USER_PHOTO, null);
        editor.putString(PREF_USER_REPUTATION, null);
        editor.putString(PREF_USER_DATE_BIRTH, null);
        editor.putString(PREF_USER_GENDER, null);
        editor.putString(PREF_USER_HEIGHT, null);
        editor.putString(PREF_USER_COUNTRY, null);
        editor.putString(PREF_USER_SESSION, null);
        editor.apply();
    }

    public String getUserId(){
        return mPrefs.getString(PREF_USER_ID, null);
    }
    public String getUserRol(){
        return mPrefs.getString(PREF_USER_ROL, null);
    }
    public String getUserPhoto(){
        return mPrefs.getString(PREF_USER_PHOTO, null);
    }
    public String getUserSession(){
        return mPrefs.getString(PREF_USER_SESSION, null);
    }
    public String getUserHeight(){
        return mPrefs.getString(PREF_USER_HEIGHT, null);
    }
    public String getUserEmail(){
        return mPrefs.getString(PREF_USER_EMAIL, null);
    }

    public String getUserName(){
        return mPrefs.getString(PREF_USER_NAME, null)+" "+mPrefs.getString(PREF_USER_SURNAME, null);
    }

    public String getUserPref(){
        return mPrefs.getString(PREF_USER_SESSION, null);
    }

    public void setUserHeight(String height) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USER_HEIGHT, height);
        editor.apply();
    }
}