package com.java.proj.view.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.UriModel;
import com.java.proj.view.Models.UserModel;
import com.java.proj.view.Models.UserProfileImageModel;
import com.java.proj.view.R;
import com.java.proj.view.Utils.FileReaderUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LikedDataBase extends SQLiteOpenHelper {
    private static final String TAG = "mysql";
    private static final String DATABASE_NAME = "likedDatabase.db";
    private static final String DATABASE_TABLE_NAME = "liked_table";
    private static final String URI_MODEL_TABLE_NAME = "uri_model_table";
    private static final String USER_MODEL_TABLE_NAME = "user_model_table";
    private static final int DATABASE_VERSION = 1;
    private static WeakReference<LikedDataBase> mInstance = null;
    private final Context context;

    private String sqlCreate = "";
    private String errorMessage = "";

    public synchronized static LikedDataBase getInstance(@NonNull Context context) {
        if (mInstance == null || mInstance.get() == null) {
            mInstance = new WeakReference<>(new LikedDataBase(context, DATABASE_NAME, null, DATABASE_VERSION));
        }
        return mInstance.get();
    }

    public LikedDataBase(@NonNull Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        try {
            String sql = FileReaderUtil.readTextFile(context.getResources().openRawResource(R.raw.create_sql_tables));
            sqlCreate = sql;
            String[] queries = sql.split(";;;");
            for (String query : queries) {
                Log.d(TAG, "qquery: " + query);
                sqLiteDatabase.execSQL(query);
            }
        } catch (SQLException sqlException) {
            errorMessage = sqlException.getMessage();
        } catch (IOException e) {
            errorMessage += "\n" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + DATABASE_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + URI_MODEL_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + USER_MODEL_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean likePicture(@NonNull String image_id,
                               @NonNull String user_model_id,
                               @NonNull String likes,
                               @NonNull String description,
                               @NonNull String user_model_name,
                               @NonNull String regular_url,
                               @NonNull String full_url,
                               @NonNull String raw_url) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("primary_key", image_id);
        contentValues.put("regular", regular_url);
        contentValues.put("full", full_url);
        contentValues.put("raw", raw_url);
        long res1 = db.insert(URI_MODEL_TABLE_NAME, null, contentValues);

        contentValues.clear();
        contentValues.put("id", user_model_id);
        contentValues.put("name", user_model_name);
        long res2 = db.insert(USER_MODEL_TABLE_NAME, null, contentValues);

        contentValues.clear();
        contentValues.put("image_id", image_id);
        contentValues.put("description", description);
        contentValues.put("user_model", user_model_id);
        contentValues.put("likes", likes);
        long res3 = db.insert(DATABASE_TABLE_NAME, null, contentValues);
        db.close();
        return (res1 != -1 && res2 != -1 && res3 != -1);
    }

    public boolean likePicture (GeneralModel model) {
        model.setLiked(true);
        return likePicture(model.getImageId(),
                model.getUserModel().getId(),
                model.getLikes(),
                model.getDescription(),
                model.getUserModel().getName(),
                model.getUriModel().getRegular(),
                model.getUriModel().getFull(),
                model.getUriModel().getRaw());
    }


    public void unlikePicture(@NonNull String image_id) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
//        db.delete(DATABASE_TABLE_NAME, "image_id = ? ", new String[]{image_id});
        final String deleteQuery_1 = "DELETE FROM " + DATABASE_TABLE_NAME + " WHERE image_id = '" + image_id + "'";
        final String deleteQuery_2 = "DELETE FROM " + URI_MODEL_TABLE_NAME + " WHERE primary_key NOT IN (SELECT image_id FROM " + DATABASE_TABLE_NAME + " )";
        final String deleteQuery_3 = "DELETE FROM " + USER_MODEL_TABLE_NAME + " WHERE id NOT IN (SELECT user_model FROM " + DATABASE_TABLE_NAME + " )";
        db.execSQL(deleteQuery_1);
        db.execSQL(deleteQuery_2);
        db.execSQL(deleteQuery_3);
        db.close();
    }

    public void unlikePicture(@NonNull GeneralModel model) {
        model.setLiked(false);
        unlikePicture(model.getImageId());
    }

    public Cursor getAllDataFromLikedTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
        return cursor;
    }

    public Cursor getAllDataFromUserModelTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_MODEL_TABLE_NAME, null);
        return cursor;
    }

    public Cursor getAllDataFromUriModelTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + URI_MODEL_TABLE_NAME, null);
        return cursor;
    }

    public Cursor getKeyDataFromUriModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + URI_MODEL_TABLE_NAME + " WHERE primary_key = '" + key + "'", null);
        return cursor;
    }

    public Cursor getKeyDataFromUserModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_MODEL_TABLE_NAME + " WHERE id = '" + key + "'", null);
        return cursor;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSqlCreate() {
        return sqlCreate;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public ArrayList<GeneralModel> getGeneralModelList () {
        ArrayList<GeneralModel> list = new ArrayList<>();
        Cursor cursor_liked = getAllDataFromLikedTable();
        if (cursor_liked != null) {
            while (cursor_liked.moveToNext()) {
                String image_id = cursor_liked.getString(0);
                String user_image_id = cursor_liked.getString(2);
                Cursor cursor_user = getKeyDataFromUserModelTable(user_image_id);
                Cursor cursor_uri = getKeyDataFromUriModelTable(image_id);
                cursor_user.moveToFirst();
                cursor_uri.moveToFirst();
                @SuppressLint("Range") GeneralModel generalModel = new GeneralModel(
                        new UriModel(cursor_uri.getString(1), cursor_uri.getString(2), cursor_uri.getString(3)),
                        cursor_liked.getString(cursor_liked.getColumnIndex("image_id")),
                        cursor_liked.getString(cursor_liked.getColumnIndex("description")),
                        cursor_liked.getString(cursor_liked.getColumnIndex("likes")),
                        new UserModel(cursor_user.getString(cursor_user.getColumnIndex("id")), cursor_user.getString(cursor_user.getColumnIndex("name")), new UserProfileImageModel("", "", "")));
                list.add(generalModel);
            }
            cursor_liked.close();
        }
        return list;
    }
}
