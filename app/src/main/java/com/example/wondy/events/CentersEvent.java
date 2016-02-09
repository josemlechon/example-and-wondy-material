package com.example.wondy.events;

import android.support.annotation.Nullable;

import com.example.wondy.model.Center;

import java.util.List;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class CentersEvent extends BaseEvent<List<Center>> {

    public CentersEvent(boolean success, @Nullable List<Center> data) {
        super(success, data);
    }
}
