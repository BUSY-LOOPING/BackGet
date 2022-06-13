package com.java.proj.view.Models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;
import java.util.Objects;

public class GeneralModel extends BaseObservable implements Serializable {
    private UriModel uriModel;
    private String imageId;
    private String description;
    private UserModel userModel;
    private String likes;
    private LinksModel linksModel;
    private boolean isLiked;

    public GeneralModel(UriModel uriModel, String imageId, String description, String likes, UserModel userModel, LinksModel linksModel, boolean isLikedByUser) {
        this.uriModel = uriModel;
        this.imageId = imageId;
        this.description = description;
        this.likes = likes;
        this.userModel = userModel;
        this.linksModel = linksModel;
        this.isLiked = isLikedByUser;
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
//        if (isLiked != liked) {
//            if (isLiked)
//                likes = "" + (Integer.parseInt(likes) - 1);
//            else
//                likes = "" + (Integer.parseInt(likes) + 1);
//            isLiked = liked;
//        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralModel that = (GeneralModel) o;
        return getImageId().equals(that.getImageId());
    }

    public LinksModel getLinksModel() {
        return linksModel;
    }

    public void setLinksModel(LinksModel linksModel) {
        this.linksModel = linksModel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageId());
    }
}
