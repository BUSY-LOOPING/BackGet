package com.java.proj.view.api;

import static com.java.proj.view.api.ApiUtilities.API_KEY;

import com.java.proj.view.Models.AccessToken;
import com.java.proj.view.Models.CollectionModel;
import com.java.proj.view.Models.DownloadModel;
import com.java.proj.view.Models.ImageModel;
import com.java.proj.view.Models.LikeUnlikeModel;
import com.java.proj.view.Models.ProfileModel;
import com.java.proj.view.Models.SearchModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

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
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int per_page,
            @Query("order_by") String order_by
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
    @GET
    Call<DownloadModel> triggerDownload (
            @Url String url
    );

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("photos/{id}/download")
    Call<DownloadModel> triggerDownloadWithId (
            @Path(value = "id", encoded = true) String id
    );

    @POST("/oauth/token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("redirect_uri") String redirect_uri,
            @Field("code") String code,
            @Field("grant_type") String grantType
    );



    @POST("/photos/{id}/like")
    Call<LikeUnlikeModel> likePicture(
            @Path(value = "id", encoded = true) String id
    );

    @DELETE("/photos/{id}/like")
    Call<LikeUnlikeModel> unLikePicture(
            @Path(value = "id", encoded = true) String id
    );


    @GET("me")
    Call<ProfileModel> getCurrentUser();

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/users/{username}")
    Call<ProfileModel> getUser(
            @Path(value = "username", encoded = true) String userName);


    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/users/{username}/photos")
    Call<List<ImageModel>> getUserImages(
            @Path(value = "username", encoded = true) String userName,
            @Query("page") int page ,
            @Query("per_page") int perPage

    );
}
