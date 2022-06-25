package com.java.proj.view.Utils;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EventDef {
    // categories
    @IntDef({Category.NONE, Category.IMAGE_CLICK, Category.TOOLBAR, Category.BTN, Category.LIKE_UNLIKE, Category.PERMISSION, Category.DOWNLOAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {
        int NONE = 0;

        int IMAGE_CLICK = 100;
        int TOOLBAR = 101;
        int BTN = 102;
        int LIKE_UNLIKE = 103;
        int PERMISSION = 104;
        int DOWNLOAD = 105;
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

    @IntDef({CALLBACK_EVENTS.LOADING_CALLBACK, CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, CALLBACK_EVENTS.LOADING, CALLBACK_EVENTS.NOT_LOADING, CALLBACK_EVENTS.SCROLL_CALLBACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CALLBACK_EVENTS {
        int LOADING_CALLBACK = 0;
        int LIST_REFRESH_CALLBACK = 1;
        int SCROLL_CALLBACK = 2;


        int LOADING = 100;
        int NOT_LOADING = 101;
    }

    @IntDef({LIKE_UNLIKE_EVENTS.LIKED, LIKE_UNLIKE_EVENTS.UNLIKED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LIKE_UNLIKE_EVENTS {
        int LIKED = 400;
        int UNLIKED = 401;
    }

    @IntDef({PERMISSION_EVENTS.PERMISSION_GRANTED, PERMISSION_EVENTS.PERMISSION_NOT_GRANTED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PERMISSION_EVENTS {
        int PERMISSION_GRANTED = 601;
        int PERMISSION_NOT_GRANTED = 600;
    }


    @IntDef({DOWNLOAD_OPTIONS.download_1080, DOWNLOAD_OPTIONS.download_full, DOWNLOAD_OPTIONS.download_started, DOWNLOAD_OPTIONS.download_ended})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DOWNLOAD_OPTIONS {
        int download_1080 = 1000;
        int download_full = 1001;

        int download_started = 1111;
        int download_ended = 1112;
    }

    @IntDef({WALLPAPER_OPTIONS.home_screen_wallpaper, WALLPAPER_OPTIONS.lock_screen_wallpaper, WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WALLPAPER_OPTIONS {
        int home_screen_wallpaper = 1002;
        int lock_screen_wallpaper = 1003;
        int home_n_lock_screen_wallpaper = 1004;
    }

    @IntDef({REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface REQUEST_CODES {
        int PERMISSION_WRITE_EXTERNAL_STORAGE = 500;
        int PERMISSION_MANAGE_EXTERNAL_STORAGE = 501;
    }
}
