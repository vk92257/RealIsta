package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class locationpojo implements Parcelable {
    String Locationid;
    String location_name;

    public locationpojo() {
    }

    public locationpojo(String locationid, String location_name) {
        Locationid = locationid;
        this.location_name = location_name;
    }

    public String getLocationid() {
        return Locationid;
    }

    public void setLocationid(String locationid) {
        Locationid = locationid;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Locationid);
        dest.writeString(this.location_name);
    }

    protected locationpojo(Parcel in) {
        this.Locationid = in.readString();
        this.location_name = in.readString();
    }

    public static final Parcelable.Creator<locationpojo> CREATOR = new Parcelable.Creator<locationpojo>() {
        @Override
        public locationpojo createFromParcel(Parcel source) {
            return new locationpojo(source);
        }

        @Override
        public locationpojo[] newArray(int size) {
            return new locationpojo[size];
        }
    };
}
