package com.java.busylooping.backget.utils;

import android.content.Context;

public class GlobalAppControllerAccessor {
    public interface Provider{
        GlobalAppController getGlobalAppControllerInstance();
    }

    public static GlobalAppController getInstance(Context context) {
        return ((Provider) context).getGlobalAppControllerInstance();
    }
}
