package com.java.busylooping.backget.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.java.busylooping.backget.AsyncTasks.BackGroundInvoke;
import com.java.busylooping.backget.CallBacks.GetDataCallBack;
import com.java.busylooping.backget.DataBase.LikedDataBase;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.ImageModel;
import com.java.busylooping.backget.models.LikesModel;
import com.java.busylooping.backget.models.SearchModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    public static final String BASE_URL = "https://api.unsplash.com";
    public static final String BASE_URL2 = "https://unsplash.com";
    public static final String API_KEY = "1htoE7NrnRRWLcUVutfVVF2uZ2QCCo2mXOmAkGlxr_4";
    public static final String API_SECRET = "7vmWOsxz-qUbnjCMN3TjnVUa53JvaViGhbu3W7XSQpo";
    public static final String OAUTH_URL = "https://unsplash.com/oauth/authorize";
    public static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    public static final String REDIRECT_URI2 = "futurestudio://callback";
    public static final String SCOPE = "public+write_likes+read_user";

    public static final String SHARED_PREF_NAME = "access_token_preference";
    public static final String ACCESS_TOKEN = "logged_user_access_token";

    public static Retrofit retrofit = null;
    public static Retrofit retrofitWithBearerAccessToken = null;
    public static WeakReference<Retrofit> baseUrl2Retrofit = null;

    public static ApiInterface getApiInterface(@Nullable String accessToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (accessToken != null) {
            httpClient.addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                    Log.d("mylog", "intercepted");
                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + accessToken).build();
                    return chain.proceed(request);
                }
            });
        }
        if (retrofitWithBearerAccessToken == null) {
            retrofitWithBearerAccessToken = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .client(httpClient.build())
                    .build();
        }
        return retrofitWithBearerAccessToken.create(ApiInterface.class);
    }

    public static ApiInterface getApiInterface() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }


    public static ApiInterface getApiInterfaceForBase2() {
        if (baseUrl2Retrofit == null || baseUrl2Retrofit.get() == null) {
            baseUrl2Retrofit = new WeakReference<>(new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build());
        }
        return baseUrl2Retrofit.get().create(ApiInterface.class);
    }


    public static void getCollectionsData(Context context, int page, int pageSize, String query, FrameLayout loadingLayout, RecyclerView.Adapter<?> adapter, ArrayList<GeneralModel> list, @NonNull GetDataCallBack callBack) {

//        loadingLayout.setVisibility(View.VISIBLE);
//        callBack.isLoading(true);
//        ApiUtilities.getApiInterface().searchCollections(page, pageSize, query, "popular")
//                .enqueue(new Callback<CollectionModel>() {
//                    @Override
//                    public void onResponse(@NonNull Call<CollectionModel> call, @NonNull Response<CollectionModel> response) {
//                        if (response.body() != null && response.body().getList() != null) {
//                            int startIndex = list.size();
//                            for (CollectionsResultModel model : response.body().getList()) {
//                                List<PreviewPhotosModel> previewPhotosModelArrayList = model.getList();
//                                Random random = new Random();
//                                int randInt = random.nextInt(previewPhotosModelArrayList.size());
//                                PreviewPhotosModel model2 = previewPhotosModelArrayList.get(randInt);
//                                list.add(new GeneralModel(
//                                        model2.getUriModel(),
//                                        model2.getId(),
//                                        model.getDescription(),
//                                        "123",
//                                        model.getUserModel(), null,
//                                        false,
//                                        ""));
////                                int pos = 0;
////                                for (PreviewPhotosModel model2 : previewPhotosModelArrayList) {
////                                    UserModel userModel = model.getUserModel();
////                                }
////                                PreviewPhotosModel model2 = previewPhotosModelArrayList.get(pos);
////                                list.add(new GeneralModel(model2.getUriModel(), model2.getId(), model.getDescription(), model.getUserModel()));
//
////                                for (PreviewPhotosModel model2 : previewPhotosModelArrayList) {
////                                    list.add(new GeneralModel(model2.getUriModel(), model2.getId(), model.getDescription(), model.getUserModel()));
////                                }
//                            }
//
//                            adapter.notifyItemRangeInserted(startIndex, list.size() - startIndex);
//                            loadingLayout.setVisibility(View.GONE);
//                        }
//                        callBack.isLoading(false);
//                        if (list.size() > 0) {
//                            boolean res = list.size() < pageSize;
//                            callBack.isLastPage(res);
//                        } else
//                            callBack.isLastPage(true);
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<CollectionModel> call, @NonNull Throwable t) {
//                        Log.d("myerr", t.toString());
//                        callBack.isLoading(false);
//                        loadingLayout.setVisibility(View.GONE);
//                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public static void getTopicsData(Context context, int page, int pageSize, String topicNameOrId, View loadingLayout, RecyclerView.Adapter<?> adapter, ArrayList<GeneralModel> list, LikesModel likesModel, @NonNull GetDataCallBack callBack) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadingLayout.setVisibility(View.VISIBLE);
            }
        });
        callBack.isLoading(true);

        ApiUtilities.getApiInterface().getTopicImages(topicNameOrId, page, pageSize, "latest")
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ImageModel>> call, @NonNull Response<List<ImageModel>> response) {
                        BackGroundInvoke<Void, Void, Void> backGroundInvoke = new BackGroundInvoke<>(new BackGroundInvoke.Command() {
                            @Override
                            public void execute(Object[] data) {
                                if (response.body() != null) {
                                    int startIndex = list.size();
                                    addToListAndCheck(context, list, response.body(), likesModel);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                    adapter.notifyItemRangeInserted(startIndex, list.size() - startIndex);
                                            loadingLayout.setVisibility(View.GONE);
                                        }
                                    });

                                }
                                callBack.isLoading(false);
                                if (list.size() > 0) {
                                    boolean res = list.size() < pageSize;
                                    callBack.isLastPage(res);
                                } else
                                    callBack.isLastPage(true);
                            }
                        }, context);
                        backGroundInvoke.execute();
                    }


                    @Override
                    public void onFailure(@NonNull Call<List<ImageModel>> call, @NonNull Throwable t) {
                        Log.d("myerr", t.toString());
                        callBack.isLoading(false);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingLayout.setVisibility(View.GONE);
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


    }

    public static void getSearchData(Context context, int page, int pageSize, String query, String order_by, RecyclerView.Adapter<?> adapter, ArrayList<GeneralModel> list, LikesModel likesModel, @NonNull GetDataCallBack callBack) {
        callBack.isLoading(true);
        ApiUtilities.getApiInterface().searchImages(query, page, pageSize, order_by)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SearchModel> call, @NonNull Response<SearchModel> response) {
                        BackGroundInvoke<Void, Void, Void> backGroundInvoke = new BackGroundInvoke<>(new BackGroundInvoke.Command() {
                            @Override
                            public void execute(Object[] data) {
                                if (response.body() != null) {
                                    int startIndex = list.size();
                                    SearchModel searchModel = response.body();
                                    addToListAndCheck(context, list, searchModel.getResult(), likesModel);
//                            for (ImageModel imageModel : searchModel.getResult()) {
//                                GeneralModel generalModel = new GeneralModel(
//                                        imageModel.getUrls(),
//                                        imageModel.getId(),
//                                        imageModel.getDescription(),
//                                        imageModel.getLikes(),
//                                        imageModel.getUserModel(),
//                                        imageModel.getLinksModel(),
//                                        imageModel.isLikedByUser(),
//                                        imageModel.getViews());
//                                list.add(generalModel);
//
//
//                                if (likesModel.getCurrentLikedList().getValue() != null && likesModel.getCurrentLikedList().getValue().containsKey(generalModel.getImageId())) {
//                                    likesModel.getLastLikedModel().setValue(generalModel);
//                                }
//                            }

                                    adapter.notifyItemRangeInserted(startIndex, list.size() - startIndex);
                                    callBack.isLoading(false);
                                    if (list.size() > 0) {
                                        boolean res = list.size() < pageSize;
                                        callBack.isLastPage(res);
                                    } else
                                        callBack.isLastPage(true);
                                }
                            }
                        }, context);

                    }

                    @Override
                    public void onFailure(@NonNull Call<SearchModel> call, @NonNull Throwable t) {
                        Log.d("myerr", t.toString());
                        callBack.isLoading(false);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void getListImageModel(@NonNull Context context, Call<List<ImageModel>> call, ArrayList<GeneralModel> list, RecyclerView.Adapter<?> adapter, int pageSize, @Nullable LikesModel likesModel, @NonNull GetDataCallBack callBack) {
        long start = System.currentTimeMillis();
        Handler handler = new Handler(Looper.getMainLooper());
        callBack.isLoading(true);
        call.enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ImageModel>> call, @NonNull Response<List<ImageModel>> response) {
                BackGroundInvoke<Void, Void, Void> backGroundInvoke = new BackGroundInvoke<>(new BackGroundInvoke.Command() {
                    @Override
                    public void execute(Object[] data) {
                        if (response.body() != null) {
                            int startIndex = list.size();
                            addToListAndCheck(context, list, response.body(), likesModel);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemRangeInserted(startIndex, response.body().size());
                                }
                            });
                            callBack.isLoading(false);
                        }
                        callBack.isLoading(false);
                        if (list.size() > 0) {
                            boolean res = list.size() < pageSize;
                            callBack.isLastPage(res);
                        } else callBack.isLastPage(true);
                    }
                }, context);
                backGroundInvoke.execute();
            }

            @Override
            public void onFailure(@NonNull Call<List<ImageModel>> call, @NonNull Throwable t) {
                callBack.isLoading(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private static void addToListAndCheck(@NonNull Context context, ArrayList<GeneralModel> list, @NonNull List<ImageModel> responseBody, @Nullable LikesModel likesModel) {

        for (ImageModel imageModel : responseBody) {
            GeneralModel generalModel = new GeneralModel(
                    imageModel.getUrls(),
                    imageModel.getId(),
                    imageModel.getDescription(),
                    imageModel.getLikes(),
                    imageModel.getUserModel(),
                    imageModel.getLinksModel(),
                    imageModel.isLikedByUser(),
                    imageModel.getViews());
            list.add(generalModel);

            if (likesModel != null) {
                if (likesModel.getCurrentLikedList().getValue() != null && likesModel.getCurrentLikedList().getValue().containsKey(generalModel.getImageId())) {
                    likesModel.getLastLikedModel().postValue(generalModel);
                    generalModel.setLiked(true);
                    LikedDataBase likedDataBase = LikedDataBase.getInstance(context);
                    likedDataBase.insertOrUpdatePicture(generalModel);
                } else if (likesModel.getCurrentLikedList().getValue() == null) {
                    Log.d("myapi", "likesModel.getCurrentLikedList().getValue() == null ");

                }
            }
        }
    }
}
