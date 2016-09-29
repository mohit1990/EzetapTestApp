package com.demoapp.ezetaptestapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohit on 30/09/16.
 */

public class ResultModel implements Parcelable{

    private String key;
    private String value;

    public ResultModel(){

    }

    public ResultModel(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<ResultModel> CREATOR = new Creator<ResultModel>() {
        @Override
        public ResultModel createFromParcel(Parcel in) {
            return new ResultModel(in);
        }

        @Override
        public ResultModel[] newArray(int size) {
            return new ResultModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(value);
    }
}
