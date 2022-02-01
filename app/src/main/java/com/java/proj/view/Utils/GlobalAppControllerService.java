package com.java.proj.view.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class GlobalAppControllerService extends Service {
    private GlobalAppController globalAppController;
    private static final String TAG = "GlobalAppControllerSvc";

    //internal class
    /*
    * LocalServiceBinder is in extended class of binder which holds the reference to the service
    * in a weak reference. The service type is GlobalAppControllerService.
    * So, in onBind, new instance of LocalBinder with current class reference as parameter is returned.
    * */
    public static class LocalBinder extends LocalServiceBinder<GlobalAppControllerService> {
        public LocalBinder(GlobalAppControllerService service) {
            super(service);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return new LocalBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    /**
     * START_STICKY tells the OS to recreate the service after it has enough memory and call
     * onStartCommand() again with a null intent. START_NOT_STICKY tells the OS to not bother
     * recreating the service again. There is also a third code START_REDELIVER_INTENT that
     * tells the OS to recreate the service and redeliver the same intent to onStartCommand()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand(flags = " + flags + ", startId = " + startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    public GlobalAppController getGlobalAppController() {
        return globalAppController;
    }

    public void setGlobalAppController(GlobalAppController globalAppController) {
        this.globalAppController = globalAppController;
        if (globalAppController != null) {
            globalAppController.bindHolderService(this);
        }
    }
}
