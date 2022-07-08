package com.java.busylooping.backget.api;

import com.java.busylooping.backget.Notification.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GoogleApiInterface {

    @Headers({"Authorization: Bearer " + GoogleApiUtilities.SERVER_KEY, "Content-Type:" + GoogleApiUtilities.CONTENT_TYPE})
    @POST("/v1/projects/" + GoogleApiUtilities.PROJECT_ID +"/messages:send")
    Call<PushNotification> sendNotificationNew(@Body PushNotification pushNotification);

    @Headers({"Authorization: key=" + GoogleApiUtilities.SERVER_KEY, "Content-Type:" + GoogleApiUtilities.CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification pushNotification);
}
