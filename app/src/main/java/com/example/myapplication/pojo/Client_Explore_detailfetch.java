package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Client_Explore_detailfetch implements Parcelable {
    public String user_id;
    public String name;
    public String hd_images;
    public String proffesion;
    public String Profile_img;

    public Client_Explore_detailfetch(String user_id, String name, String hd_images,String proffesion,String Profile_img) {
        this.user_id = user_id;
        this.name = name;
        this.hd_images = hd_images;
        this.proffesion = proffesion;
        this.Profile_img = Profile_img;
    }

    public Client_Explore_detailfetch() {
    }

    public String getProffesion() {
        return proffesion;
    }

    public void setProffesion(String proffesion) {
        this.proffesion = proffesion;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHd_images() {
        return hd_images;
    }

    public void setHd_images(String hd_images) {
        this.hd_images = hd_images;
    }

    public String getProfile_img() {
        return Profile_img;
    }

    public void setProfile_img(String profile_img) {
        Profile_img = profile_img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.hd_images);
        dest.writeString(this.proffesion);
        dest.writeString(this.Profile_img);
    }

    protected Client_Explore_detailfetch(Parcel in) {
        this.user_id = in.readString();
        this.name = in.readString();
        this.hd_images = in.readString();
        this.proffesion = in.readString();
        this.Profile_img = in.readString();
    }

    public static final Parcelable.Creator<Client_Explore_detailfetch> CREATOR = new Parcelable.Creator<Client_Explore_detailfetch>() {
        @Override
        public Client_Explore_detailfetch createFromParcel(Parcel source) {
            return new Client_Explore_detailfetch(source);
        }

        @Override
        public Client_Explore_detailfetch[] newArray(int size) {
            return new Client_Explore_detailfetch[size];
        }
    };
}
