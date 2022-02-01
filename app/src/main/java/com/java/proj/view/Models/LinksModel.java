package com.java.proj.view.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinksModel {
    @SerializedName("download_location")
    @Expose
    private String downloadLocation;

    public LinksModel(String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }
}
