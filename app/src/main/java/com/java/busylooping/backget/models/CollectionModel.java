package com.java.busylooping.backget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CollectionModel {
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("total_pages")
    @Expose
    private int total_pages;
    @SerializedName("results")
    @Expose
    private List<CollectionsResultModel> list;

    public CollectionModel(int total, int total_pages, List<CollectionsResultModel> list) {
        this.total = total;
        this.total_pages = total_pages;
        this.list = list;
    }

    public CollectionModel(ArrayList<CollectionsResultModel> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<CollectionsResultModel> getList() {
        return list;
    }

    public void setList(ArrayList<CollectionsResultModel> list) {
        this.list = list;
    }
}
