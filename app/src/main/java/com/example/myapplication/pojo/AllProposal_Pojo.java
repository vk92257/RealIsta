package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AllProposal_Pojo implements Parcelable {
    private String id;
    private String jobId;
    private String userId;
    private String purposalRate;
    private String cover_letter;
    private List<String> attachment;
    private String invitation;
    private String createdAt;
    private String updatedAt;
    private String name;
    private String city;
    private String state;
    private String country;
    private List<String> skills;
    private String proffesion;
    private String profile_img;


//
//    public AllProposal_Pojo(String id, String jobId, String userId,
//                            String purposalRate, String attachment,
//                            String invitation, String createdAt,
//                            String updatedAt, String name, String city,
//                            String state, String country, String skills,
//                            String proffesion, String profile_img, String cover_letter) {
//        this.id = id;
//        this.jobId = jobId;
//        this.userId = userId;
//        this.purposalRate = purposalRate;
//        this.attachment = attachment;
//        this.invitation = invitation;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.name = name;
//        this.city = city;
//        this.state = state;
//        this.country = country;
//        this.skills = skills;
//        this.proffesion = proffesion;
//        this.profile_img = profile_img;
//        this.cover_letter = cover_letter;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurposalRate() {
        return purposalRate;
    }

    public void setPurposalRate(String purposalRate) {
        this.purposalRate = purposalRate;
    }

    public String getCover_letter() {
        return cover_letter;
    }

    public void setCover_letter(String cover_letter) {
        this.cover_letter = cover_letter;
    }

    public List<String> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<String> attachment) {
        this.attachment = attachment;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getProffesion() {
        return proffesion;
    }

    public void setProffesion(String proffesion) {
        this.proffesion = proffesion;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.jobId);
        dest.writeString(this.userId);
        dest.writeString(this.purposalRate);
        dest.writeString(this.cover_letter);
        dest.writeStringList(this.attachment);
        dest.writeString(this.invitation);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.name);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeStringList(this.skills);
        dest.writeString(this.proffesion);
        dest.writeString(this.profile_img);
    }

    public AllProposal_Pojo() {
    }

    protected AllProposal_Pojo(Parcel in) {
        this.id = in.readString();
        this.jobId = in.readString();
        this.userId = in.readString();
        this.purposalRate = in.readString();
        this.cover_letter = in.readString();
        this.attachment = in.createStringArrayList();
        this.invitation = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.name = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.skills = in.createStringArrayList();
        this.proffesion = in.readString();
        this.profile_img = in.readString();
    }

    public static final Parcelable.Creator<AllProposal_Pojo> CREATOR = new Parcelable.Creator<AllProposal_Pojo>() {
        @Override
        public AllProposal_Pojo createFromParcel(Parcel source) {
            return new AllProposal_Pojo(source);
        }

        @Override
        public AllProposal_Pojo[] newArray(int size) {
            return new AllProposal_Pojo[size];
        }
    };
}
