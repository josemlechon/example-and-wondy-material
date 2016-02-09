package com.example.wondy.events;

import android.support.annotation.NonNull;

/**
 * Created by jose m lechon on 08/02/2016
 *
 * @version 0.1.0
 * @since 1
 */
public class BaseEvent<T> {

    protected  T mData;

    private  boolean mSuccess;

    private String mError;

    public BaseEvent(boolean success) {
        mSuccess = success;
    }

    public BaseEvent(boolean success, T data) {
        mSuccess = success;
        this.mData = data;
    }

    public BaseEvent(boolean success, @NonNull String error)  {
        mSuccess = success;
        mError = error;
    }

    public boolean isSuccess() {
        return mSuccess;
    }


    public String getError(){
        return mError;
    }

    public T getData(){
        return mData;
    }
}
