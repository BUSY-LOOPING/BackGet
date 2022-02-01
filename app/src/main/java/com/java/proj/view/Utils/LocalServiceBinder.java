package com.java.proj.view.Utils;

import android.os.Binder;

import java.lang.ref.WeakReference;

public class LocalServiceBinder<T> extends Binder {
    private static final String TAG = "LocalServiceBinder";
    private final WeakReference<T> mService;

    public LocalServiceBinder(T service) {
        mService = new WeakReference<>(service);
    }

    public T getService() {
        return mService.get();
    }
}
