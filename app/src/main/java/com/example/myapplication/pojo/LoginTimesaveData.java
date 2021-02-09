package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginTimesaveData implements Parcelable {
    private String token ;
    private String userid;
    private String role;

    public LoginTimesaveData(String token, String userid,String role) {
        this.token = token;
        this.userid = userid;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.userid);
        dest.writeString(this.role);
    }

    protected LoginTimesaveData(Parcel in) {
        this.token = in.readString();
        this.userid = in.readString();
        this.role = in.readString();
    }

    public static final Parcelable.Creator<LoginTimesaveData> CREATOR = new Parcelable.Creator<LoginTimesaveData>() {
        @Override
        public LoginTimesaveData createFromParcel(Parcel source) {
            return new LoginTimesaveData(source);
        }

        @Override
        public LoginTimesaveData[] newArray(int size) {
            return new LoginTimesaveData[size];
        }
    };
}
