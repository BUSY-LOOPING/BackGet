package com.java.busylooping.backget.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserSocialsModel implements Serializable {

    @SerializedName("instagram_username")
    @Expose
    @Nullable
    private String instagram_username;

    @SerializedName("portfolio_url")
    @Expose
    @Nullable
    private String portfolio_url;

    @SerializedName("twitter_username")
    @Expose
    @Nullable
    private String twitter_username;

    public UserSocialsModel(@Nullable String instagram_username, @Nullable String portfolio_url, @Nullable String twitter_username) {
        this.instagram_username = instagram_username;
        this.portfolio_url = portfolio_url;
        this.twitter_username = twitter_username;
    }

    public UserSocialsModel (UserSocialsModel userSocialsModel) {
        this.instagram_username = userSocialsModel.getInstagram_username();
        this.portfolio_url = userSocialsModel.getPortfolio_url();
        this.twitter_username = userSocialsModel.getTwitter_username();
    }

    @Nullable
    public String getInstagram_username() {
        return instagram_username;
    }

    public void setInstagram_username(@Nullable String instagram_username) {
        this.instagram_username = instagram_username;
    }

    @Nullable
    public String getPortfolio_url() {
        return portfolio_url;
    }

    public void setPortfolio_url(@Nullable String portfolio_url) {
        this.portfolio_url = portfolio_url;
    }

    @Nullable
    public String getTwitter_username() {
        return twitter_username;
    }

    public void setTwitter_username(@Nullable String twitter_username) {
        this.twitter_username = twitter_username;
    }
}
