package com.java.busylooping.backget.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class DownloadDatabase extends SQLiteOpenHelper {
    private static WeakReference<DownloadDatabase> mInstance = null;
    private static final String DATABASE_NAME = "download.db";
    private static final int DATABASE_VERSION = 1;

    public synchronized static DownloadDatabase getInstance(@NonNull Context context) {
        if (mInstance == null || mInstance.get() == null) {
            mInstance = new WeakReference<>(new DownloadDatabase(context, DATABASE_NAME, null, DATABASE_VERSION));
        }
        return mInstance.get();
    }

    public DownloadDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
