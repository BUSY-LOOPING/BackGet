package com.java.proj.view;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerAccessor;
import com.java.proj.view.Utils.GlobalAppControllerService;

/**
 *Application class with globalAppControllerService
 */
public class ApplicationClass extends Application {

    public static final String CHANNEL_ID_1 = "NotificationChannel1";
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

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
