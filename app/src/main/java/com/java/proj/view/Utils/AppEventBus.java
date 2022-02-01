package com.java.proj.view.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.java.proj.view.Utils.AppEvent;

import java.lang.ref.WeakReference;

public class AppEventBus {

    private static final String APP_EVENT_PREFIX = "APP_EVENT_BUS_";
    private static final String EXTRA_EVENT = "AppEventBus.event";

    private final LocalBroadcastManager mBroadcastManager;

    public AppEventBus(Context context) {
        Context mContext = context.getApplicationContext();
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    public static class Receiver<T> extends BroadcastReceiver {
        private final WeakReference<T> mHolder;
        private final int[] mCategoryFilter;

        public Receiver (T holder, int[] categoryFilter) {
            mHolder = new WeakReference<T>(holder);
            mCategoryFilter = categoryFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            AppEvent event = intent.getParcelableExtra(EXTRA_EVENT);
            T holder =mHolder.get();

            if (event != null && holder != null) {
                onReceiveAppEvent(holder, event);
            }
        } //1

        protected void onReceiveAppEvent(T holder, AppEvent event) {
        } //2

        private int[] getCategoryFilter() {
            return mCategoryFilter;
        }
    }

    public void post(AppEvent event) {
        if (event == null)
            throw new IllegalArgumentException();

        mBroadcastManager.sendBroadcast(createEventIntent(event));
    }

    private static Intent createEventIntent(AppEvent event) {
        Intent intent = new Intent();

        intent.setAction(categoryToActionName(event.category));
        intent.putExtra(EXTRA_EVENT, event);

        return intent;
    }

    private static String categoryToActionName(int category) {
        return APP_EVENT_PREFIX + category;
    }

    public void register(Receiver<?> receiver) {
        if (receiver == null)
            throw new IllegalArgumentException();

        mBroadcastManager.registerReceiver(receiver, createIntentFilter(receiver));
    }

    private static IntentFilter createIntentFilter(Receiver<?> receiver) {
        int[] categories = receiver.getCategoryFilter();
        IntentFilter filter = new IntentFilter();

        if (categories != null) {
            for (int category : categories) {
                filter.addAction(categoryToActionName(category));
            }
        }

        return filter;
    }

}
