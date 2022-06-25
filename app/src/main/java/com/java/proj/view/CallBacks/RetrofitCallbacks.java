package com.java.proj.view.CallBacks;

import androidx.annotation.NonNull;

import com.java.proj.view.Models.ProfileModel;

import retrofit2.Call;
import retrofit2.Response;

public interface RetrofitCallbacks {
    void onResponse(@NonNull Call<?> call, @NonNull Response<?> response);
    void onFailure(@NonNull Call<?> call, @NonNull Throwable t);
}
