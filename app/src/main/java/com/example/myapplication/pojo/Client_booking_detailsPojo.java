package com.example.myapplication.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Client_booking_detailsPojo implements Parcelable {

    private String contractId;
    private String status;
    private String id;
    private String userId;
    private String jobSaved;
    private String roleAgeMin;
    private String roleAgeMax;
    private String heightFeet;
    private String heightInches;
    private String weight;
    private String aboutProject;
    private String aboutPersonality;
    private String jobRatePerDay;
    private List<String> skills;
    private List<String>  ethnicity;
    private List<String>  hairColor;
    private List<String>  eyeColor;
    private List<String>  hairLength;
    private String jobTitle;
    private List<String>  jobType;
    private String totalRoles;
    private String genderType;
    private String productName;
    private String jobDuration;
    private String roleDescription;
    private String performanceLocation;
    private String performanceFromDate;
    private String performanceToDate;
    private String clientInformation;
    private List<String>  additionalAttachment;
    private String draft;
    private String createdAt;
    private String updatedAt;
    private String name;
    private String talentId;
    private String profile_img;
    private String final_price;

    public Client_booking_detailsPojo() {
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobSaved() {
        return jobSaved;
    }

    public void setJobSaved(String jobSaved) {
        this.jobSaved = jobSaved;
    }

    public String getRoleAgeMin() {
        return roleAgeMin;
    }

    public void setRoleAgeMin(String roleAgeMin) {
        this.roleAgeMin = roleAgeMin;
    }

    public String getRoleAgeMax() {
        return roleAgeMax;
    }

    public void setRoleAgeMax(String roleAgeMax) {
        this.roleAgeMax = roleAgeMax;
    }

    public String getHeightFeet() {
        return heightFeet;
    }

    public void setHeightFeet(String heightFeet) {
        this.heightFeet = heightFeet;
    }

    public String getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(String heightInches) {
        this.heightInches = heightInches;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAboutProject() {
        return aboutProject;
    }

    public void setAboutProject(String aboutProject) {
        this.aboutProject = aboutProject;
    }

    public String getAboutPersonality() {
        return aboutPersonality;
    }

    public void setAboutPersonality(String aboutPersonality) {
        this.aboutPersonality = aboutPersonality;
    }

    public String getJobRatePerDay() {
        return jobRatePerDay;
    }

    public void setJobRatePerDay(String jobRatePerDay) {
        this.jobRatePerDay = jobRatePerDay;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(List<String> ethnicity) {
        this.ethnicity = ethnicity;
    }

    public List<String> getHairColor() {
        return hairColor;
    }

    public void setHairColor(List<String> hairColor) {
        this.hairColor = hairColor;
    }

    public List<String> getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(List<String> eyeColor) {
        this.eyeColor = eyeColor;
    }

    public List<String> getHairLength() {
        return hairLength;
    }

    public void setHairLength(List<String> hairLength) {
        this.hairLength = hairLength;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<String>  getJobType() {
        return jobType;
    }

    public void setJobType(List<String>  jobType) {
        this.jobType = jobType;
    }

    public String getTotalRoles() {
        return totalRoles;
    }

    public void setTotalRoles(String totalRoles) {
        this.totalRoles = totalRoles;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(String jobDuration) {
        this.jobDuration = jobDuration;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getPerformanceLocation() {
        return performanceLocation;
    }

    public void setPerformanceLocation(String performanceLocation) {
        this.performanceLocation = performanceLocation;
    }

    public String getPerformanceFromDate() {
        return performanceFromDate;
    }

    public void setPerformanceFromDate(String performanceFromDate) {
        this.performanceFromDate = performanceFromDate;
    }

    public String getPerformanceToDate() {
        return performanceToDate;
    }

    public void setPerformanceToDate(String performanceToDate) {
        this.performanceToDate = performanceToDate;
    }

    public String getClientInformation() {
        return clientInformation;
    }

    public void setClientInformation(String clientInformation) {
        this.clientInformation = clientInformation;
    }

    public List<String> getAdditionalAttachment() {
        return additionalAttachment;
    }

    public void setAdditionalAttachment(List<String> additionalAttachment) {
        this.additionalAttachment = additionalAttachment;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
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

    public String getTalentId() {
        return talentId;
    }

    public void setTalentId(String talentId) {
        this.talentId = talentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contractId);
        dest.writeString(this.status);
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.jobSaved);
        dest.writeString(this.roleAgeMin);
        dest.writeString(this.roleAgeMax);
        dest.writeString(this.heightFeet);
        dest.writeString(this.heightInches);
        dest.writeString(this.weight);
        dest.writeString(this.aboutProject);
        dest.writeString(this.aboutPersonality);
        dest.writeString(this.jobRatePerDay);
        dest.writeStringList(this.skills);
        dest.writeStringList(this.ethnicity);
        dest.writeStringList(this.hairColor);
        dest.writeStringList(this.eyeColor);
        dest.writeStringList(this.hairLength);
        dest.writeString(this.jobTitle);
        dest.writeStringList(this.jobType);
        dest.writeString(this.totalRoles);
        dest.writeString(this.genderType);
        dest.writeString(this.productName);
        dest.writeString(this.jobDuration);
        dest.writeString(this.roleDescription);
        dest.writeString(this.performanceLocation);
        dest.writeString(this.performanceFromDate);
        dest.writeString(this.performanceToDate);
        dest.writeString(this.clientInformation);
        dest.writeStringList(this.additionalAttachment);
        dest.writeString(this.draft);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.name);
        dest.writeString(this.talentId);
        dest.writeString(this.profile_img);
        dest.writeString(this.final_price);
    }

    protected Client_booking_detailsPojo(Parcel in) {
        this.contractId = in.readString();
        this.status = in.readString();
        this.id = in.readString();
        this.userId = in.readString();
        this.jobSaved = in.readString();
        this.roleAgeMin = in.readString();
        this.roleAgeMax = in.readString();
        this.heightFeet = in.readString();
        this.heightInches = in.readString();
        this.weight = in.readString();
        this.aboutProject = in.readString();
        this.aboutPersonality = in.readString();
        this.jobRatePerDay = in.readString();
        this.skills = in.createStringArrayList();
        this.ethnicity = in.createStringArrayList();
        this.hairColor = in.createStringArrayList();
        this.eyeColor = in.createStringArrayList();
        this.hairLength = in.createStringArrayList();
        this.jobTitle = in.readString();
        this.jobType = in.createStringArrayList();
        this.totalRoles = in.readString();
        this.genderType = in.readString();
        this.productName = in.readString();
        this.jobDuration = in.readString();
        this.roleDescription = in.readString();
        this.performanceLocation = in.readString();
        this.performanceFromDate = in.readString();
        this.performanceToDate = in.readString();
        this.clientInformation = in.readString();
        this.additionalAttachment = in.createStringArrayList();
        this.draft = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.name = in.readString();
        this.talentId = in.readString();
        this.profile_img = in.readString();
        this.final_price = in.readString();
    }

    public static final Parcelable.Creator<Client_booking_detailsPojo> CREATOR = new Parcelable.Creator<Client_booking_detailsPojo>() {
        @Override
        public Client_booking_detailsPojo createFromParcel(Parcel source) {
            return new Client_booking_detailsPojo(source);
        }

        @Override
        public Client_booking_detailsPojo[] newArray(int size) {
            return new Client_booking_detailsPojo[size];
        }
    };
}
