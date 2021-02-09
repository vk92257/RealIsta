package com.example.myapplication.clientsprofile.clients_fragment;

public class Client_Explore_detailfetch {
    public String user_id;
    public String name;
    public String hd_images;
    public String proffesion;

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String profile_img;
    public Client_Explore_detailfetch(){

    }
    public Client_Explore_detailfetch(String user_id, String name, String hd_images,String proffesion) {
        this.user_id = user_id;
        this.name = name;
        this.hd_images = hd_images;
        this.proffesion = proffesion;
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
}
