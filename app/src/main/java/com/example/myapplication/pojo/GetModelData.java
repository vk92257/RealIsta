package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetModelData implements Parcelable {

    public String user_id;
    private String name;
    private String email;
    private String mobile;
    private String country;
    private String state;
    private String city;
    private locationpojo country_pojo;
    private locationpojo state_pojo;
    private locationpojo city_pojo;
    private String age;
    private String proffesion;
    private String gender;
    private String passport;
    private String personal_bio;
    private String personal_journey;
    private String hourly_rate;
    private String body_bio;
    private String body_journey;
    private String role_age_min;
    private String role_age_max;
    private String height_feet;
    private String height_inches;
    private ArrayList<String> skill;
    private ArrayList<String> hd_images;
    private String profile_img;
    private String weight;
    private String video_url;
    private ArrayList<String> language;
    private ArrayList<String>  body_type;
    private ArrayList<String> ethnicity;
    private String created_at;
    private String updated_at;
    private String waist;
    private String  hip;
    private String chest;


    public String getImgCount() {
        return imgCount;
    }

    public void setImgCount(String imgCount) {
        this.imgCount = imgCount;
    }

    private String imgCount;

    public GetModelData() {
    }

    public GetModelData(String user_id, String name, String email, String mobile,
                      String age, String proffesion, String gender,
                        String passport, String personal_bio, String personal_journey, String hourly_rate,
                    String role_age_min, String role_age_max,
                        String height_feet, String height_inches, ArrayList<String>  skill, ArrayList<String>  hd_images, String profile_img,
                        String weight, String video_url, ArrayList<String>  language, ArrayList<String>  body_type,
                        ArrayList<String>  ethnicity, String created_at, String updated_at) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.age = age;
        this.proffesion = proffesion;
        this.gender = gender;
        this.passport = passport;
        this.personal_bio = personal_bio;
        this.personal_journey = personal_journey;
        this.hourly_rate = hourly_rate;
        this.role_age_min = role_age_min;
        this.role_age_max = role_age_max;
        this.height_feet = height_feet;
        this.height_inches = height_inches;
        this.skill = skill;
        this.hd_images = hd_images;
        this.profile_img = profile_img;
        this.weight = weight;
        this.video_url = video_url;
        this.language = language;
        this.body_type = body_type;
        this.ethnicity = ethnicity;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public locationpojo getCountry_pojo() {
        return country_pojo;
    }

    public void setCountry_pojo(locationpojo country_pojo) {
        this.country_pojo = country_pojo;
    }

    public locationpojo getState_pojo() {
        return state_pojo;
    }

    public void setState_pojo(locationpojo state_pojo) {
        this.state_pojo = state_pojo;
    }

    public locationpojo getCity_pojo() {
        return city_pojo;
    }

    public void setCity_pojo(locationpojo city_pojo) {
        this.city_pojo = city_pojo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProffesion() {
        return proffesion;
    }

    public void setProffesion(String proffesion) {
        this.proffesion = proffesion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPersonal_bio() {
        return personal_bio;
    }

    public void setPersonal_bio(String personal_bio) {
        this.personal_bio = personal_bio;
    }

    public String getPersonal_journey() {
        return personal_journey;
    }

    public void setPersonal_journey(String personal_journey) {
        this.personal_journey = personal_journey;
    }

    public String getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(String hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public String getBody_bio() {
        return body_bio;
    }

    public void setBody_bio(String body_bio) {
        this.body_bio = body_bio;
    }

    public String getBody_journey() {
        return body_journey;
    }

    public void setBody_journey(String body_journey) {
        this.body_journey = body_journey;
    }

    public String getRole_age_min() {
        return role_age_min;
    }

    public void setRole_age_min(String role_age_min) {
        this.role_age_min = role_age_min;
    }

    public String getRole_age_max() {
        return role_age_max;
    }

    public void setRole_age_max(String role_age_max) {
        this.role_age_max = role_age_max;
    }

    public String getHeight_feet() {
        return height_feet;
    }

    public void setHeight_feet(String height_feet) {
        this.height_feet = height_feet;
    }

    public String getHeight_inches() {
        return height_inches;
    }

    public void setHeight_inches(String height_inches) {
        this.height_inches = height_inches;
    }

    public ArrayList<String> getSkill() {
        return skill;
    }

    public void setSkill(ArrayList<String> skill) {
        this.skill = skill;
    }

    public ArrayList<String> getHd_images() {
        return hd_images;
    }

    public void setHd_images(ArrayList<String> hd_images) {
        this.hd_images = hd_images;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public ArrayList<String> getLanguage() {
        return language;
    }

    public void setLanguage(ArrayList<String> language) {
        this.language = language;
    }

    public ArrayList<String> getBody_type() {
        return body_type;
    }

    public void setBody_type(ArrayList<String> body_type) {
        this.body_type = body_type;
    }

    public ArrayList<String> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(ArrayList<String> ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getHip() {
        return hip;
    }

    public void setHip(String hip) {
        this.hip = hip;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public static Creator<GetModelData> getCREATOR() {
        return CREATOR;
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
        dest.writeString(this.mobile);
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeParcelable(this.country_pojo, flags);
        dest.writeParcelable(this.state_pojo, flags);
        dest.writeParcelable(this.city_pojo, flags);
        dest.writeString(this.age);
        dest.writeString(this.proffesion);
        dest.writeString(this.gender);
        dest.writeString(this.passport);
        dest.writeString(this.personal_bio);
        dest.writeString(this.personal_journey);
        dest.writeString(this.hourly_rate);
        dest.writeString(this.body_bio);
        dest.writeString(this.body_journey);
        dest.writeString(this.role_age_min);
        dest.writeString(this.role_age_max);
        dest.writeString(this.height_feet);
        dest.writeString(this.height_inches);
        dest.writeStringList(this.skill);
        dest.writeStringList(this.hd_images);
        dest.writeString(this.profile_img);
        dest.writeString(this.weight);
        dest.writeString(this.video_url);
        dest.writeStringList(this.language);
        dest.writeStringList(this.body_type);
        dest.writeStringList(this.ethnicity);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.waist);
        dest.writeString(this.hip);
        dest.writeString(this.chest);
        dest.writeString(this.imgCount);
    }

    protected GetModelData(Parcel in) {
        this.user_id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.country = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.country_pojo = in.readParcelable(locationpojo.class.getClassLoader());
        this.state_pojo = in.readParcelable(locationpojo.class.getClassLoader());
        this.city_pojo = in.readParcelable(locationpojo.class.getClassLoader());
        this.age = in.readString();
        this.proffesion = in.readString();
        this.gender = in.readString();
        this.passport = in.readString();
        this.personal_bio = in.readString();
        this.personal_journey = in.readString();
        this.hourly_rate = in.readString();
        this.body_bio = in.readString();
        this.body_journey = in.readString();
        this.role_age_min = in.readString();
        this.role_age_max = in.readString();
        this.height_feet = in.readString();
        this.height_inches = in.readString();
        this.skill = in.createStringArrayList();
        this.hd_images = in.createStringArrayList();
        this.profile_img = in.readString();
        this.weight = in.readString();
        this.video_url = in.readString();
        this.language = in.createStringArrayList();
        this.body_type = in.createStringArrayList();
        this.ethnicity = in.createStringArrayList();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.waist = in.readString();
        this.hip = in.readString();
        this.chest = in.readString();
        this.imgCount = in.readString();
    }

    public static final Parcelable.Creator<GetModelData> CREATOR = new Parcelable.Creator<GetModelData>() {
        @Override
        public GetModelData createFromParcel(Parcel source) {
            return new GetModelData(source);
        }

        @Override
        public GetModelData[] newArray(int size) {
            return new GetModelData[size];
        }
    };
}
