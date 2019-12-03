package com.example.jonsmauricio.eyesfood;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by JonásMauricio on 07-11-2017.
 */

public class EyesFoodApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
