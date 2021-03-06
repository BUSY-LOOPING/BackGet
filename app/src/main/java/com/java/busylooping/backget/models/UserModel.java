package com.java.busylooping.backget.models;

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

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("profile_image")
    @Expose
    private UserProfileImageModel userProfileImageModel;

    public UserModel(String id, String name, String bio,String username ,UserProfileImageModel userProfileImageModel) {
        this.id = id;
        this.name = name;
        this.bio =  bio;
        this.username = username;
        this.userProfileImageModel = userProfileImageModel;
    }

    public UserModel(UserModel userModel) {
        this.id = id;
        this.name = name;
        this.bio =  bio;
        this.username = username;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
