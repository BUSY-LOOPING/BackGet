package com.java.busylooping.backget.models;


import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observable;

public class GeneralModel extends Observable implements Serializable {
    private UriModel uriModel;

    private String imageId;

    @Nullable
    private String description;

    private ProfileModel profileModel;

    private String likes;

    private String views;

    @Nullable
    private LinksModel linksModel;

    private boolean isLiked;

    public GeneralModel(UriModel uriModel, String imageId, @Nullable String description, String likes, ProfileModel profileModel, @Nullable LinksModel linksModel, boolean isLikedByUser, String views) {
        this.uriModel = uriModel;
        this.imageId = imageId;
        this.description = description;
        this.likes = likes;
        this.profileModel = profileModel;
        this.linksModel = linksModel;
        this.isLiked = isLikedByUser;
        this.views = views;
    }

    public GeneralModel(GeneralModel generalModel) {
        this.uriModel = new UriModel(generalModel.getUriModel());
        this.imageId = generalModel.getImageId();
        this.description = generalModel.getDescription();
        this.likes = generalModel.getLikes();
        this.profileModel = new ProfileModel(generalModel.getProfileModel());
        this.linksModel = new LinksModel(generalModel.getLinksModel());
        this.isLiked = generalModel.isLiked;
        this.views = generalModel.getViews();
    }

    //    @Bindable
    public UriModel getUriModel() {
        return uriModel;
    }

    public void setUriModel(UriModel uriModel) {
        this.uriModel = uriModel;
    }

    //    @Bindable
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }


    //    @Bindable

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    //    @Bindable
    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    //    @Bindable
    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

//    @Bindable
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
        setChanged();
//        notifyPropertyChanged(BR.isLiked);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralModel that = (GeneralModel) o;
        return getImageId().equals(that.getImageId());
    }

    @Nullable
    public LinksModel getLinksModel() {
        return linksModel;
    }

    public void setLinksModel(@Nullable LinksModel linksModel) {
        this.linksModel = linksModel;
    }

    //    @Bindable
    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageId());
    }
}
