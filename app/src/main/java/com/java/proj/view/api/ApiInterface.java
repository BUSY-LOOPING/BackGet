package com.java.proj.view.api;

import static com.java.proj.view.api.ApiUtilities.API_KEY;

import com.java.proj.view.Models.CollectionModel;
import com.java.proj.view.Models.DownloadModel;
import com.java.proj.view.Models.ImageModel;
import com.java.proj.view.Models.SearchModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("photos")
    Call<List<ImageModel>> getImages(
            @Query("page") int page ,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy
    );

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/search/photos")
    Call<SearchModel> searchImages(
            @Query("query") String query
    );

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/search/collections")
    Call<CollectionModel> searchCollections(
            @Query("page") int page,
            @Query("per_page") int per_page,
            @Query("query") String query,
            @Query("order_by") String orderBy
    );

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/topics/{id_or_slug}/photos")
    Call<List<ImageModel>> getTopicImages(
            @Path(value = "id_or_slug", encoded = true) String topicName,
            @Query("page") int page ,
            @Query("per_page") int perPage,
            @Query("order_by") String orderBy
    );

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/photos/{id}/download")
    Call<DownloadModel> triggerDownload (
            @Path(value = "id", encoded = true) String id,
            @Query("ixid") String ixid
    );
}
