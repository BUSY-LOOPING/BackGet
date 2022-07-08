package com.java.busylooping.backget.utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.java.busylooping.backget.ApplicationClass;
import com.java.busylooping.backget.AsyncTasks.SaveImgToStorage;
import com.java.busylooping.backget.AsyncTasks.SetBmpAsWallpaper;
import com.java.busylooping.backget.CallBacks.DownloadCallBack;
import com.java.busylooping.backget.CallBacks.SuccessCallBack;
import com.java.busylooping.backget.DataBase.LikedDataBase;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.api.ApiInterface;
import com.java.busylooping.backget.api.ApiUtilities;
import com.java.busylooping.backget.models.DownloadGeneralModel;
import com.java.busylooping.backget.models.DownloadModel;
import com.java.busylooping.backget.models.GeneralModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalAppControllerService extends Service {
    public static final String FLAGS = "GlobalAppControllerServiceFlags";

    private GlobalAppController globalAppController;
    private static final String TAG = "GlobalAppControllerSvc";
//    private ThreadPoolExecutor executor;
    private ExecutorService executorService;
//    private int NUMBER_OF_CORES;
    private GlideImageLoader glideImageLoader;
    private Random random;
    private HashMap<String, Integer> hashMap;
    private HashMap<String, GeneralModel> generalModelHashMap;

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
        Log.d(TAG, "onCreate: ");
        generalModelHashMap = new HashMap<>();
        random = new Random(1000);
        hashMap = new HashMap<>();
//        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
//        executor = new ThreadPoolExecutor(
//                NUMBER_OF_CORES * 2,
//                NUMBER_OF_CORES * 2,
//                60L,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>()
//        );
        executorService = Executors.newCachedThreadPool();
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
        return true;
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
                Runnable runnable = null;
                switch (flag) {
                    case EventDef.Category.NONE:
                        break;
                    case EventDef.DOWNLOAD_OPTIONS.download_full:
                        Log.d(TAG, "onStartCommand: " + generalModel.getUriModel().getFull());
                        runnable = downloadUrl(generalModel, EventDef.DOWNLOAD_OPTIONS.download_full, null);
                        break;
                    case EventDef.DOWNLOAD_OPTIONS.download_1080:
                        Log.d(TAG, "onStartCommand: " + generalModel.getUriModel().getRegular());
                        runnable = downloadUrl(generalModel, EventDef.DOWNLOAD_OPTIONS.download_1080, null);
                        break;
                    case EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper:
                        runnable = setAsWallpaper(generalModel, EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper);
                        break;
                    case EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper:
                        runnable = setAsWallpaper(generalModel, EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper);
                        break;
                    case EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper:
                        runnable = setAsWallpaper(generalModel, EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper);
                        break;
                }
                if (runnable != null) {
                    try {
                        executorService.execute(runnable);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
//                    executor.execute(runnable);
                }
            }

        }
        return START_STICKY;
    }

    private void addKey(String key, GeneralModel generalModel, boolean post) {
        if (post) {
            AppEvent appEvent = new AppEvent(EventDef.Category.DOWNLOAD, EventDef.DOWNLOAD_OPTIONS.download_started, 0, 0);
            appEvent.extras.putString(GlobalAppController.GENERAL_MODEL_ID, generalModel.getImageId());
            globalAppController.postToBus(appEvent);
        }
        hashMap.put(key, Math.abs(random.nextInt(1000)) + 1);
        generalModelHashMap.put(generalModel.getImageId(), generalModel);
    }

    private void removeKey(String key, String id, boolean post) {
        hashMap.remove(key);
        generalModelHashMap.remove(id);
        if (hashMap.isEmpty() && post) {
            AppEvent appEvent = new AppEvent(EventDef.Category.DOWNLOAD, EventDef.DOWNLOAD_OPTIONS.download_ended, 0, 0);
            appEvent.extras.putString(GlobalAppController.GENERAL_MODEL_ID, id);
            globalAppController.postToBus(appEvent);
        }
    }

    private Runnable downloadUrl(GeneralModel generalModel, int downloadRes, @Nullable DownloadCallBack downloadCallBack) {
        return new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String id = generalModel.getImageId();
                String downloadUrl;
                if (downloadRes == EventDef.DOWNLOAD_OPTIONS.download_full) {
                    downloadUrl = generalModel.getUriModel().getFull();
                } else {
                    downloadUrl = generalModel.getUriModel().getRegular();
                }

                Toast.makeText(GlobalAppControllerService.this, "Downloading", Toast.LENGTH_SHORT).show();
                addKey(downloadUrl, generalModel, true);
                Intent intent = new Intent(GlobalAppControllerService.this, MainActivity.class);
                intent.putExtra(GlobalAppController.FROM_NOTIFICATION,(Integer) EventDef.Category.NOTIFICATION_CLICK);
                intent.putExtra(GlobalAppController.NOTIFICATION_ACTION,(Integer) EventDef.Category.VIEW_DOWNLOADED);
                intent.putExtra(GlobalAppController.GENERAL_MODEL,(GeneralModel) generalModel);
                intent.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                PendingIntent pendingIntent = PendingIntent.getActivity(GlobalAppControllerService.this, 505, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = getNotificationBuilder(ApplicationClass.CHANNEL_ID_1, pendingIntent);
                showDownloadNotification(builder, 0, "Downloading", null, downloadUrl, false);

                String accessToken = globalAppController.getAccessToken();
                triggerDownload(ApiUtilities.getApiInterface(accessToken), id);


                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.cursor_drawable)
                        .error(R.drawable.cursor_drawable)
                        .priority(Priority.HIGH);

                ProgressBar progressBar = new ProgressBar(GlobalAppControllerService.this);
                ImageView imageView = new ImageView(GlobalAppControllerService.this);
                glideImageLoader = new GlideImageLoader(imageView, progressBar);
                glideImageLoader.load(downloadUrl, options, new DownloadCallBack() {
                    @Override
                    public void completed(int progress) {
                        if (downloadCallBack != null)
                            downloadCallBack.completed(progress);
                        if (progress < 100) {
//                            showNotification(builder, progress, "Downloading", null);
                            showDownloadNotification(builder, progress, "Downloading", null, downloadUrl, false);
                        }
                    }

                    @Override
                    public void finalBitmap(Bitmap bitmap) {
                        SaveImgToStorage saveImgToStorage = new SaveImgToStorage(imageView.getContext(), bitmap, new SuccessCallBack() {
                            @Override
                            public void onSuccess(@Nullable Object obj) {
                                showDownloadNotification(builder, 100, "Downloaded", bitmap, downloadUrl, false);
                                LikedDataBase likedDataBase = LikedDataBase.getInstance(imageView.getContext());
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date(System.currentTimeMillis());
                                String dateTxt = simpleDateFormat.format(date);
                                Log.d(TAG, "onSuccess: date " + dateTxt);
                                likedDataBase.downloadPicture(new DownloadGeneralModel(generalModel, dateTxt, obj == null ? "" : obj.toString(), downloadRes));
//                        ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
//                        stopSelf();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            stopForeground(Service.STOP_FOREGROUND_DETACH);
//                        } else {
//                            stopForeground(false);
//                        }
                                ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
                                removeKey(downloadUrl, id, true);
                                if (downloadCallBack != null)
                                    downloadCallBack.finalBitmap(bitmap);
//                        hashMap.remove(downloadUrl);
                            }

                            @Override
                            public void onFailure() {
                                showDownloadNotification(builder, 100, "Download Failed", null, downloadUrl, true);
//                        ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
//                        stopSelf();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            stopForeground(Service.STOP_FOREGROUND_DETACH);
//                        } else {
//                            stopForeground(false);
//                        }
                                ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
                                removeKey(downloadUrl, id, true);
//                        hashMap.remove(downloadUrl);
                            }
                        });
                        saveImgToStorage.execute();

                    }

                    @Override
                    public void onFailed() {
                        showDownloadNotification(builder, 100, "Download Failed", null, downloadUrl, true);
                        ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
//                stopSelf();
                        removeKey(downloadUrl, id, true);
//                hashMap.remove(downloadUrl);
                    }
                });
                Looper.loop();
            }
        };
    }


    private Runnable setAsWallpaper(GeneralModel generalModel, int flag) {
        return new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                NotificationCompat.Builder builder = getNotificationBuilder(ApplicationClass.CHANNEL_ID_3, null);
                String contentText = "Setting your image as wallpaper! Sit tight";
                switch (flag) {
                    case EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper:
                        contentText = "Setting your image as home screen wallpaper! Sit tight";
                        break;
                    case EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper:
                        contentText = "Setting your image as lock screen wallpaper! Sit tight";
                        break;
                    case EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper:
                        contentText = "Setting your image as home and lock screen wallpaper! Sit tight";
                        break;
                }
                addKey(generalModel.getImageId(), generalModel, false);
                showSetAsWallpaperNotification(builder, "Setting wallpaper", contentText, generalModel.getImageId(), true);
                executorService.execute(downloadUrl(generalModel, EventDef.DOWNLOAD_OPTIONS.download_full, new DownloadCallBack() {
                    @Override
                    public void completed(int progress) {

                    }

                    @Override
                    public void finalBitmap(Bitmap bitmap) {
                        if (bitmap != null) {
                            SetBmpAsWallpaper setBmpAsWallpaper = new SetBmpAsWallpaper(GlobalAppControllerService.this, bitmap, flag, output -> {
                                addKey(generalModel.getImageId(), generalModel, false);
                                if (output) {
                                    showSetAsWallpaperNotification(builder, "Wallpaper Set", "Your image was set as wallpaper!", generalModel.getImageId(), false);
                                } else {
                                    showSetAsWallpaperNotification(builder, "Something went wrong", "Something went wrong while setting the image as wallpaper! Try again", generalModel.getImageId(), false);
                                }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            stopForeground(Service.STOP_FOREGROUND_DETACH);
//                        } else {
//                            stopForeground(false);
//                        }
                                ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
//                        ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
                                removeKey(generalModel.getImageId(), generalModel.getImageId(), false);
                            });
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
                        showSetAsWallpaperNotification(builder, "Something went wrong", "Image download failed so the image cannot be set as wallpaper! Try again", generalModel.getImageId(), false);
                        ServiceCompat.stopForeground(GlobalAppControllerService.this, ServiceCompat.STOP_FOREGROUND_DETACH);
                        removeKey(generalModel.getImageId(), generalModel.getImageId(), false);
                    }
                }));
                Looper.loop();
            }
        };

    }

    private void showSetAsWallpaperNotification(@NonNull NotificationCompat.Builder builder, @NonNull String title, @NonNull String contentText, @NonNull String key, boolean ongoing) {
        Log.d(TAG, "showSetAsWallpaperNotification: title " + title);
        builder
                .setContentTitle(title)
                .setContentText(contentText)
                .setProgress(0, 0, ongoing)
                .setOngoing(ongoing)
                .setStyle(new NotificationCompat.BigTextStyle());
        startForeground(hashMap.get(key), builder.build());
    }


    private void showDownloadNotification(NotificationCompat.Builder builder, int progress, @NonNull String title, @Nullable Bitmap picture, String key, boolean failed) {
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
        builder
                .setContentText(contentText)
                .setAutoCancel(true);

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
        if (picture != null) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(hashMap.get(key), builder.build());
        } else {
            startForeground(hashMap.get(key), builder.build());
        }
        Log.d(TAG, "showNotification: id = " + hashMap.get(key));

    }

    private NotificationCompat.Builder getNotificationBuilder(@NonNull String channelId, @Nullable PendingIntent contentIntent) {
        if (contentIntent == null) {
            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
            contentIntent = PendingIntent.getActivity(this, 505, intent, 0);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
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
        executorService.shutdown();
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
