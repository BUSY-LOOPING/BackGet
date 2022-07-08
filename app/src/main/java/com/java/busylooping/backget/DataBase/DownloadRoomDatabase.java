//package com.java.busylooping.backget.DataBase;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.java.busylooping.backget.models.DownloadGeneralModel;
//
//@Database(entities = {DownloadGeneralModel.class}, version = 1)
//public abstract class DownloadRoomDatabase extends RoomDatabase {
//    public abstract DownloadDao downloadDao();
//
//    public DownloadRoomDatabase getInstance(@NonNull Context context) {
//        return Room.databaseBuilder(context,DownloadRoomDatabase.class ,"download").build();
//    }
//}
