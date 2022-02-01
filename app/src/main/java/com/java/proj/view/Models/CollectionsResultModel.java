package com.java.proj.view.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * this is the object that is inside 'results' list
 */
public class CollectionsResultModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("preview_photos")
    @Expose
    private List<PreviewPhotosModel> list;

    @SerializedName("user")
    @Expose
    private UserModel userModel;

    @SerializedName("description")
    @Expose
    private String description;

    public CollectionsResultModel() {
        list = new ArrayList<>();
    }

    public CollectionsResultModel(String id, String description, List<PreviewPhotosModel> list, UserModel userModel) {
        this.id = id;
        this.description = description;
        this.list = list;
        this.userModel = userModel;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PreviewPhotosModel> getList() {
        return list;
    }

    public void setList(ArrayList<PreviewPhotosModel> list) {
        this.list = list;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
