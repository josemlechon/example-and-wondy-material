package com.example.wondy.di;

import android.app.Application;

import com.example.wondy.app.WondyApplication;
import com.example.wondy.di.component.ActivityComponent;
import com.example.wondy.di.component.AppComponent;
import com.example.wondy.di.component.DaggerActivityComponent;


/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class ComponentFactory {


    public static AppComponent getAppComponent(Application context) {

        return ((WondyApplication) context).getAppComponent();
    }


    public static ActivityComponent getActivityComponent(Application context) {

        AppComponent appComponent = ((WondyApplication) context).getAppComponent();

        return DaggerActivityComponent.builder()
                .appComponent(appComponent).build();
    }


}
