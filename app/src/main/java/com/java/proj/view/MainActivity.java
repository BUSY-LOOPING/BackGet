package com.java.proj.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String MAIN_CONTAINER = "MainActivityRootContainer";
    private GlobalAppController globalAppController;
    private GlobalAppControllerService globalAppControllerService;

    private final ServiceConnection globalAppControllerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            globalAppControllerService = ((GlobalAppControllerService.LocalBinder) iBinder).getService();
            onGlobalAppControllerServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            globalAppControllerService = null;
            onGlobalAppControllerServiceDisconnected();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //this statement makes the content full screen but does not remove status bar
        //for removing the status bar-
        hideStatusBar(decorView);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        window.setStatusBarColor(Color.TRANSPARENT);


        setContentView(R.layout.activity_main);
        init();
//        listeners();

        LottieAnimationView confetti = findViewById(R.id.confetti_lottie);

        confetti.setSpeed(0.5f);
        confetti.setRepeatCount(LottieDrawable.INFINITE);
    }

    private void init() {
        globalAppController = new GlobalAppController(this, null);
        doBindGlobalAppControllerService();
    }


    private void hideStatusBar(View decorView) {
        WindowInsetsControllerCompat windowInsetsControllerCompat = ViewCompat.getWindowInsetsController(decorView);
        if (windowInsetsControllerCompat != null) {
//             Configure the behavior of the hidden system bars
            windowInsetsControllerCompat.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
            // Hide the status bar
            windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars());
        }

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideStatusBar(getWindow().getDecorView());
            }
        }, 2000);
    }

    public View getView(int id) {
        return findViewById(id);
    }

    public void switchContent(int id, Fragment fragment, ImageView imageView, String name) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, fragment, fragment.toString());
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addSharedElement(imageView, name);
        fragmentTransaction.addToBackStack("transaction");
        fragmentTransaction.commit();
    }

    public GlobalAppController getGlobalAppController() {
        return globalAppController;
    }

    private void doBindGlobalAppControllerService() {
        if (globalAppControllerService != null) {
            return;
        }
        bindService(new Intent(MainActivity.this, GlobalAppControllerService.class), globalAppControllerServiceConnection, Context.BIND_AUTO_CREATE);
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
    protected void onDestroy() {
        doUnbindGlobalAppControllerService();
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
