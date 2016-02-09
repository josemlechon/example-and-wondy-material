package com.example.wondy.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wondy.BuildConfig;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class AppConfig {

    private static final String NAME_PROPERTIES = "app_properties";

    private final SharedPreferences mSharedPreferences;

    private static final String MAP_STATIC_IMAGE = "http://maps.google.com/maps/api/staticmap?center=%s&zoom=15&size=400x400&sensor=false&markers=color:green|%s";

    private static final String PREF_IMAGE_PATH = AppConfig.class.getCanonicalName() + "PREF_IMAGE_PATH";

    public AppConfig(Context context) {
        mSharedPreferences = context.getSharedPreferences(NAME_PROPERTIES, Context.MODE_PRIVATE);
    }

    public static String getApiUrl() {
        return BuildConfig.SERVER_URL;
    }


    public static String getStaticMap(String coordinates){

        return String.format(MAP_STATIC_IMAGE, coordinates, coordinates);
    }


    public  String getUserImagePath(){

        return mSharedPreferences.getString(PREF_IMAGE_PATH, "");
    }

    public  void setUserImagePath(String path){

         mSharedPreferences.edit().putString(PREF_IMAGE_PATH, path).apply();
    }
}
