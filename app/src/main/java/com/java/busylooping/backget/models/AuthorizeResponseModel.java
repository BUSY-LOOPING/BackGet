package com.java.busylooping.backget.models;

public class AuthorizeResponseModel {
    private String response;

    public AuthorizeResponseModel(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
