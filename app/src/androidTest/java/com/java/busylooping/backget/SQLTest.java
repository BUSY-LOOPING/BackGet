package com.java.busylooping.backget;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.java.busylooping.backget.DataBase.LikedDataBase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SQLTest {

    @Test
    public void createSQLTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LikedDataBase likedDataBase = LikedDataBase.getInstance(appContext);
        likedDataBase.getWritableDatabase();
        String err = likedDataBase.getErrorMessage();
        Assert.assertEquals(err, "");
        likedDataBase.close();
    }
}
