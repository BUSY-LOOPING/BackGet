package com.java.proj.view.Utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventDef {
    // categories
    @IntDef({Category.NONE, Category.IMAGE_CLICK, Category.TOOLBAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
        int NONE = 0;
        int IMAGE_CLICK = 100;
        int TOOLBAR = 101;
        int GALLERY_BTN = 102;
    }

    @IntDef({TOOLBAR_EVENTS.SHARE_BTN, TOOLBAR_EVENTS.PRO_BTN, TOOLBAR_EVENTS.SETTINGS_BTN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TOOLBAR_EVENTS {
        int SHARE_BTN = 0;
        int PRO_BTN = 1;
        int SETTINGS_BTN = 2;
    }

    @IntDef({CALLBACK_EVENTS.LOADING_CALLBACK, CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, CALLBACK_EVENTS.LOADING, CALLBACK_EVENTS.NOT_LOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CALLBACK_EVENTS {
        int LOADING_CALLBACK = 0;
        int LIST_REFRESH_CALLBACK = 1;

        int LOADING = 100;
        int NOT_LOADING = 101;
    }
}
