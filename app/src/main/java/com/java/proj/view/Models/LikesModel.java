package com.java.proj.view.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.java.proj.view.DataBase.LikedDataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikesModel extends ViewModel {
    private static final String TAG = "MyLikesModel";
    private MutableLiveData<Map<String,GeneralModel>> currentLikedList;
    private MutableLiveData<GeneralModel> lastLikedModel;
    private MutableLiveData<GeneralModel> lastUnLikedModel;

    @NonNull
    public MutableLiveData<Map<String, GeneralModel>> getCurrentLikedList() {
        if (currentLikedList == null) {
            currentLikedList = new MutableLiveData<>();
            currentLikedList.setValue(new HashMap<>());
        }
        return currentLikedList;
    }

    @NonNull
    public MutableLiveData<GeneralModel> getLastLikedModel() {
        if (lastLikedModel == null) {
            lastLikedModel = new MutableLiveData<>();
        }
        return lastLikedModel;
    }

    @NonNull
    public MutableLiveData<GeneralModel> getLastUnLikedModel() {
        if (lastUnLikedModel == null) {
            lastUnLikedModel = new MutableLiveData<>();
        }
        return lastUnLikedModel;
    }

    public void addModel(GeneralModel model, boolean notifyMainList) {
        if (getCurrentLikedList().getValue() != null) {
            getCurrentLikedList().getValue().put(model.getImageId(), model);
            if (notifyMainList)
                getCurrentLikedList().setValue(getCurrentLikedList().getValue());
            getLastLikedModel().setValue(model);
        }
    }

    public void removeModel(GeneralModel model, boolean notifyMainList) {
        if (getCurrentLikedList().getValue() != null) {
            getCurrentLikedList().getValue().remove(model.getImageId());
            if (notifyMainList)
                getCurrentLikedList().setValue(getCurrentLikedList().getValue());
            getLastUnLikedModel().setValue(model);
        }
    }


    public static class InsertModelAsyncTask extends AsyncTask<GeneralModel, Void, Void> {
        private final LikedDataBase dataBase;
        private final MutableLiveData<List<GeneralModel>> currentLikedList;

        @SuppressWarnings("deprecation")
        public InsertModelAsyncTask(@NonNull Context context, MutableLiveData<List<GeneralModel>> currentLikedList) {
            this.currentLikedList = currentLikedList;
            dataBase = LikedDataBase.getInstance(context);
        }

        @Override
        protected Void doInBackground(GeneralModel... generalModels) {
            dataBase.likePicture(generalModels[0]);
            if (currentLikedList.getValue() != null) {
                Log.d(TAG, "doInBackground: insert result = " + currentLikedList.getValue().add(generalModels[0]));
                currentLikedList.postValue(currentLikedList.getValue());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dataBase.close();
        }
    }

    public static class DeleteModelAsyncTask extends AsyncTask<GeneralModel, Void, Void> {
        private final LikedDataBase dataBase;
        private final MutableLiveData<List<GeneralModel>> currentLikedList;

        @SuppressWarnings("deprecation")
        public DeleteModelAsyncTask(@NonNull Context context, MutableLiveData<List<GeneralModel>> currentLikedList) {
            dataBase = LikedDataBase.getInstance(context);
            this.currentLikedList = currentLikedList;
        }

        @Override
        protected Void doInBackground(GeneralModel... generalModels) {
            dataBase.unlikePicture(generalModels[0]);
            if (currentLikedList.getValue() != null) {
                currentLikedList.getValue().remove(generalModels[0]);
                currentLikedList.postValue(currentLikedList.getValue());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dataBase.close();
        }
    }
}
