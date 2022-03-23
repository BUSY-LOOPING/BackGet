package com.java.proj.view.api;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.java.proj.view.CallBacks.GetDataCallBack;
import com.java.proj.view.Models.CollectionModel;
import com.java.proj.view.Models.CollectionsResultModel;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.ImageModel;
import com.java.proj.view.Models.PreviewPhotosModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    public static final String BASE_URL = "https://api.unsplash.com";
    public static final String API_KEY = "1htoE7NrnRRWLcUVutfVVF2uZ2QCCo2mXOmAkGlxr_4";

    public static Retrofit retrofit = null;

    public static ApiInterface getApiInterface() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }

    public static void getCollectionsData(Context context, int page, int pageSize, String query, FrameLayout loadingLayout, RecyclerView.Adapter<?> adapter, ArrayList<GeneralModel> list, @NonNull GetDataCallBack callBack) {

        loadingLayout.setVisibility(View.VISIBLE);
        callBack.isLoading(true);
        ApiUtilities.getApiInterface().searchCollections(page, pageSize, query, "popular")
                .enqueue(new Callback<CollectionModel>() {
                    @Override
                    public void onResponse(@NonNull Call<CollectionModel> call, @NonNull Response<CollectionModel> response) {
                        if (response.body() != null && response.body().getList() != null) {
                            int startIndex = list.size();
                            for (CollectionsResultModel model : response.body().getList()) {
                                List<PreviewPhotosModel> previewPhotosModelArrayList = model.getList();
                                Random random = new Random();
                                int randInt = random.nextInt(previewPhotosModelArrayList.size());
                                PreviewPhotosModel model2 = previewPhotosModelArrayList.get(randInt);
                                list.add(new GeneralModel(model2.getUriModel(), model2.getId(), model.getDescription(), "123", model.getUserModel()));
//                                int pos = 0;
//                                for (PreviewPhotosModel model2 : previewPhotosModelArrayList) {
//                                    UserModel userModel = model.getUserModel();
//                                }
//                                PreviewPhotosModel model2 = previewPhotosModelArrayList.get(pos);
//                                list.add(new GeneralModel(model2.getUriModel(), model2.getId(), model.getDescription(), model.getUserModel()));

//                                for (PreviewPhotosModel model2 : previewPhotosModelArrayList) {
//                                    list.add(new GeneralModel(model2.getUriModel(), model2.getId(), model.getDescription(), model.getUserModel()));
//                                }
                            }

                            adapter.notifyItemRangeInserted(startIndex, list.size() - startIndex);
                            loadingLayout.setVisibility(View.GONE);
                        }
                        callBack.isLoading(false);
                        if (list.size() > 0) {
                            boolean res = list.size() < pageSize;
                            callBack.isLastPage(res);
                        } else
                            callBack.isLastPage(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CollectionModel> call, @NonNull Throwable t) {
                        Log.d("myerr", t.toString());
                        callBack.isLoading(false);
                        loadingLayout.setVisibility(View.GONE);
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void getTopicsData(Context context, int page, int pageSize, String topicNameOrId, View loadingLayout, RecyclerView.Adapter<?> adapter, ArrayList<GeneralModel> list, @NonNull GetDataCallBack callBack) {
        loadingLayout.setVisibility(View.VISIBLE);
        callBack.isLoading(true);
        ApiUtilities.getApiInterface().getTopicImages(topicNameOrId, page, pageSize, "popular")
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ImageModel>> call, @NonNull Response<List<ImageModel>> response) {
                        if (response.body() != null) {
                            int startIndex = list.size();
                            for (ImageModel imageModel : response.body()) {
                                list.add(new GeneralModel(imageModel.getUrls(), imageModel.getId(), imageModel.getDescription(), imageModel.getLikes(), imageModel.getUserModel()));
                            }
                            adapter.notifyItemRangeInserted(startIndex, list.size() - startIndex);
                            loadingLayout.setVisibility(View.GONE);
                        }
                        callBack.isLoading(false);
                        if (list.size() > 0) {
                            boolean res = list.size() < pageSize;
                            callBack.isLastPage(res);
                        } else
                            callBack.isLastPage(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ImageModel>> call, @NonNull Throwable t) {
                        Log.d("myerr", t.toString());
                        callBack.isLoading(false);
                        loadingLayout.setVisibility(View.GONE);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
