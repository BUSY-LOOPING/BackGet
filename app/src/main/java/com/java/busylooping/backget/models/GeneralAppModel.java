package com.java.busylooping.backget.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GeneralAppModel extends ViewModel {
    private MutableLiveData<String> accessTokenLiveData;
    private MutableLiveData<ArrayList<DownloadGeneralModel>> downloadLiveData = null;

    public void setAccessTokenLiveData(String accessToken) {
        if (accessTokenLiveData == null)
            accessTokenLiveData = new MutableLiveData<>();
        accessTokenLiveData.setValue(accessToken);
    }

    @NonNull
    public MutableLiveData<String> getAccessTokenLiveData() {
        if (accessTokenLiveData == null)
            accessTokenLiveData = new MutableLiveData<>();
        return accessTokenLiveData;
    }

    @Nullable
    public MutableLiveData<ArrayList<DownloadGeneralModel>> getDownloadLiveData() {
        return downloadLiveData;
    }

    public void addToList(@Nullable DownloadGeneralModel... models) {
        if (models != null) {
            for (DownloadGeneralModel model : models) {
                if (model != null) {

                }
            }
        }
    }
}
