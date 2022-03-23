package com.java.proj.view.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class UserModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_image")
    @Expose
    private UserProfileImageModel userProfileImageModel;

    public UserModel(String id, String name, UserProfileImageModel userProfileImageModel) {
        this.id = id;
        this.name = name;
        this.userProfileImageModel = userProfileImageModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfileImageModel getUserProfileImageModel() {
        return userProfileImageModel;
    }

    public void setUserProfileImageModel(UserProfileImageModel userProfileImageModel) {
        this.userProfileImageModel = userProfileImageModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return getId().equals(userModel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
