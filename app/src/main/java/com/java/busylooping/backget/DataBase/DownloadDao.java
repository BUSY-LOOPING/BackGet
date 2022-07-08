package com.java.busylooping.backget.DataBase;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.java.busylooping.backget.models.GeneralModel;

public interface DownloadDao {
    @Insert
    void insert(GeneralModel generalModel);

    @Insert
    void insertAll(GeneralModel... generalModels);

    @Delete
    void delete(GeneralModel generalModel);

    @Update
    void update(GeneralModel generalModel);

}
