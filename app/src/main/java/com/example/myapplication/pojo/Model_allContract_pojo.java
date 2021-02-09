package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Model_allContract_pojo implements Parcelable {
    public String jobid;
    public String contractid;
    public String job_title;
    public String name;
    public String job_duration;
    public String performance_location;
    public String product_name;
    public String final_price;
    public String position;
    public String status;
    public String profile_img;
    public String client_information;
    public String userid;

    public Model_allContract_pojo() {
    }

    public Model_allContract_pojo(String jobid, String contractid, String job_title, String name,
                                  String job_duration, String performance_location, String product_name,
                                  String final_price, String position, String status, String profile_img) {
        this.jobid = jobid;
        this.contractid = contractid;
        this.job_title = job_title;
        this.name = name;
        this.job_duration = job_duration;
        this.performance_location = performance_location;
        this.product_name = product_name;
        this.final_price = final_price;
        this.position = position;
        this.status = status;
        this.profile_img = profile_img;
    }

    public String getClient_information() {
        return client_information;
    }

    public void setClient_information(String client_information) {
        this.client_information = client_information;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getContractid() {
        return contractid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob_duration() {
        return job_duration;
    }

    public void setJob_duration(String job_duration) {
        this.job_duration = job_duration;
    }

    public String getPerformance_location() {
        return performance_location;
    }

    public void setPerformance_location(String performance_location) {
        this.performance_location = performance_location;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobid);
        dest.writeString(this.contractid);
        dest.writeString(this.job_title);
        dest.writeString(this.name);
        dest.writeString(this.job_duration);
        dest.writeString(this.performance_location);
        dest.writeString(this.product_name);
        dest.writeString(this.final_price);
        dest.writeString(this.position);
        dest.writeString(this.status);
        dest.writeString(this.userid);
    }

    protected Model_allContract_pojo(Parcel in) {
        this.jobid = in.readString();
        this.contractid = in.readString();
        this.job_title = in.readString();
        this.name = in.readString();
        this.job_duration = in.readString();
        this.performance_location = in.readString();
        this.product_name = in.readString();
        this.final_price = in.readString();
        this.position = in.readString();
        this.status = in.readString();
        this.userid = in.readString();
    }

    public static final Parcelable.Creator<Model_allContract_pojo> CREATOR = new Parcelable.Creator<Model_allContract_pojo>() {
        @Override
        public Model_allContract_pojo createFromParcel(Parcel source) {
            return new Model_allContract_pojo(source);
        }

        @Override
        public Model_allContract_pojo[] newArray(int size) {
            return new Model_allContract_pojo[size];
        }
    };
}
