package com.java.busylooping.backget.api;


import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleApiUtilities {
    public static final String BASE_URL = "https://fcm.googleapis.com";
    public static final String PROJECT_ID = "backget-43e93";
    public static final String SERVER_KEY = "AAAAuD57jl0:APA91bHxMNwBSXzQzRXvuDm0JSm77AK-uNKFnFHqWRGHfCMlmkvWDM4_askEijpAvATltzDXC_0cnSsWbYmFcQHCUPxBD3ZhSigM6c3Xanm61qriyCzG_TcEaDWBVCAqF8WKRxc7byVR";
    public static final String CONTENT_TYPE = "application/json";
    public static final String TOPIC = "/topics/backget";

    private static WeakReference<GoogleApiInterface> retrofitWeakReference;

    public static GoogleApiInterface getApiInterface() {
        if (retrofitWeakReference == null || retrofitWeakReference.get() == null) {
            retrofitWeakReference = new WeakReference<>(new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(GoogleApiInterface.class));
        }
        return retrofitWeakReference.get();
    }

//    private static String getAccessToken() throws IOException {
//        GoogleCredentials googleCredentials = GoogleCredentials
//                .fromStream(new FileInputStream("service-account.json"))
//                .createScoped(Arrays.asList(SCOPES));
//        googleCredentials.refreshAccessToken();
//        return googleCredentials.getAccessToken().getTokenValue();
//    }

}
