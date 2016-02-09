package com.example.wondy.controller;

import android.content.Context;

import com.example.wondy.di.component.AppComponent;
import com.example.wondy.events.BaseEvent;
import com.example.wondy.events.CentersEvent;
import com.example.wondy.events.ErrorNetEvent;
import com.example.wondy.io.ApiService;
import com.example.wondy.model.Center;
import com.example.wondy.util.L;
import com.example.wondy.util.helper.NetworkHelper;
import com.example.wondy.util.validation.ValidationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class FeedController {

    public static final String TAG = FeedController.class.getSimpleName();


    private HashMap<String, Center> mHashMapCache = new HashMap<>();

    @Inject
    EventBus mEventBus;

    @Inject
    ApiService mApiService;

    @Inject
    Context mContext;


    public FeedController(AppComponent appComponent) {

        appComponent.inject(this);
        mEventBus.register(this);
    }

    @Subscribe
    public void onEvent(BaseEvent event) {

    }

    public void requestCleanFeed() {

        if (!NetworkHelper.isNetworkAvailable(mContext)) {

            mEventBus.post(new ErrorNetEvent());
            return;
        }

        Call<List<Center>> call = mApiService.getCenters();
        call.enqueue(new CustomCallback(Boolean.TRUE));
    }

    public void requestFeed() {

        List<Center> listValidCenter = new ArrayList<>(mHashMapCache.values());

        //Sending cached data
        mEventBus.post(new CentersEvent(Boolean.TRUE, listValidCenter));


        //Checking network
        if (!NetworkHelper.isNetworkAvailable(mContext) && !listValidCenter.isEmpty())
            return; //there is data cached to show

        else if (!NetworkHelper.isNetworkAvailable(mContext)) { //nothing cached, show message
            mEventBus.post(new ErrorNetEvent());
            return;
        }

        Call<List<Center>> call = mApiService.getCenters();
        call.enqueue(new CustomCallback(Boolean.FALSE));

    }


    class CustomCallback implements Callback<List<Center>> {

        boolean mClean;

        public CustomCallback(Boolean clean) {
            this.mClean = clean;
        }

        @Override
        public void onResponse(Response<List<Center>> response, Retrofit retrofit) {
            L.LOGD(TAG, "Response");

            if (mClean) mHashMapCache.clear();

            saveIfNotExist(response.body());

            List<Center> listValidPlaces = new ArrayList<>(mHashMapCache.values());
            CentersEvent event = new CentersEvent(Boolean.TRUE, listValidPlaces);
            mEventBus.post(event);
        }

        @Override
        public void onFailure(Throwable t) {

            L.LOGE(TAG, "Error ", t);
            mEventBus.post(new CentersEvent(Boolean.FALSE, null));
        }
    }


    public synchronized void saveIfNotExist(final List<Center> centers) {
        ValidationUtil.pruneInvalid(centers);

        if (centers.isEmpty()) {
            return;
        }

        for (Center item : centers) {
            mHashMapCache.put(item.idCenter, item);
        }
    }
}
