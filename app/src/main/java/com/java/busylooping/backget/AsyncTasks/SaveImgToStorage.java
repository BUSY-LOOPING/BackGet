package com.java.busylooping.backget.AsyncTasks;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import com.java.busylooping.backget.CallBacks.SuccessCallBack;

import java.io.File;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SaveImgToStorage extends AsyncTask<Void, Void, String> {
    private final WeakReference<Context> weakReference;
    private final Bitmap bitmap;
    private final SuccessCallBack callBack;
    private boolean success = false;
    private String savePath;

    public SaveImgToStorage(@NonNull Context context, @NonNull Bitmap bitmap, @NonNull SuccessCallBack callBack) {
        weakReference = new WeakReference<>(context);
        this.bitmap = bitmap;
        this.callBack =callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        saveBitmapToStorage(bitmap);
        return savePath;
    }

    @Override
    protected void onPostExecute(String savePath) {
        super.onPostExecute(savePath);
        if (success)
            callBack.onSuccess(savePath);
        else
            callBack.onFailure();
    }

    public void saveBitmapToStorage(@NonNull Bitmap bitmap) {
        try {
            String fileName = getFileName();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "BackGet");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                values.put(MediaStore.Images.Media.ALBUM, "BackGet");
            }
            values.put(MediaStore.Images.Media.WIDTH, bitmap.getWidth());
            values.put(MediaStore.Images.Media.HEIGHT, bitmap.getHeight());
            values.put(MediaStore.Images.Media.SIZE, bitmap.getByteCount());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, "BackGet/");
                values.put(MediaStore.MediaColumns.SIZE, bitmap.getByteCount());
                values.put(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME, "BackGet");
                values.put(MediaStore.MediaColumns.IS_PENDING, 0);
            } else {
                File ext = Environment.getExternalStorageDirectory();
                if (!ext.exists()) {
                    Log.d("mylog", "saveBitmapToStorage: ext does not exist");
                    ext.mkdirs();
                }
                File directory = new File(ext, "BackGet");
                boolean res = true;
                if (!directory.exists()) {
                    res = directory.mkdirs();
                    Log.d("mylog", "dir does not exist res = : " + res);
                }
                if (res) {
                    File file = new File(directory, fileName);
                    values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                    Log.d("mylog", "file absolute path : " + file.getAbsolutePath());
                }
                Log.d("mylog", "saveBitmapToStorage: " + res);
            }

            Context context = weakReference.get();
            Uri uri;
            try {
                uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }catch (Exception e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/BackGet");
                }
                uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
            savePath = uri.toString();
            Log.d("mylog", "uri =" + uri);
            try (OutputStream output = context.getContentResolver().openOutputStream(uri)) {
                success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            }
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
            Log.d("mylog", e.toString()); // java.io.IOException: Operation not permitted
        }
    }

    private String getFileName() {

//                Date c = Calendar.getInstance().getTime();
        Object c = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy-HH:mm:ss", Locale.getDefault());
        return df.format(c);
    }
}
