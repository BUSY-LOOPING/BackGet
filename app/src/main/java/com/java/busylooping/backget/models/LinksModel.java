package com.java.busylooping.backget.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LinksModel implements Serializable {
    @Nullable
    @SerializedName("download_location")
    @Expose
    private String downloadLocation;

    public LinksModel(@Nullable String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }

    public LinksModel(@Nullable LinksModel linksModel) {
        if (linksModel == null)
            this.downloadLocation = null;
        else
            this.downloadLocation = linksModel.getDownloadLocation();
    }

    @Nullable
    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(@Nullable String downloadLocation) {
        this.downloadLocation = downloadLocation;
    }
}
