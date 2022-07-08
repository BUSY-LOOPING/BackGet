package com.java.busylooping.backget.CallBacks;

import androidx.annotation.Nullable;

public interface SuccessCallBack {
    void onSuccess(@Nullable Object obj);
    void onFailure();
}
