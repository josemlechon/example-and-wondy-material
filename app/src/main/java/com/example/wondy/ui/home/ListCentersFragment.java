package com.example.wondy.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;


import com.example.wondy.R;
import com.example.wondy.events.CentersEvent;
import com.example.wondy.model.Center;
import com.example.wondy.ui.BaseFragment;
import com.example.wondy.ui.widget.recycler.EmptyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;



/**
 * @author jose m lechon on 08/02/2016
 * @version 1.0.0
 * @since 1
 */
public class ListCentersFragment extends BaseFragment implements CentersAdapter.Callback {


    private OnListPlacesInteractionListener mListener;

    public ListCentersFragment() { }


    CentersAdapter mCentersAdapter;

    @Inject
    EventBus mEventBus;

    //Views
    @Bind(R.id.emptyrecyclerview_centers)
    EmptyRecyclerView mEmptyRecyclerView;

    @Bind(android.R.id.empty)
    TextView mTextViewEmpty;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListPlacesInteractionListener) {
            mListener = (OnListPlacesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);
    }


    @Override
    @LayoutRes protected int getLayoutView() {
        return R.layout.fragment_list_centers;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCentersAdapter = new CentersAdapter();
        mCentersAdapter.setCallback(this);

        mEmptyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mEmptyRecyclerView.setEmptyView(mTextViewEmpty);
        mEmptyRecyclerView.setAdapter(mCentersAdapter);


        mTextViewEmpty.setText(R.string.no_centers);
    }

    @Override
    public void onStart() {
        super.onStart();

        mEventBus.register(this );
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(CentersEvent event){

        if(event.isSuccess()){
            mCentersAdapter.setData(new ArrayList<>(event.getData()));
        }else{
            mCentersAdapter.clear();
        }
    }




    @Override
    public void onCenterClicked(Center center) {
        if(mListener == null) return;

        mListener.onPlaceSelected(center );
    }


    public interface OnListPlacesInteractionListener {

        void onPlaceSelected(Center center);
    }
}
