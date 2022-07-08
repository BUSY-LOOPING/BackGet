package com.java.busylooping.backget;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DownloadModelTest {
    public static final String TAG = "DownloadModelTest";

    @Test
    public void modelTest() {
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        LikedDataBase likedDataBase = LikedDataBase.getInstance(context);
//        Map.Entry<String, GeneralModel> entry= likedDataBase.getGeneralModelList().entrySet().iterator().next();
//        GeneralModel generalModel = entry.getValue();
//        DownloadGeneralModel model = new DownloadGeneralModel(generalModel, EventDef.DOWNLOAD_OPTIONS.download_1080);
//        for (Field field : generalModel.getClass().getDeclaredFields()) {
//            Log.d(TAG, "outer loop: " );
//            if (field.isAnnotationPresent(ExposedName.class)) {
//                field.setAccessible(true);
//                ExposedName[] arr = field.getAnnotationsByType(ExposedName.class);
//                for (ExposedName exposedName : arr) {
//                    Log.d(TAG, "name: " + exposedName.name());
//                    Log.d(TAG, "name: " + exposedName.getClass());
//                    System.out.println(exposedName.name());
//                }
//                Object obj = null;
//                try {
//                    obj = field.get(generalModel);
//                    if (obj instanceof String) {
//                        String str = (String) obj;
//                        Log.d(TAG, "modelTest: " + str);
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
