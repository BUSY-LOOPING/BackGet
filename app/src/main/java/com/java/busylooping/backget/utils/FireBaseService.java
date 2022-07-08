package com.java.busylooping.backget.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.java.busylooping.backget.ApplicationClass;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.R;

import java.util.Random;

public class FireBaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFireBaseServiceTag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, "onMessageReceived: ffrom" + message);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        Intent intentView = new Intent(this, MainActivity.class);
        intentView.putExtra(GlobalAppController.FROM_NOTIFICATION, EventDef.Category.NOTIFICATION_CLICK);
        intentView.putExtra(GlobalAppController.NOTIFICATION_ACTION, EventDef.Category.VIEW_IMAGE);
        intentView.putExtra(GlobalAppController.GENERAL_MODEL_ID, message.getData().get("imageId"));
        Intent intentShare = new Intent(this, MainActivity.class);
        int notifId = Math.abs(new Random().nextInt(1000));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntentView = PendingIntent.getActivity(this, 0, intentView, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pendingIntentShare = PendingIntent.getActivity(this, 0, intentShare, PendingIntent.FLAG_ONE_SHOT);
        Glide.with(this)
                .asBitmap()
                .load(message.getData().get("icon"))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Notification notification = new NotificationCompat.Builder(FireBaseService.this, ApplicationClass.CHANNEL_ID_2)
                                .setContentIntent(pendingIntent)
                                .addAction(android.R.drawable.ic_menu_view, "View", pendingIntentView)
                                .addAction(android.R.drawable.ic_menu_share, "Share", pendingIntentShare)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle(message.getData().get("title"))
                                .setContentText(message.getData().get("message"))
                                .setStyle(
                                        new NotificationCompat.BigPictureStyle()
                                                .bigPicture(resource)
                                )
                                .build();
                        notificationManager.notify(notifId, notification);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d(TAG, "onLoadCleared: ");
                    }
                });

//        Notification notification = new NotificationCompat.Builder(FireBaseService.this, ApplicationClass.CHANNEL_ID_2)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(message.getData().get("title"))
//                .setContentText(message.getData().get("message"))
//                .setStyle(
//                        new NotificationCompat.BigTextStyle().setBigContentTitle(message.getData().get("title"))
//                        .setSummaryText(message.getData().get("message"))
//                )
//                .build();
//        notificationManager.notify(notifId, notification);


    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token);
    }

}
