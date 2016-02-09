package com.example.wondy.di.component;


import com.example.wondy.di.scope.ActivityScope;
import com.example.wondy.ui.detail.DetailActivity;
import com.example.wondy.ui.home.ListCentersFragment;
import com.example.wondy.ui.home.MainActivity;
import com.example.wondy.ui.profile.ProfileActivity;

import dagger.Component;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {



    //Activities
    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);

    //Fragments
    void inject(ListCentersFragment listCentersFragment);

    void inject(ProfileActivity profileActivity);
}
