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
import java.util.HashMap;
import java.util.Map;

public class LikedDataBase extends SQLiteOpenHelper {
    private static final String TAG = "mysql";
    private static final String DATABASE_NAME = "likedDatabase.db";
    private static final String DATABASE_TABLE_NAME = "liked_table";
    private static final String URI_MODEL_TABLE_NAME = "uri_model_table";
    private static final String USER_MODEL_TABLE_NAME = "user_model_table";
    private static final String USER_PROFILE_IMAGE_MODEL_TABLE_NAME = "user_profile_image_table";
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
        sqLiteDatabase.execSQL("drop table if exists " + USER_PROFILE_IMAGE_MODEL_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean likePicture(@NonNull String image_id,
                               @NonNull String user_model_id,
                               @NonNull String user_model_username,
                               @NonNull String likes,
                               @NonNull String description,
                               @NonNull String user_model_name,
                               @NonNull String user_model_bio,
                               @NonNull String regular_url,
                               @NonNull String full_url,
                               @NonNull String raw_url,
                               @NonNull String small_user_profile_url,
                               @NonNull String medium_user_profile_url,
                               @NonNull String large_user_profile_url) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("primary_key", image_id);
        contentValues.put("regular", regular_url);
        contentValues.put("full", full_url);
        contentValues.put("raw", raw_url);
        long res1 = db.insert(URI_MODEL_TABLE_NAME, null, contentValues);

        contentValues.clear();
        contentValues.put("id", user_model_id);
        contentValues.put("small", small_user_profile_url);
        contentValues.put("medium", medium_user_profile_url);
        contentValues.put("large", large_user_profile_url);
        long res2 = db.insert(USER_PROFILE_IMAGE_MODEL_TABLE_NAME, null, contentValues);

        contentValues.clear();
        contentValues.put("id", user_model_id);
        contentValues.put("name", user_model_name);
        contentValues.put("bio", user_model_bio);
        contentValues.put("username", user_model_username);
        long res3 = db.insert(USER_MODEL_TABLE_NAME, null, contentValues);

        contentValues.clear();
        contentValues.put("image_id", image_id);
        contentValues.put("description", description);
        contentValues.put("user_model", user_model_id);
        contentValues.put("likes", likes);
        long res4 = db.insert(DATABASE_TABLE_NAME, null, contentValues);

        db.close();
        return (res1 != -1 && res2 != -1 && res3 != -1 && res4 != -1);
    }

    public boolean likePicture(GeneralModel model) {
        model.setLikes(Integer.parseInt(model.getLikes()) + 1 + "");
        model.setLiked(true);
        return likePicture(model.getImageId(),
                model.getUserModel().getId(),
                model.getUserModel().getUsername(),
                model.getLikes(),
                model.getDescription(),
                model.getUserModel().getName(),
                model.getUserModel().getBio(),
                model.getUriModel().getRegular(),
                model.getUriModel().getFull(),
                model.getUriModel().getRaw(),
                model.getUserModel().getUserProfileImageModel().getSmall(),
                model.getUserModel().getUserProfileImageModel().getMedium(),
                model.getUserModel().getUserProfileImageModel().getLarge());
    }


    public void unlikePicture(@NonNull String image_id) {

        SQLiteDatabase db = getInstance(context).getWritableDatabase();
//        db.delete(DATABASE_TABLE_NAME, "image_id = ? ", new String[]{image_id});
        final String deleteQuery_1 = "DELETE FROM " + DATABASE_TABLE_NAME + " WHERE image_id = '" + image_id + "'";
        final String deleteQuery_2 = "DELETE FROM " + URI_MODEL_TABLE_NAME + " WHERE primary_key NOT IN (SELECT image_id FROM " + DATABASE_TABLE_NAME + " )";
        final String deleteQuery_3 = "DELETE FROM " + USER_MODEL_TABLE_NAME + " WHERE id NOT IN (SELECT user_model FROM " + DATABASE_TABLE_NAME + " )";
        final String deleteQuery_4 = "DELETE FROM " + USER_PROFILE_IMAGE_MODEL_TABLE_NAME + " WHERE id NOT IN (SELECT user_model FROM " + DATABASE_TABLE_NAME + " )";
        db.execSQL(deleteQuery_1);
        db.execSQL(deleteQuery_2);
        db.execSQL(deleteQuery_3);
        db.execSQL(deleteQuery_4);
        db.close();
    }

    public void unlikePicture(@NonNull GeneralModel model) {
        model.setLikes(Integer.parseInt(model.getLikes()) - 1 + "");
        model.setLiked(false);
        unlikePicture(model.getImageId());
    }

    public Cursor getAllDataFromLikedTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
    }

    public Cursor getAllDataFromUserModelTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + USER_MODEL_TABLE_NAME, null);
    }

    public Cursor getAllDataFromUriModelTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + URI_MODEL_TABLE_NAME, null);
    }

    public Cursor getAllDataFromUserProfileModelTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + USER_PROFILE_IMAGE_MODEL_TABLE_NAME, null);
    }

    public Cursor getKeyDataFromUriModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + URI_MODEL_TABLE_NAME + " WHERE primary_key = '" + key + "'", null);
    }

    public Cursor getKeyDataFromUserModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + USER_MODEL_TABLE_NAME + " WHERE id = '" + key + "'", null);
    }

    public Cursor getKeyDataFromUserProfileModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + USER_PROFILE_IMAGE_MODEL_TABLE_NAME + " WHERE id = '" + key + "'", null);
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

    public Map<String, GeneralModel> getGeneralModelList() {
        Map<String, GeneralModel> hashMap = new HashMap<>();
        Cursor cursor_liked = getAllDataFromLikedTable();
        if (cursor_liked != null) {
            while (cursor_liked.moveToNext()) {
                String image_id = cursor_liked.getString(0);
                String user_image_id = cursor_liked.getString(2);
                Cursor cursor_user = getKeyDataFromUserModelTable(user_image_id);
                Cursor cursor_user_profile = getKeyDataFromUserProfileModelTable(user_image_id);
                Cursor cursor_uri = getKeyDataFromUriModelTable(image_id);
                cursor_user.moveToFirst();
                cursor_uri.moveToFirst();
                cursor_user_profile.moveToFirst();
                @SuppressLint("Range") GeneralModel generalModel = new GeneralModel(
                        new UriModel(cursor_uri.getString(1), cursor_uri.getString(2), cursor_uri.getString(3)),
                        cursor_liked.getString(cursor_liked.getColumnIndex("image_id")),
                        cursor_liked.getString(cursor_liked.getColumnIndex("description")),
                        cursor_liked.getString(cursor_liked.getColumnIndex("likes")),
                        new UserModel(cursor_user.getString(cursor_user.getColumnIndex("id")),
                                cursor_user.getString(cursor_user.getColumnIndex("name")),
                                cursor_user.getString(cursor_user.getColumnIndex("bio")),
                                cursor_user.getString(cursor_user.getColumnIndex("username")),
                                new UserProfileImageModel(
                                        cursor_user_profile.getString(cursor_user_profile.getColumnIndex("small")),
                                        cursor_user_profile.getString(cursor_user_profile.getColumnIndex("medium")),
                                        cursor_user_profile.getString(cursor_user_profile.getColumnIndex("large")))),
                        null, true);
                hashMap.put(generalModel.getImageId(), generalModel);
            }
            cursor_liked.close();
        }
        return hashMap;
    }
}
