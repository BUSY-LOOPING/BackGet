package com.java.busylooping.backget.DataBase;

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

import com.java.busylooping.backget.R;
import com.java.busylooping.backget.models.DownloadGeneralModel;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.ProfileModel;
import com.java.busylooping.backget.models.UriModel;
import com.java.busylooping.backget.models.UserProfileImageModel;
import com.java.busylooping.backget.models.UserSocialsModel;
import com.java.busylooping.backget.utils.FileReaderUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LikedDataBase extends SQLiteOpenHelper {
    private static final String TAG = "mysql";
    private static final String DATABASE_NAME = "likedDatabase.db";
    private static final String DATABASE_TABLE_NAME = "liked_table";
    private static final String URI_MODEL_TABLE_NAME = "uri_model_table";
    private static final String USER_MODEL_TABLE_NAME = "user_model_table";
    private static final String USER_PROFILE_IMAGE_MODEL_TABLE_NAME = "user_profile_image_table";
    private static final String DOWNLOAD_TABLE_NAME = "download_table";
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
        sqLiteDatabase.execSQL("drop table if exists " + DOWNLOAD_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertOrUpdatePicture(@NonNull String image_id,
                                         @NonNull String user_model_id,
                                         @NonNull String user_model_username,
                                         @NonNull String likes,
                                         @Nullable String views,
                                         @Nullable String description,
                                         @NonNull String user_model_name,
                                         @NonNull String user_model_bio,
                                         @NonNull String regular_url,
                                         @NonNull String full_url,
                                         @NonNull String raw_url,
                                         @NonNull String small_user_profile_url,
                                         @NonNull String medium_user_profile_url,
                                         @NonNull String large_user_profile_url,
                                         boolean isLiked) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", user_model_id);
        contentValues.put("name", user_model_name);
        contentValues.put("bio", user_model_bio);
        contentValues.put("username", user_model_username);
        long res3 = db.insertWithOnConflict(USER_MODEL_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (res3 == -1) {
            res3 = db.update(USER_MODEL_TABLE_NAME, contentValues, "user_id =?", new String[]{user_model_id});
            Log.d(TAG, "insertOrUpdatePicture: USER_MODEL_TABLE_NAME update " + res3);

        }
//        if (isLiked) {
//            likes = ("" + (Integer.parseInt(likes) - 1));
//        }

        contentValues.clear();
        contentValues.put("image_id", image_id);
        contentValues.put("description", description);
        contentValues.put("user_model", user_model_id);
        contentValues.put("likes", likes);
        contentValues.put("views", views);
        contentValues.put("liked", isLiked);
        long res4 = db.insertWithOnConflict(DATABASE_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (res4 == -1) {
            res4 = db.update(DATABASE_TABLE_NAME, contentValues, "image_id =?", new String[]{image_id});
            Log.d(TAG, "insertOrUpdatePicture: DATABASE_TABLE_NAME update " + res4);

        }

        contentValues.clear();
        contentValues.put("primary_key", image_id);
        contentValues.put("regular", regular_url);
        contentValues.put("full", full_url);
        contentValues.put("raw", raw_url);
        long res1 = db.insertWithOnConflict(URI_MODEL_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (res1 == -1) {
            res1 = db.update(URI_MODEL_TABLE_NAME, contentValues, "primary_key =?", new String[]{image_id});
            Log.d(TAG, "insertOrUpdatePicture: URI_MODEL_TABLE_NAME update " + res1);

        }

        contentValues.clear();
        contentValues.put("id", user_model_id);
        contentValues.put("small", small_user_profile_url);
        contentValues.put("medium", medium_user_profile_url);
        contentValues.put("large", large_user_profile_url);
        long res2 = db.insert(USER_PROFILE_IMAGE_MODEL_TABLE_NAME, null, contentValues);
        if (res2 == -1) {
            res2 = db.update(USER_PROFILE_IMAGE_MODEL_TABLE_NAME, contentValues, "id =?", new String[]{user_model_id});
        }


//        db.close();
        return (res1 != -1 && res2 != -1 && res3 != -1 && res4 != -1);
    }

    public boolean insertOrUpdatePicture(GeneralModel model) {
        return insertOrUpdatePicture(model.getImageId(),
                model.getProfileModel().getId(),
                model.getProfileModel().getUsername(),
                model.getLikes(),
                model.getViews(),
                model.getDescription(),
                model.getProfileModel().getName(),
                model.getProfileModel().getBio(),
                model.getUriModel().getRegular(),
                model.getUriModel().getFull(),
                model.getUriModel().getRaw(),
                model.getProfileModel().getUserProfileImageModel().getSmall(),
                model.getProfileModel().getUserProfileImageModel().getMedium(),
                model.getProfileModel().getUserProfileImageModel().getLarge(),
                model.isLiked());
    }

    public boolean downloadPicture(@NonNull DownloadGeneralModel downloadGeneralModel) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        insertOrUpdatePicture(downloadGeneralModel);
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", downloadGeneralModel.getImageId());
        contentValues.put("path", downloadGeneralModel.getSavePath());
        contentValues.put("date", downloadGeneralModel.getDate());
        contentValues.put("download_res", downloadGeneralModel.getDownloadRes());
        long res = db.insert(DOWNLOAD_TABLE_NAME, null, contentValues);
        return res == -1;
    }

    @SuppressLint("Range")
    public void unlikePicture(@NonNull String image_id) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("liked", false);
        db.update(DATABASE_TABLE_NAME, contentValues, "image_id =?", new String[]{image_id});
//        db.delete(DATABASE_TABLE_NAME, "image_id =?", new String[]{image_id});
        final String deleteQuery_1 = "DELETE FROM " + DATABASE_TABLE_NAME + " WHERE NOT EXISTS (SELECT * FROM " + DOWNLOAD_TABLE_NAME + " WHERE " + DOWNLOAD_TABLE_NAME + ".id = '" + image_id + "' AND " + DATABASE_TABLE_NAME + ".image_id = " + DOWNLOAD_TABLE_NAME + ".id )";
        try {
            db.execSQL(deleteQuery_1);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final String deleteQuery_3 = "UPDATE " + DATABASE_TABLE_NAME + " SET likes  = likes - 1 WHERE image_id = '" + image_id + "'";
//        db.execSQL(deleteQuery_3);


        final String deleteQuery_2 = "DELETE FROM " + USER_MODEL_TABLE_NAME + " WHERE user_id NOT IN (SELECT user_model FROM " + DATABASE_TABLE_NAME + " )";
        db.execSQL(deleteQuery_2);
        db.close();
    }

    public void unlikePicture(@NonNull GeneralModel model) {
        unlikePicture(model.getImageId());
    }

    public Cursor getAllDataFromLikedTable(boolean onlyLikes) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        if (onlyLikes)
            return db.rawQuery("select * from " + DATABASE_TABLE_NAME + " where liked =?", new String[]{"1"});
        return db.rawQuery("select * from " + DATABASE_TABLE_NAME, null);
    }

    public Cursor getAllDataFromDownloadTable() {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + DOWNLOAD_TABLE_NAME, null);
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

    public Cursor getKeyDataFromLikedModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + DATABASE_TABLE_NAME + " WHERE image_id = ?", new String[]{key});
    }

    public Cursor getKeyDataFromUriModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
//        return db.rawQuery("select * from " + URI_MODEL_TABLE_NAME + " WHERE primary_key = '" + key + "'", null);
        return db.rawQuery("select * from " + URI_MODEL_TABLE_NAME + " WHERE primary_key =?  ", new String[]{key});
    }

    public Cursor getKeyDataFromUserModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        return db.rawQuery("select * from " + USER_MODEL_TABLE_NAME + " WHERE user_id =? ", new String[]{key});
    }

    public Cursor getKeyDataFromUserProfileModelTable(@NonNull String key) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
//        return db.rawQuery("select * from " + USER_PROFILE_IMAGE_MODEL_TABLE_NAME + " WHERE id = '" + key + "'", null);
        return db.rawQuery("select * from " + USER_PROFILE_IMAGE_MODEL_TABLE_NAME + " WHERE id =? ", new String[]{key});
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

    @SuppressLint("Range")
    @Nullable
    private GeneralModel getGeneralModel(@NonNull Cursor cursor_liked) {
        GeneralModel generalModel = null;
        boolean res = true;
        String image_id = cursor_liked.getString(0);
        String user_image_id = cursor_liked.getString(2);
        Cursor cursor_user = getKeyDataFromUserModelTable(user_image_id);
        Cursor cursor_user_profile = getKeyDataFromUserProfileModelTable(user_image_id);
        Cursor cursor_uri = getKeyDataFromUriModelTable(image_id);
        res = cursor_user.moveToFirst();
        res &= cursor_uri.moveToFirst();
        res &= cursor_user_profile.moveToFirst();

        if (res) {
            generalModel = new GeneralModel(
                    new UriModel(cursor_uri.getString(1), cursor_uri.getString(2), cursor_uri.getString(3)),
                    cursor_liked.getString(cursor_liked.getColumnIndex("image_id")),
                    cursor_liked.getString(cursor_liked.getColumnIndex("description")),
                    cursor_liked.getString(cursor_liked.getColumnIndex("likes")),
                    new ProfileModel(cursor_user.getString(cursor_user.getColumnIndex("user_id")),
                            cursor_user.getString(cursor_user.getColumnIndex("username")),
                            cursor_user.getString(cursor_user.getColumnIndex("name")),
                            cursor_user.getString(cursor_user.getColumnIndex("first_name")),
                            cursor_user.getString(cursor_user.getColumnIndex("last_name")),
                            cursor_user.getString(cursor_user.getColumnIndex("email")),
                            cursor_user.getString(cursor_user.getColumnIndex("downloads")),
                            cursor_user.getString(cursor_user.getColumnIndex("bio")),
                            cursor_user.getString(cursor_user.getColumnIndex("for_hire")).equals("1"),
                            new UserProfileImageModel(
                                    cursor_user_profile.getString(cursor_user_profile.getColumnIndex("small")),
                                    cursor_user_profile.getString(cursor_user_profile.getColumnIndex("medium")),
                                    cursor_user_profile.getString(cursor_user_profile.getColumnIndex("large"))),
                            new UserSocialsModel(cursor_user.getString(
                                    cursor_user.getColumnIndex("instagram_username")),
                                    cursor_user.getString(cursor_user.getColumnIndex("portfolio_url")),
                                    cursor_user.getString(cursor_user.getColumnIndex("twitter_username")))),
                    null,
                    cursor_liked.getString(cursor_liked.getColumnIndex("liked")).equals("1"),
                    cursor_liked.getString(cursor_liked.getColumnIndex("views")));
        }
        return generalModel;
    }

    public Map<String, GeneralModel> getLikedGeneralModelMap() {
        Map<String, GeneralModel> hashMap = new HashMap<>();
        Cursor cursor_liked = getAllDataFromLikedTable(true);
        if (cursor_liked != null) {
            while (cursor_liked.moveToNext()) {
//                String image_id = cursor_liked.getString(0);
//                String user_image_id = cursor_liked.getString(2);
//                Cursor cursor_user = getKeyDataFromUserModelTable(user_image_id);
//                Cursor cursor_user_profile = getKeyDataFromUserProfileModelTable(user_image_id);
//                Cursor cursor_uri = getKeyDataFromUriModelTable(image_id);
//                cursor_user.moveToFirst();
//                cursor_uri.moveToFirst();
//                cursor_user_profile.moveToFirst();
//                @SuppressLint("Range") GeneralModel generalModel = new GeneralModel(
//                        new UriModel(cursor_uri.getString(1), cursor_uri.getString(2), cursor_uri.getString(3)),
//                        cursor_liked.getString(cursor_liked.getColumnIndex("image_id")),
//                        cursor_liked.getString(cursor_liked.getColumnIndex("description")),
//                        cursor_liked.getString(cursor_liked.getColumnIndex("likes")),
//                        new UserModel(cursor_user.getString(cursor_user.getColumnIndex("user_id")),
//                                cursor_user.getString(cursor_user.getColumnIndex("name")),
//                                cursor_user.getString(cursor_user.getColumnIndex("bio")),
//                                cursor_user.getString(cursor_user.getColumnIndex("username")),
//                                new UserProfileImageModel(
//                                        cursor_user_profile.getString(cursor_user_profile.getColumnIndex("small")),
//                                        cursor_user_profile.getString(cursor_user_profile.getColumnIndex("medium")),
//                                        cursor_user_profile.getString(cursor_user_profile.getColumnIndex("large")))),
//                        null, true,
//                        cursor_liked.getString(cursor_liked.getColumnIndex("views")));
                GeneralModel generalModel = getGeneralModel(cursor_liked);
                if (generalModel != null)
                    hashMap.put(generalModel.getImageId(), generalModel);
            }
            cursor_liked.close();
        }
        return hashMap;
    }

    @SuppressLint("Range")
    public ArrayList<DownloadGeneralModel> getDownloadList() {
        ArrayList<DownloadGeneralModel> list = new ArrayList<>();
        Cursor download_cursor = getAllDataFromDownloadTable();
        if (download_cursor != null) {
            while (download_cursor.moveToNext()) {
                String image_id = download_cursor.getString(download_cursor.getColumnIndex("id"));
                Cursor liked_cursor = getKeyDataFromLikedModelTable(image_id);
                if (liked_cursor.moveToFirst()) {
                    GeneralModel generalModel = getGeneralModel(liked_cursor);
                    if (generalModel != null) {
                        DownloadGeneralModel downloadGeneralModel = new DownloadGeneralModel(generalModel,
                                download_cursor.getString(download_cursor.getColumnIndex("date")),
                                download_cursor.getString(download_cursor.getColumnIndex("path")),
                                Integer.parseInt(download_cursor.getString(download_cursor.getColumnIndex("download_res"))));
                        list.add(downloadGeneralModel);
                    }
                }
            }
            download_cursor.close();
        }

        return list;
    }
}
