package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ImageViewSinglefullsize implements Parcelable {
    String profileimage;
    String viewimage;
    String fullname;
    String professiontitle;
    String UserId;
    int postion;
    ArrayList<String> Images_hd;
    ArrayList<Client_Explore_detailfetch> data;

    public ImageViewSinglefullsize(String profileimage, String viewimage, String fullname, String professiontitle) {
        this.profileimage = profileimage;
        this.viewimage = viewimage;
        this.fullname = fullname;
        this.professiontitle = professiontitle;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getViewimage() {
        return viewimage;
    }

    public void setViewimage(String viewimage) {
        this.viewimage = viewimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfessiontitle() {
        return professiontitle;
    }

    public void setProfessiontitle(String professiontitle) {
        this.professiontitle = professiontitle;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public ArrayList<String> getImages_hd() {
        return Images_hd;
    }

    public void setImages_hd(ArrayList<String> images_hd) {
        Images_hd = images_hd;
    }

    public ArrayList<Client_Explore_detailfetch> getData() {
        return data;
    }

    public void setData(ArrayList<Client_Explore_detailfetch> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.viewimage);
        dest.writeString(this.profileimage);
        dest.writeString(this.fullname);
        dest.writeString(this.professiontitle);
        dest.writeString(this.UserId);
        dest.writeInt(this.postion);
        dest.writeStringList(this.Images_hd);
        dest.writeList(this.data);
    }

    protected ImageViewSinglefullsize(Parcel in) {
        this.viewimage = in.readString();
        this.profileimage = in.readString();
        this.fullname = in.readString();
        this.professiontitle = in.readString();
        this.UserId = in.readString();
        this.postion = in.readInt();
        this.Images_hd = in.createStringArrayList();
        this.data = new ArrayList<Client_Explore_detailfetch>();
        in.readList(this.data, Client_Explore_detailfetch.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImageViewSinglefullsize> CREATOR = new Parcelable.Creator<ImageViewSinglefullsize>() {
        @Override
        public ImageViewSinglefullsize createFromParcel(Parcel source) {
            return new ImageViewSinglefullsize(source);
        }

        @Override
        public ImageViewSinglefullsize[] newArray(int size) {
            return new ImageViewSinglefullsize[size];
        }
    };
}
