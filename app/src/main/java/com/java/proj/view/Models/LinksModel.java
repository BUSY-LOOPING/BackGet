package com.java.proj.view.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LinksModel implements Serializable {
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
