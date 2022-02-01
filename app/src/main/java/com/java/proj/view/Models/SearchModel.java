package com.java.proj.view.Models;

import java.util.ArrayList;

public class SearchModel {
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
