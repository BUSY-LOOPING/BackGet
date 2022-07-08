package com.java.busylooping.backget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchModel {
    @SerializedName("results")
    @Expose
    private ArrayList<ImageModel> result;

    public SearchModel(ArrayList<ImageModel> result) {
        this.result = result;
    }


    public ArrayList<ImageModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<ImageModel> result) {
        this.result = result;
    }
}
