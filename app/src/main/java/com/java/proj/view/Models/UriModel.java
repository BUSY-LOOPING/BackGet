package com.java.proj.view.Models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class UriModel implements Serializable {
    @SerializedName("regular")
    @Expose
    private String regular;
    @SerializedName("full")
    @Expose
    private String full;
    @SerializedName("raw")
    @Expose
    private String raw;

    public UriModel() {

    }

    public UriModel(String regular, String full, String raw) {
        this.regular = regular;
        this.full = full;
        this.raw = raw;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass())
            return false;

        if (obj == this) {
            return true;
        }
        UriModel uriModel = (UriModel) obj;
        return this.getRegular().equals(uriModel.getRegular())
                && this.getRaw().equals(uriModel.getRaw())
                && this.getFull().equals(uriModel.getFull());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegular(), getFull(), getRaw());
    }
}
