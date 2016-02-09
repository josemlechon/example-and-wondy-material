package com.example.wondy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.wondy.util.validation.Validation;
import com.example.wondy.util.validation.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */


public class Center implements Parcelable, Validation {


    @JsonProperty("id")
    public String idCenter;


    @JsonProperty("long")
    public double longitude;


    @JsonProperty("lat")
    public double latitude;


    @JsonProperty("name")
    public String name;


    @JsonProperty("address")
    public String address;

    @JsonProperty("img")
    public List<String> images;

    @JsonProperty("services")
    public List<String> services;

    @JsonProperty("desc")
    public String description;


    public Center() {
    }

    protected Center(Parcel in) {
        idCenter = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        name = in.readString();
        address = in.readString();
        images = in.createStringArrayList();
        services = in.createStringArrayList();
        description = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idCenter);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeStringList(images);
        dest.writeStringList(services);
        dest.writeString(description);
    }


    public static final Creator<Center> CREATOR = new Creator<Center>() {
        @Override
        public Center createFromParcel(Parcel in) {
            return new Center(in);
        }

        @Override
        public Center[] newArray(int size) {
            return new Center[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public
    @Nullable
    MarkerOptions getMapMarker() {


        try {
            // Add a marker in Sydney and move the camera
            LatLng coordinates = new LatLng(this.latitude, this.longitude);

            return new MarkerOptions()
                    .position(coordinates)
                    .title(this.name);

        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public void validate() {

        if (StringUtils.isEmpty(idCenter)) {
            throw new ValidationFailedException("invalid Center id");
        }
        if (StringUtils.isEmpty(name)) {
            throw new ValidationFailedException("invalid created date");
        }
        if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
            throw new ValidationFailedException("invalid location");
        }

    }


}
