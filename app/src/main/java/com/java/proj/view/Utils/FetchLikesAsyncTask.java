package com.java.proj.view.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.java.proj.view.DataBase.LikedDataBase;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.LikesModel;

import java.lang.ref.WeakReference;
import java.util.Map;

public class FetchLikesAsyncTask extends AsyncTask<Void, Void, Map<String, GeneralModel>> {
    private final String TAG = "FetchLikesAsyncTaskTag";
    private final int flag;
    private final WeakReference<Context> contextWeakReference;
    private final LikesModel likesModel;

    public interface FLAGS {
        int POST_TO_LIKES_MODEL = 1;
        int DO_NOT_POST_TO_LIKES_MODEL = 0;
    }

    public FetchLikesAsyncTask(@NonNull Context context, int flag) {
        contextWeakReference = new WeakReference<>(context);
        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
        this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, GeneralModel> doInBackground(Void... voids) {
        if (isCancelled()) {
            return null;
        }
        LikedDataBase likedDataBase = LikedDataBase.getInstance(contextWeakReference.get());
        Map<String, GeneralModel> map = likedDataBase.getGeneralModelList();
        for (GeneralModel generalModel : map.values()) {
            Log.d(TAG, "doInBackground: id = " + generalModel.getImageId() + " likes = " + generalModel.getLikes() );
            likesModel.getLastLikedModel().postValue(generalModel);
        }
        return map;
    }

    @Override
    protected void onPostExecute(Map<String, GeneralModel> map) {
        super.onPostExecute(map);
        if (flag == FLAGS.POST_TO_LIKES_MODEL) {
            likesModel.getCurrentLikedList().setValue(map);
            Log.d(TAG, "onPostExecute: flag is to post");
        }

    }
}
