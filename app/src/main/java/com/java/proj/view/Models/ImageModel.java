package com.java.proj.view.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("width")
    @Expose
    private String width;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("urls")
    @Expose
    private UriModel urls;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("user")
    @Expose
    private UserModel userModel;

    @SerializedName("links")
    @Expose
    private LinksModel linksModel;

    @SerializedName("liked_by_user")
    private boolean likedByUser;


    public ImageModel(UriModel urls, String id, String description, String likes, UserModel userModel, LinksModel linksModel, boolean likedByUser) {
        this.urls = urls;
        this.id = id;
        this.description = description;
        this.likes = likes;
        this.userModel = userModel;
        this.linksModel = linksModel;
        this.likedByUser = likedByUser;
    }

    public UriModel getUrls() {
        return urls;
    }

    public void setUrls(UriModel urls) {
        this.urls = urls;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public LinksModel getLinksModel() {
        return linksModel;
    }

    public void setLinksModel(LinksModel linksModel) {
        this.linksModel = linksModel;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }
}
