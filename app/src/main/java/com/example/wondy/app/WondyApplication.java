package com.example.wondy.app;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.wondy.di.component.AppComponent;
import com.example.wondy.di.component.DaggerAppComponent;
import com.example.wondy.di.module.AppModule;
import com.example.wondy.io.ApiModule;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class WondyApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();
    }


    private void initDagger() {

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(AppConfig.getApiUrl()))
                .build();
    }


    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @VisibleForTesting
    public void setAppComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
