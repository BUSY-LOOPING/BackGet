package com.java.busylooping.backget.AsyncTasks;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.busylooping.backget.CallBacks.AsyncResponse;
import com.java.busylooping.backget.utils.EventDef;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

@SuppressWarnings("ALL")
public class SetBmpAsWallpaper extends AsyncTask<Void, Void, Boolean> {
    private final WeakReference<Context> contextWeakReference;
    private Bitmap bitmap;
    private final int flag;
    private AsyncResponse<?, ?, Boolean> asyncResponse;


    public SetBmpAsWallpaper(@NonNull Context context, @Nullable Bitmap bitmap, int flag, AsyncResponse<?, ?,Boolean> asyncResponse) {
        this.contextWeakReference = new WeakReference<>(context);
        this.bitmap = bitmap;
        this.flag = flag;
        this.asyncResponse = asyncResponse;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        Context context = contextWeakReference.get();
        if (context != null && bitmap != null) {
//            bitmap = getResizedBitmap(bitmap, 500);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int currSize;
            int currQuality = 100;
            double maxSizeBytes = 16777210;

            do {
                bitmap.compress(Bitmap.CompressFormat.JPEG, currQuality, stream);
                currSize = stream.toByteArray().length;
                // limit quality by 5 percent every time
                currQuality -= 5;
                Log.d("mywall", "doInBackground: currQuality = " + currQuality);

            } while (currSize >= maxSizeBytes);
            // creating the instance of the WallpaperManager
            final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
            try {
                switch (flag) {
                    case EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper:
                        return wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM) != 0;
                    case EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            return wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK) != 0;
                        }
                        return true;
                    case EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            boolean res1 = wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK) != 0;
                            boolean res2 = wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM) != 0;
                            return res1 && res2;
                        }
                        return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (asyncResponse != null) {
            asyncResponse.onProcessFinished(result);
        }
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return Bitmap
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
