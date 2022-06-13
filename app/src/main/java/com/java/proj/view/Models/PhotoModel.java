package com.java.proj.view.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("liked_by_user")
    @Expose
    private String likedByUser;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("urls")
    @Expose
    private UriModel uriModel;

    public PhotoModel(String id, String likes, String likedByUser, String description, UriModel uriModel) {
        this.id = id;
        this.likes = likes;
        this.likedByUser = likedByUser;
        this.description = description;
        this.uriModel = uriModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(String likedByUser) {
        this.likedByUser = likedByUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UriModel getUriModel() {
        return uriModel;
    }

    public void setUriModel(UriModel uriModel) {
        this.uriModel = uriModel;
    }
}
