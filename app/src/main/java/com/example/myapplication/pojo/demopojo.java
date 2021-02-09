package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class demopojo implements Parcelable {
    ArrayList<Client_Explore_detailfetch> data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.data);
    }

    public demopojo() {
    }

    protected demopojo(Parcel in) {
        this.data = new ArrayList<Client_Explore_detailfetch>();
        in.readList(this.data, Client_Explore_detailfetch.class.getClassLoader());
    }

    public static final Parcelable.Creator<demopojo> CREATOR = new Parcelable.Creator<demopojo>() {
        @Override
        public demopojo createFromParcel(Parcel source) {
            return new demopojo(source);
        }

        @Override
        public demopojo[] newArray(int size) {
            return new demopojo[size];
        }
    };
}
