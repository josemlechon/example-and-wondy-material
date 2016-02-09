package com.example.wondy.di.component;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.wondy.app.AppConfig;
import com.example.wondy.controller.FeedController;
import com.example.wondy.di.module.AppModule;
import com.example.wondy.io.ApiModule;
import com.example.wondy.io.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    EventBus providesEventBus();

    ApiService apiService();

    Application providesApplication();

    Context providesContext();

    AppConfig providesConfig();

    FeedController photoController();

    ObjectMapper provideObjectMapper();

    void inject(FeedController feedController);


}
