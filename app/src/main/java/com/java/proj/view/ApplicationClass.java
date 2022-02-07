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
public class ApplicationClass extends Application
        implements GlobalAppControllerAccessor.Provider {
    private GlobalAppController globalAppController;
    private GlobalAppControllerService globalAppControllerService;
    private final ServiceConnection globalAppControllerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            globalAppControllerService = ((GlobalAppControllerService.LocalBinder) iBinder).getService();
            if (globalAppControllerService == null) {
                Toast.makeText(ApplicationClass.this, "service connected null", Toast.LENGTH_SHORT).show();
            }
            onGlobalAppControllerServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            globalAppControllerService = null;
            onGlobalAppControllerServiceDisconnected();
        }
    };

    private static final String TAG = "ApplicationClassTag";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        globalAppController = new GlobalAppController(this, null);
        doBindGlobalAppControllerService();
    }


    private void doBindGlobalAppControllerService() {
        if (globalAppControllerService != null) {
            return;
        }
        bindService(new Intent(ApplicationClass.this, GlobalAppControllerService.class), globalAppControllerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindGlobalAppControllerService() {
        if (globalAppControllerService == null)
            return;

        unbindService(globalAppControllerServiceConnection);
        globalAppControllerService = null;
    }

    private void onGlobalAppControllerServiceConnected() {
        Log.d(TAG, "onGlobalAppControllerServiceConnected()");
        globalAppControllerService.setGlobalAppController(globalAppController);
        globalAppController = null;
    }

    private void onGlobalAppControllerServiceDisconnected() {
        Log.d(TAG, "onGlobalAppControllerServiceDisconnected()");
    }


    @Override
    public void onTerminate() {
        doUnbindGlobalAppControllerService();
        Log.d(TAG, "onTerminate: ");
        super.onTerminate();
    }

    public AppEventBus getEventBus() {
        return getGlobalAppControllerInstance().getAppEventBus();
    }

    @Override
    public GlobalAppController getGlobalAppControllerInstance() {
        if (globalAppControllerService != null) {

            GlobalAppController globalAppController = globalAppControllerService.getGlobalAppController();
            if (globalAppController == null) {
                Log.d(TAG, "getGlobalAppController: null");
            } else {
                Log.d(TAG, "getGlobalAppController: not null");
            }
            return globalAppController;
        } else {
            return globalAppController;
        }
    }

}
