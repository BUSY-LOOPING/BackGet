package com.java.proj.view.Utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventDef {
    // categories
    @IntDef({Category.NONE, Category.IMAGE_CLICK, Category.TOOLBAR, Category.BTN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
        int NONE = 0;

        int IMAGE_CLICK = 100;
        int TOOLBAR = 101;
        int BTN = 102;
    }

    @IntDef({BUTTON_EVENTS.NONE, BUTTON_EVENTS.GALLERY_BUTTON, BUTTON_EVENTS.CUSTOM_BUTTON, BUTTON_EVENTS.LIKED_BUTTON, BUTTON_EVENTS.DOWNLOAD_BUTTON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BUTTON_EVENTS {
        int NONE = 0;

        int GALLERY_BUTTON = 300;
        int CUSTOM_BUTTON = 301;
        int LIKED_BUTTON = 302;
        int DOWNLOAD_BUTTON = 303;
    }

    @IntDef({TOOLBAR_EVENTS.SHARE_BTN, TOOLBAR_EVENTS.PRO_BTN, TOOLBAR_EVENTS.SETTINGS_BTN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TOOLBAR_EVENTS {
        int PRO_BTN = 0;
        int SHARE_BTN = 1;
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
