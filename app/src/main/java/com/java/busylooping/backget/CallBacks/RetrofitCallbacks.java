package com.java.busylooping.backget.CallBacks;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Response;

public interface RetrofitCallbacks {
    void onResponse(@NonNull Call<?> call, @NonNull Response<?> response);
    void onFailure(@NonNull Call<?> call, @NonNull Throwable t);
}
