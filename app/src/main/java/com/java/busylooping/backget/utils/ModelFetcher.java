package com.java.busylooping.backget.utils;

import android.content.Context;

public class ModelFetcher {
    private final Context context;
    private final Class<?> c;

    public ModelFetcher(Context context, Class<?> c) {
        this.c = c;
        this.context = context;
    }

    private void writeToCache() {

    }

    public Class<?> getModel() {
        return null;
    }
}
