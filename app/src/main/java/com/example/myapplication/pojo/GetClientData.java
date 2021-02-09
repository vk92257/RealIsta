package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class GetClientData implements Parcelable {
    public String user_id;
    public String name,email,gender,mobile;
    public locationpojo country,state,city;
    public String profile_img;
    public String create_at;
    public String updated_at;

    public GetClientData(String user_id, String name, String email, String gender,
                         String mobile, locationpojo country, locationpojo state, locationpojo city,
                         String profile_img, String create_at, String updated_at) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.country = country;
        this.state = state;
        this.city = city;
        this.profile_img = profile_img;
        this.create_at = create_at;
        this.updated_at = updated_at;
    }

    public GetClientData() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public locationpojo getCountry() {
        return country;
    }

    public void setCountry(locationpojo country) {
        this.country = country;
    }

    public locationpojo getState() {
        return state;
    }

    public void setState(locationpojo state) {
        this.state = state;
    }

    public locationpojo getCity() {
        return city;
    }

    public void setCity(locationpojo city) {
        this.city = city;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.mobile);
        dest.writeParcelable(this.country, flags);
        dest.writeParcelable(this.state, flags);
        dest.writeParcelable(this.city, flags);
        dest.writeString(this.profile_img);
        dest.writeString(this.create_at);
        dest.writeString(this.updated_at);
    }

    protected GetClientData(Parcel in) {
        this.user_id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.mobile = in.readString();
        this.country = in.readParcelable(locationpojo.class.getClassLoader());
        this.state = in.readParcelable(locationpojo.class.getClassLoader());
        this.city = in.readParcelable(locationpojo.class.getClassLoader());
        this.profile_img = in.readString();
        this.create_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<GetClientData> CREATOR = new Parcelable.Creator<GetClientData>() {
        @Override
        public GetClientData createFromParcel(Parcel source) {
            return new GetClientData(source);
        }

        @Override
        public GetClientData[] newArray(int size) {
            return new GetClientData[size];
        }
    };
}
