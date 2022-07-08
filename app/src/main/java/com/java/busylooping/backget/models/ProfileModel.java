package com.java.busylooping.backget.models;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ProfileModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("downloads")
    @Expose
    private String downloads;

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("profile_image")
    @Expose
    private UserProfileImageModel profileImageModel;

    @SerializedName("social")
    @Expose
    private UserSocialsModel userSocialsModel;

    @SerializedName("for_hire")
    @Expose
    private boolean for_hire;


    public ProfileModel(String id, String username, String name ,String firstName, String lastName, String email, String downloads, String bio, boolean for_hire, UserProfileImageModel profileImageModel, UserSocialsModel userSocialsModel) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.downloads = downloads;
        this.bio = bio;
        this.for_hire = for_hire;
        this.profileImageModel = profileImageModel;
        this.userSocialsModel = userSocialsModel;
    }

    public ProfileModel(ProfileModel profileModel) {
        this.id = profileModel.getId();
        this.username = profileModel.getUsername();
        this.name = profileModel.getName();
        this.firstName = profileModel.getFirstName();
        this.lastName = profileModel.getLastName();
        this.email = profileModel.getEmail();
        this.downloads = profileModel.getDownloads();
        this.bio = profileModel.getBio();
        this.for_hire = profileModel.isFor_hire();
        this.profileImageModel = new UserProfileImageModel(profileModel.getUserProfileImageModel());
        this.userSocialsModel = new UserSocialsModel(profileModel.getUserSocialsModel());
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UserProfileImageModel getUserProfileImageModel() {
        return profileImageModel;
    }

    public void setUserProfileImageModel(UserProfileImageModel profileImageModel) {
        this.profileImageModel = profileImageModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfileImageModel getProfileImageModel() {
        return profileImageModel;
    }

    public void setProfileImageModel(UserProfileImageModel profileImageModel) {
        this.profileImageModel = profileImageModel;
    }

    public UserSocialsModel getUserSocialsModel() {
        return userSocialsModel;
    }

    public void setUserSocialsModel(UserSocialsModel userSocialsModel) {
        this.userSocialsModel = userSocialsModel;
    }

    public boolean isFor_hire() {
        return for_hire;
    }

    public void setFor_hire(boolean for_hire) {
        this.for_hire = for_hire;
    }

    public static void launchTwitterForUserName(Context context, String twitter_user_name) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
        }catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
        }
    }


    public static void launchInstagramForUserName(Context context, String instagram_user_name) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + instagram_user_name);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagram_user_name)));
        }
    }
}
