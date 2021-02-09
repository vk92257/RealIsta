package com.example.myapplication.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class JobPostdetail_pojo implements Parcelable {
        public String job_id;
        public String user_id;
        public String name;
        public String role_age_min;
        public String role_age_max;
        public String height_feet;
        public String height_inches;
        public String weight;
        public String about_project;
        public String about_personality;
        public String job_rate_per_day;
        public String job_title;
        public String job_type;
        public String total_roles;
        public String gender_type;
        public String product_name;
        public String job_duration;
        public String role_description;
        public String performance_location;
        public String performance_from_date;
        public String performance_to_date;
        public String client_information;
        public List<String> skill;
        public List<String> additional_attachment;
        public List<String> hair_color;
        public List<String> eye_color;
        public List<String> hair_length;
        public List<String> ethnicity;
        public String created_at;
        public String updated_at;

    public JobPostdetail_pojo(String job_id, String user_id, String name,
                              String role_age_min, String role_age_max,
                              String height_feet, String height_inches,
                              String weight, String about_project,
                              String about_personality,
                              String job_rate_per_day, String job_title, String job_type,
                              String total_roles, String gender_type, String product_name,
                              String job_duration, String role_description, String performance_location,
                              String performance_from_date, String performance_to_date,
                              String client_information, List<String> skill, List<String> additional_attachment,
                              List<String> hair_color,
                              List<String> eye_color, List<String> hair_length, List<String> ethnicity,
                              String created_at, String updated_at) {
        this.job_id = job_id;
        this.user_id = user_id;
        this.name = name;
        this.role_age_min = role_age_min;
        this.role_age_max = role_age_max;
        this.height_feet = height_feet;
        this.height_inches = height_inches;
        this.weight = weight;
        this.about_project = about_project;
        this.about_personality = about_personality;
        this.job_rate_per_day = job_rate_per_day;
        this.job_title = job_title;
        this.job_type = job_type;
        this.total_roles = total_roles;
        this.gender_type = gender_type;
        this.product_name = product_name;
        this.job_duration = job_duration;
        this.role_description = role_description;
        this.performance_location = performance_location;
        this.performance_from_date = performance_from_date;
        this.performance_to_date = performance_to_date;
        this.client_information = client_information;
        this.skill = skill;
        this.additional_attachment = additional_attachment;
        this.hair_color = hair_color;
        this.eye_color = eye_color;
        this.hair_length = hair_length;
        this.ethnicity = ethnicity;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAbout_project() {
        return about_project;
    }

    public void setAbout_project(String about_project) {
        this.about_project = about_project;
    }

    public String getAbout_personality() {
        return about_personality;
    }

    public void setAbout_personality(String about_personality) {
        this.about_personality = about_personality;
    }

    public String getJob_rate_per_day() {
        return job_rate_per_day;
    }

    public void setJob_rate_per_day(String job_rate_per_day) {
        this.job_rate_per_day = job_rate_per_day;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getTotal_roles() {
        return total_roles;
    }

    public void setTotal_roles(String total_roles) {
        this.total_roles = total_roles;
    }

    public String getGender_type() {
        return gender_type;
    }

    public void setGender_type(String gender_type) {
        this.gender_type = gender_type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getJob_duration() {
        return job_duration;
    }

    public void setJob_duration(String job_duration) {
        this.job_duration = job_duration;
    }

    public String getRole_description() {
        return role_description;
    }

    public void setRole_description(String role_description) {
        this.role_description = role_description;
    }

    public String getPerformance_location() {
        return performance_location;
    }

    public void setPerformance_location(String performance_location) {
        this.performance_location = performance_location;
    }

    public String getPerformance_from_date() {
        return performance_from_date;
    }

    public void setPerformance_from_date(String performance_from_date) {
        this.performance_from_date = performance_from_date;
    }

    public String getPerformance_to_date() {
        return performance_to_date;
    }

    public void setPerformance_to_date(String performance_to_date) {
        this.performance_to_date = performance_to_date;
    }

    public String getClient_information() {
        return client_information;
    }

    public void setClient_information(String client_information) {
        this.client_information = client_information;
    }

    public List<String> getSkill() {
        return skill;
    }

    public void setSkill(List<String> skill) {
        this.skill = skill;
    }

    public List<String> getAdditional_attachment() {
        return additional_attachment;
    }

    public void setAdditional_attachment(List<String> additional_attachment) {
        this.additional_attachment = additional_attachment;
    }

    public List<String> getHair_color() {
        return hair_color;
    }

    public void setHair_color(List<String> hair_color) {
        this.hair_color = hair_color;
    }

    public List<String> getEye_color() {
        return eye_color;
    }

    public void setEye_color(List<String> eye_color) {
        this.eye_color = eye_color;
    }

    public List<String> getHair_length() {
        return hair_length;
    }

    public void setHair_length(List<String> hair_length) {
        this.hair_length = hair_length;
    }

    public List<String> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(List<String> ethnicity) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.job_id);
        dest.writeString(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.role_age_min);
        dest.writeString(this.role_age_max);
        dest.writeString(this.height_feet);
        dest.writeString(this.height_inches);
        dest.writeString(this.weight);
        dest.writeString(this.about_project);
        dest.writeString(this.about_personality);
        dest.writeString(this.job_rate_per_day);
        dest.writeString(this.job_title);
        dest.writeString(this.job_type);
        dest.writeString(this.total_roles);
        dest.writeString(this.gender_type);
        dest.writeString(this.product_name);
        dest.writeString(this.job_duration);
        dest.writeString(this.role_description);
        dest.writeString(this.performance_location);
        dest.writeString(this.performance_from_date);
        dest.writeString(this.performance_to_date);
        dest.writeString(this.client_information);
        dest.writeStringList(this.skill);
        dest.writeStringList(this.additional_attachment);
        dest.writeStringList(this.hair_color);
        dest.writeStringList(this.eye_color);
        dest.writeStringList(this.hair_length);
        dest.writeStringList(this.ethnicity);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected JobPostdetail_pojo(Parcel in) {
        this.job_id = in.readString();
        this.user_id = in.readString();
        this.name = in.readString();
        this.role_age_min = in.readString();
        this.role_age_max = in.readString();
        this.height_feet = in.readString();
        this.height_inches = in.readString();
        this.weight = in.readString();
        this.about_project = in.readString();
        this.about_personality = in.readString();
        this.job_rate_per_day = in.readString();
        this.job_title = in.readString();
        this.job_type = in.readString();
        this.total_roles = in.readString();
        this.gender_type = in.readString();
        this.product_name = in.readString();
        this.job_duration = in.readString();
        this.role_description = in.readString();
        this.performance_location = in.readString();
        this.performance_from_date = in.readString();
        this.performance_to_date = in.readString();
        this.client_information = in.readString();
        this.skill = in.createStringArrayList();
        this.additional_attachment = in.createStringArrayList();
        this.hair_color = in.createStringArrayList();
        this.eye_color = in.createStringArrayList();
        this.hair_length = in.createStringArrayList();
        this.ethnicity = in.createStringArrayList();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<JobPostdetail_pojo> CREATOR = new Parcelable.Creator<JobPostdetail_pojo>() {
        @Override
        public JobPostdetail_pojo createFromParcel(Parcel source) {
            return new JobPostdetail_pojo(source);
        }

        @Override
        public JobPostdetail_pojo[] newArray(int size) {
            return new JobPostdetail_pojo[size];
        }
    };
}






