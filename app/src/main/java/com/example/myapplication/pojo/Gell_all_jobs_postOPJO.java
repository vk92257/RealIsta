package com.example.myapplication.pojo;

    import android.os.Parcel;
    import android.os.Parcelable;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class  Gell_all_jobs_postOPJO implements Parcelable {

        private String id;
        private String userId;
        private String roleAgeMin;
        private String roleAgeMax;
        private String heightFeet;
        private String heightInches;
        private String weight;
        private String aboutProject;
        private String aboutPersonality;
        private String jobRatePerDay;
        private List<String> skills;
        private List<String> ethnicity;
        private List<String> hairColor;
        private List<String> eyeColor;
        private List<String> hairLength;
        private String jobTitle;
        private String jobType;
        private String totalRoles;
        private String genderType;
        private String productName;
        private String jobDuration;
        private String roleDescription;
        private String performanceLocation;
        private String performanceFromDate;
        private String performanceToDate;
        private String clientInformation;
        private List<String> additionalAttachment;
        private String createdAt;
        private String updatedAt;
        private String name;
        private String job_id;
        private String status;

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Gell_all_jobs_postOPJO(String id, String userId, String roleAgeMin,
                                      String roleAgeMax, String heightFeet, String heightInches, String weight, String aboutProject,
                                      String aboutPersonality, String jobRatePerDay, List<String> skills, List<String> ethnicity,
                                      List<String> hairColor, List<String> eyeColor, List<String> hairLength, String jobTitle, String jobType,
                                      String totalRoles, String genderType, String productName, String jobDuration, String roleDescription,
                                      String performanceLocation, String performanceFromDate, String performanceToDate, String clientInformation,
                                      List<String> additionalAttachment, String createdAt, String updatedAt) {
            this.id = id;
            this.userId = userId;
            this.roleAgeMin = roleAgeMin;
            this.roleAgeMax = roleAgeMax;
            this.heightFeet = heightFeet;
            this.heightInches = heightInches;
            this.weight = weight;
            this.aboutProject = aboutProject;
            this.aboutPersonality = aboutPersonality;
            this.jobRatePerDay = jobRatePerDay;
            this.skills = skills;
            this.ethnicity = ethnicity;
            this.hairColor = hairColor;
            this.eyeColor = eyeColor;
            this.hairLength = hairLength;
            this.jobTitle = jobTitle;
            this.jobType = jobType;
            this.totalRoles = totalRoles;
            this.genderType = genderType;
            this.productName = productName;
            this.jobDuration = jobDuration;
            this.roleDescription = roleDescription;
            this.performanceLocation = performanceLocation;
            this.performanceFromDate = performanceFromDate;
            this.performanceToDate = performanceToDate;
            this.clientInformation = clientInformation;
            this.additionalAttachment = additionalAttachment;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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

        public String getJobType() {
            return jobType;
        }

        public void setJobType(String jobType) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.userId);
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
            dest.writeString(this.jobType);
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
            dest.writeString(this.createdAt);
            dest.writeString(this.updatedAt);
            dest.writeString(this.name);
            dest.writeString(this.job_id);
            dest.writeString(this.status);
        }

        protected Gell_all_jobs_postOPJO(Parcel in) {
            this.id = in.readString();
            this.userId = in.readString();
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
            this.jobType = in.readString();
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
            this.createdAt = in.readString();
            this.updatedAt = in.readString();
            this.name = in.readString();
            this.job_id = in.readString();
            this.status = in.readString();
        }

        public static final Parcelable.Creator<Gell_all_jobs_postOPJO> CREATOR = new Parcelable.Creator<Gell_all_jobs_postOPJO>() {
            @Override
            public Gell_all_jobs_postOPJO createFromParcel(Parcel source) {
                return new Gell_all_jobs_postOPJO(source);
            }

            @Override
            public Gell_all_jobs_postOPJO[] newArray(int size) {
                return new Gell_all_jobs_postOPJO[size];
            }
        };
    }