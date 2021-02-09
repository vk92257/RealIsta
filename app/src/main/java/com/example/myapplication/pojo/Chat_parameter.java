package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Chat_parameter implements Parcelable {
    private String id;
    private String name;
    private String profileImg;

    public Chat_parameter() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.profileImg);
    }

    protected Chat_parameter(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.profileImg = in.readString();
    }

    public static final Parcelable.Creator<Chat_parameter> CREATOR = new Parcelable.Creator<Chat_parameter>() {
        @Override
        public Chat_parameter createFromParcel(Parcel source) {
            return new Chat_parameter(source);
        }

        @Override
        public Chat_parameter[] newArray(int size) {
            return new Chat_parameter[size];
        }
    };
}
