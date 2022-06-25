package com.java.proj.view.Utils;

import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.java.proj.view.ApplicationClass;
import com.java.proj.view.CallBacks.DownloadCallBack;
import com.java.proj.view.CallBacks.SuccessCallBack;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.DownloadModel;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;
import com.java.proj.view.api.ApiInterface;
import com.java.proj.view.api.ApiUtilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalAppControllerService extends Service {
    public static final String FLAGS = "GlobalAppControllerServiceFlags";

    private GlobalAppController globalAppController;
    private static final String TAG = "GlobalAppControllerSvc";
    private ThreadPoolExecutor executor;
    private int NUMBER_OF_CORES;
    private GlideImageLoader glideImageLoader;
    private Random random;
    private HashMap<String, Integer> hashMap;

    //internal class
    /*
     * LocalServiceBinder is in extended class of binder which holds the reference to the service
     * in a weak reference. The service type is GlobalAppControllerService.
     * So, in onBind, new instance of LocalBinder with current class reference as parameter is returned.
     * */
    public static class LocalBinder extends LocalServiceBinder<GlobalAppControllerService> {
        public LocalBinder(GlobalAppControllerService service) {
            super(service);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random(1000);
        hashMap = new HashMap<>();
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>()
        );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return new LocalBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    /**
     * START_STICKY tells the OS to recreate the service after it has enough memory and call
     * onStartCommand() again with a null intent. START_NOT_STICKY tells the OS to not bother
     * recreating the service again. There is also a third code START_REDELIVER_INTENT that
     * tells the OS to recreate the service and redeliver the same intent to onStartCommand()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand(flags = " + flags + ", startId = " + startId);
        if (intent != null) {
            GeneralModel generalModel = (GeneralModel) intent.getSerializableExtra(GlobalAppController.GENERAL_MODEL);
            int flag = intent.getIntExtra(FLAGS, 0);
            if (generalModel != null) {
                switch (flag) {
                    case EventDef.Category.NONE:
                        break;
                    case EventDef.DOWNLOAD_OPTIONS.download_full:
                        Log.d(TAG, "onStartCommand: " + generalModel.getUriModel().getFull());
                        downloadUrl(generalModel.getUriModel().getFull(), generalModel.getImageId(), null);
                        break;
                    case EventDef.DOWNLOAD_OPTIONS.download_1080:
                        Log.d(TAG, "onStartCommand: " + generalModel.getUriModel().getRegular());
                        downloadUrl(generalModel.getUriModel().getRegular(), generalModel.getImageId(), null);
                        break;
                    case EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper:
                        setAsWallpaper(generalModel, EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper);
                        break;
                    case EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper:
                        setAsWallpaper(generalModel, EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper);
                        break;
                    case EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper:
                        setAsWallpaper(generalModel, EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper);
                        break;
                }
            }

        }
        return START_STICKY;
    }

    private void addKey(String key, String id) {
        AppEvent appEvent = new AppEvent(EventDef.Category.DOWNLOAD, EventDef.DOWNLOAD_OPTIONS.download_started, 0,0);
        appEvent.extras.putString(GlobalAppController.GENERAL_MODEL_ID, id);
        globalAppController.postToBus(appEvent);
        hashMap.put(key, Math.abs(random.nextInt(1000)) + 1);
    }

    private void removeKey(String key, String id) {
        hashMap.remove(key);
        if (hashMap.isEmpty()) {
            AppEvent appEvent = new AppEvent(EventDef.Category.DOWNLOAD, EventDef.DOWNLOAD_OPTIONS.download_ended, 0, 0);
            appEvent.extras.putString(GlobalAppController.GENERAL_MODEL_ID, id);
            globalAppController.postToBus(appEvent);
        }
    }

    private void downloadUrl(String downloadUrl, String id, @Nullable DownloadCallBack downloadCallBack) {
        Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
//        hashMap.put(downloadUrl, Math.abs(random.nextInt(1000)) + 1);
//        AppEvent appEvent = new AppEvent(EventDef.Category.DOWNLOAD, EventDef.DOWNLOAD_OPTIONS.download_started, 0,0);
//        appEvent.extras.putString(GlobalAppController.GENERAL_MODEL_ID, id);
//        globalAppController.postToBus(appEvent);
        addKey(downloadUrl, id);
        NotificationCompat.Builder builder = getDownloadNotificationBuilder();
        showNotification(builder, 0, "Downloading", null, downloadUrl, false);

        String accessToken = globalAppController.getAccessToken();
        triggerDownload(ApiUtilities.getApiInterface(accessToken), id);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.cursor_drawable)
                .error(R.drawable.cursor_drawable)
                .priority(Priority.HIGH);

        ProgressBar progressBar = new ProgressBar(this);
        ImageView imageView = new ImageView(this);
        glideImageLoader = new GlideImageLoader(imageView, progressBar);
        glideImageLoader.load(downloadUrl, options, new DownloadCallBack() {
            @Override
            public void completed(int progress) {
                if (downloadCallBack != null)
                    downloadCallBack.completed(progress);
                if (progress < 100) {
//                            showNotification(builder, progress, "Downloading", null);
                    showNotification(builder, progress, "Downloading", null, downloadUrl, false);
                }
            }

            @Override
            public void finalBitmap(Bitmap bitmap) {
                SaveImgToStorage saveImgToStorage = new SaveImgToStorage(imageView.getContext(), bitmap, new SuccessCallBack(){
                    @Override
                    public void onSuccess() {
                        showNotification(builder, 100, "Downloaded", bitmap, downloadUrl, false);
                        stopForeground(false);
                        stopSelf();
                        removeKey(downloadUrl, id);
                        if (downloadCallBack != null)
                            downloadCallBack.finalBitmap(bitmap);
//                        hashMap.remove(downloadUrl);
                    }

                    @Override
                    public void onFailure() {
                        showNotification(builder, 100, "Download Failed", null, downloadUrl, true);
                        stopForeground(false);
                        stopSelf();
                        removeKey(downloadUrl, id);
//                        hashMap.remove(downloadUrl);
                    }
                });
                saveImgToStorage.execute();

            }

            @Override
            public void onFailed() {
                showNotification(builder, 100, "Download Failed", null, downloadUrl, true);
                stopForeground(false);
                stopSelf();
                removeKey(downloadUrl, id);
//                hashMap.remove(downloadUrl);
            }
        });
    }

    private void setAsWallpaper(GeneralModel generalModel, int flag) {
        downloadUrl(generalModel.getUriModel().getFull(), generalModel.getImageId(), new DownloadCallBack() {
            @Override
            public void completed(int progress) {

            }

            @Override
            public void finalBitmap(Bitmap bitmap) {
                if (bitmap != null) {
                    SetBmpAsWallpaper setBmpAsWallpaper = new SetBmpAsWallpaper(GlobalAppControllerService.this, bitmap, flag);
                    setBmpAsWallpaper.execute();
//                     creating the instance of the WallpaperManager
//                    final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//                    try {
//                        switch (flag) {
//                            case EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper:
//                                wallpaperManager.setBitmap(bitmap);
//                                break;
//                            case EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper:
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
//                                }
//                                break;
//                            case EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper:
//                                wallpaperManager.setBitmap(bitmap);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
//                                }
//                                break;
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            @Override
            public void onFailed() {

            }
        });
    }



    private void showNotification(NotificationCompat.Builder builder, int progress, @NonNull String title, @Nullable Bitmap picture, String key, boolean failed) {
        String contentText = "";
        builder.setContentTitle(title);
        if (progress < 100) {
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        if (failed) {
            contentText = "Something went wrong!";
        } else {
            contentText = progress + "/100% downloaded";
        }
        builder.setContentText(contentText);

        if (picture != null) {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                    .bigPicture(picture);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                style.showBigPictureWhenCollapsed(true);
            }
            builder.setStyle(style);
        } else {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(title).setSummaryText(contentText).setBigContentTitle(title));
        }
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());
        Log.d(TAG, "showNotification: id = " + hashMap.get(key));
        startForeground(hashMap.get(key), builder.build());

    }

    private NotificationCompat.Builder getDownloadNotificationBuilder() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 505, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID_1)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent);
        return builder;
    }


    private void triggerDownload(@NonNull ApiInterface apiInterface, @NonNull String query) {
        apiInterface.triggerDownloadWithId(query).enqueue(new Callback<DownloadModel>() {
            @Override
            public void onResponse(@NonNull Call<DownloadModel> call, @NonNull Response<DownloadModel> response) {
                if (response.body() != null) {
                    Log.d("mydownload", "onResponse download: response body not null");
                    DownloadModel downloadModel = response.body();
                } else {
                    Log.d("mydownload", "onResponse download: response body null | request =" + call.request() + " accessToken = " + call.request().headers());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DownloadModel> call, @NonNull Throwable t) {
                Log.d("mydownload", "onFailure" + t);

            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    public GlobalAppController getGlobalAppController() {
        return globalAppController;
    }

    public void setGlobalAppController(GlobalAppController globalAppController) {
        this.globalAppController = globalAppController;
        if (globalAppController != null) {
            globalAppController.bindHolderService(this);
        }
    }
}
