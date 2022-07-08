package com.java.busylooping.backget.CallBacks;

public interface AsyncResponse<M, N, T> {
    void onProcessFinished(T output);
}
