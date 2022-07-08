package com.java.busylooping.backget;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 *Application class with globalAppControllerService
 */
public class ApplicationClass extends Application {

    public static final String CHANNEL_ID_1 = "NotificationChannel1";
    public static final String CHANNEL_ID_2 = "NotificationChannel2";
    public static final String CHANNEL_ID_3 = "NotificationChannel3";
    private static final String TAG = "ApplicationClassTag";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        createNotificationChannel();
//        globalAppController = new GlobalAppController(this, null);
//        doBindGlobalAppControllerService();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_1,
                    "Current Download", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Images in queue for download");
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setShowBadge(true);

            NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID_2,
                    "Rich Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel2.setDescription("Check out what's new");
            notificationChannel2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel2.setShowBadge(true);

            NotificationChannel notificationChannel3 = new NotificationChannel(CHANNEL_ID_3,
                    "Wallpaper Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel2.setDescription("See the status of wallpaper");
            notificationChannel2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel2.setShowBadge(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.createNotificationChannel(notificationChannel2);
            notificationManager.createNotificationChannel(notificationChannel3);
        }
    }

    public static int dpToPx(@NonNull Context context,  int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(@NonNull Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
