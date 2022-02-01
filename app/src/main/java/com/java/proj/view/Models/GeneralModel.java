package com.java.proj.view.Models;

import java.io.Serializable;

public class GeneralModel implements Serializable {
    private UriModel uriModel;
    private String imageId;
    private String description;
    private UserModel userModel;
    private String likes;

    public GeneralModel(UriModel uriModel, String imageId, String description, String likes, UserModel userModel) {
        this.uriModel = uriModel;
        this.imageId = imageId;
        this.description = description;
        this.likes = likes;
        this.userModel = userModel;
    }

    public UriModel getUriModel() {
        return uriModel;
    }

    public void setUriModel(UriModel uriModel) {
        this.uriModel = uriModel;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
