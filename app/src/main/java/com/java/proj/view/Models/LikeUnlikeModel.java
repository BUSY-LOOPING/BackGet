package com.java.proj.view.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeUnlikeModel {
    @SerializedName("photo")
    @Expose
    private PhotoModel photoModel;

    public LikeUnlikeModel(PhotoModel photoModel) {
        this.photoModel = photoModel;
    }

    public PhotoModel getPhotoModel() {
        return photoModel;
    }

    public void setPhotoModel(PhotoModel photoModel) {
        this.photoModel = photoModel;
    }
}
