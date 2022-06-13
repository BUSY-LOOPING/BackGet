package com.java.proj.view;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerAccessor;
import com.java.proj.view.Utils.GlobalAppControllerService;

/**
 *Application class with globalAppControllerService
 */
public class ApplicationClass extends Application {
//    private GlobalAppController globalAppController;
//    private GlobalAppControllerService globalAppControllerService;
//    private final ServiceConnection globalAppControllerServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            globalAppControllerService = ((GlobalAppControllerService.LocalBinder) iBinder).getService();
//            if (globalAppControllerService == null) {
//                Toast.makeText(ApplicationClass.this, "service connected null", Toast.LENGTH_SHORT).show();
//            }
//            onGlobalAppControllerServiceConnected();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            globalAppControllerService = null;
//            onGlobalAppControllerServiceDisconnected();
//        }
//    };

    private static final String TAG = "ApplicationClassTag";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
//        globalAppController = new GlobalAppController(this, null);
//        doBindGlobalAppControllerService();
    }





//    @Override
//    public void onTerminate() {
//        doUnbindGlobalAppControllerService();
//        Log.d(TAG, "onTerminate: ");
//        super.onTerminate();
//    }

//    public AppEventBus getEventBus() {
//        return getGlobalAppControllerInstance().getAppEventBus();
//    }



}
