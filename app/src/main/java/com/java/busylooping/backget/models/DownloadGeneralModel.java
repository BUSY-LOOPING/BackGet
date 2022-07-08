package com.java.busylooping.backget.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.java.busylooping.backget.R;
import com.java.busylooping.backget.utils.EventDef;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadGeneralModel extends GeneralModel {

    @EventDef.DOWNLOAD_OPTIONS
    private int downloadRes;

    @NonNull
    private String savePath;

    @NonNull
    private String date;

    public DownloadGeneralModel(GeneralModel generalModel, @NonNull String date, @NonNull String path, @EventDef.DOWNLOAD_OPTIONS int downloadRes) {
        super(generalModel.getUriModel(), generalModel.getImageId(), generalModel.getDescription(), generalModel.getLikes(), generalModel.getProfileModel(), generalModel.getLinksModel(), generalModel.isLiked(), generalModel.getViews());
        this.date = date;
        this.downloadRes = downloadRes;
        this.savePath = path;
    }


    public int getDownloadRes() {
        return downloadRes;
    }

    public void setDownloadRes(int downloadRes) {
        this.downloadRes = downloadRes;
    }

    @NonNull
    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(@NonNull String savePath) {
        this.savePath = savePath;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public static String getFormattedPath(Context context, String uriPath) {
        Uri uri = Uri.parse(uriPath);
        File file = new File(uri.getPath());
        return getFileName(context, uri);
    }


    @SuppressLint("Range")
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Nullable
    public static DataHolderDownloadItem getDownloadDataHolder (Context context, DownloadGeneralModel downloadGeneralModel) {
        DataHolderDownloadItem dataHolderDownloadItem = null;
        Uri uri = Uri.parse(downloadGeneralModel.getSavePath());
        String [] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String path = DataHolderDownloadItem.humanReadablePath(cursor.getString(0));
                String displayName = cursor.getString(1);
                String size = DataHolderDownloadItem.humanReadableByteCountBin(Long.parseLong(cursor.getString(2)));
                String outputDate = downloadGeneralModel.getDate();
                String downloadRes = downloadGeneralModel.getDownloadRes() == EventDef.DOWNLOAD_OPTIONS.download_1080 ? "1080" : "FULL";
                int bgRes = downloadGeneralModel.getDownloadRes() == EventDef.DOWNLOAD_OPTIONS.download_1080 ? ContextCompat.getColor(context, R.color.yellow) : ContextCompat.getColor(context, R.color.brown);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("E,dd MMMM");
                try {
                    Date date = inputFormat.parse(downloadGeneralModel.getDate());
                    if (date != null) {
                        outputDate = outputFormat.format(date);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataHolderDownloadItem = new DataHolderDownloadItem(displayName, outputDate, size, path, downloadRes, bgRes);
            }
            cursor.close();
        }

        return dataHolderDownloadItem;
    }
}