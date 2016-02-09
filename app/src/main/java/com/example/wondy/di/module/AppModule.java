package com.example.wondy.di.module;

import android.app.Application;
import android.content.Context;

import com.example.wondy.app.AppConfig;
import com.example.wondy.app.WondyApplication;
import com.example.wondy.controller.FeedController;
import com.example.wondy.di.ComponentFactory;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */

@Module
public class AppModule {


    private final WondyApplication mApplication;

    public AppModule(WondyApplication app) {
        mApplication = app;
    }


    @Provides
    @Singleton
    public EventBus providesEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    public AppConfig providesConfig() {
        return new AppConfig(mApplication);
    }


    @Provides
    @Singleton
    public FeedController photoController() {

        return new FeedController(ComponentFactory.getAppComponent(providesApplication()));
    }

}
