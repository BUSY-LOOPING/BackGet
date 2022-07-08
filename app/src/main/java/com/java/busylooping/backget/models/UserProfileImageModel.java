package com.java.busylooping.backget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfileImageModel implements Serializable {
    @SerializedName("small")
    @Expose
    private String small;

    @SerializedName("medium")
    @Expose
    private String medium;

    @SerializedName("large")
    @Expose
    private String large;

    public UserProfileImageModel(String small, String medium, String large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public UserProfileImageModel(UserProfileImageModel userProfileImageModel) {
        this.small = new String(userProfileImageModel.getSmall());
        this.medium = new String(userProfileImageModel.getMedium());
        this.large = new String(userProfileImageModel.getLarge());
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
