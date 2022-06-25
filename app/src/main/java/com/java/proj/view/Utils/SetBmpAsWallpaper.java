package com.java.proj.view.Utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

@SuppressWarnings("ALL")
public class SetBmpAsWallpaper extends AsyncTask<Void, Void, Void> {
    private final WeakReference<Context> contextWeakReference;
    private Bitmap bitmap;
    private final int flag;


    public SetBmpAsWallpaper(@NonNull Context context, @Nullable Bitmap bitmap, int flag) {
        this.contextWeakReference = new WeakReference<>(context);
        this.bitmap = bitmap;
        this.flag = flag;
    }


    @Override
    protected Void doInBackground(Void... voids) {
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
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);

                        break;
                    case EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                        }
                        break;
                    case EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                        }
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
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
