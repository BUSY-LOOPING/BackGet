package com.java.busylooping.backget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviewPhotosModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("urls")
    @Expose
    private UriModel uriModel;

    public PreviewPhotosModel() {
        uriModel = new UriModel();
    }

    public PreviewPhotosModel(String id, UriModel uriModel) {
        this.id = id;
        this.uriModel = uriModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UriModel getUriModel() {
        return uriModel;
    }

    public void setUriModel(UriModel uriModel) {
        this.uriModel = uriModel;
    }
}
