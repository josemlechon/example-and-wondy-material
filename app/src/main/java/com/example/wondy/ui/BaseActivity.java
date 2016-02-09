package com.example.wondy.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wondy.di.ComponentFactory;
import com.example.wondy.di.component.ActivityComponent;


/**
 * Created by jose m lechon on 08/02/2016
 *
 * @version 0.1.0
 * @since 1
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponent = ComponentFactory.getActivityComponent(getApplication());
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

}
