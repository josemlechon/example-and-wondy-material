package com.example.wondy.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.wondy.di.ComponentFactory;
import com.example.wondy.di.component.ActivityComponent;

import butterknife.ButterKnife;

/**
 * Created by jose m lechon on 29/01/16.
 *
 * @version 0.1.0
 * @since 1
 */
public abstract class BaseFragment extends Fragment {


    private ActivityComponent mComponent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponent = ComponentFactory.getActivityComponent(getActivity().getApplication());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( getLayoutView() , container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    protected abstract @LayoutRes int getLayoutView();


    protected ActivityComponent getComponent() {
        return mComponent;
    }

}
