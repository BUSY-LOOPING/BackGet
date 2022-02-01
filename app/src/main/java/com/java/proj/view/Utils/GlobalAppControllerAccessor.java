package com.java.proj.view.Utils;

import android.content.Context;

public class GlobalAppControllerAccessor {
    public static interface Provider{
        GlobalAppController getGlobalAppControllerInstance();
    }

    public static GlobalAppController getInstance(Context context) {
        return ((Provider) context.getApplicationContext()).getGlobalAppControllerInstance();
    }
}