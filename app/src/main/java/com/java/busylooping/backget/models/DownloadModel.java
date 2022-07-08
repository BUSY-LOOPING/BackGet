package com.java.busylooping.backget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DownloadModel {
    @SerializedName("url")
    @Expose
    private String url;

    public DownloadModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
