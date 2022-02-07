package com.java.proj.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.java.proj.view.Fragments.ParentFragment;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerAccessor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyMainActivity";

    //    private GlobalAppController globalAppController;
//    private GlobalAppControllerService globalAppControllerService;

//    private final ServiceConnection globalAppControllerServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            globalAppControllerService = ((GlobalAppControllerService.LocalBinder) iBinder).getService();
//            onGlobalAppControllerServiceConnected();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            globalAppControllerService = null;
//            onGlobalAppControllerServiceDisconnected();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //this statement makes the content full screen but does not remove status bar
        //for removing the status bar-
        hideStatusBar(decorView);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ParentFragment.newInstance(new Bundle()))
                .commit();
    }

    public static void hideStatusBar(View decorView) {
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


//    private void doBindGlobalAppControllerService() {
//        if (globalAppControllerService != null) {
//            return;
//        }
//        bindService(new Intent(MainActivity.this, GlobalAppControllerService.class), globalAppControllerServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//
//    void doUnbindGlobalAppControllerService() {
//        if (globalAppControllerService == null)
//            return;
//
//        unbindService(globalAppControllerServiceConnection);
//        globalAppControllerService = null;
//    }
//
//    private void onGlobalAppControllerServiceConnected() {
//        Log.d(TAG, "onGlobalAppControllerServiceConnected()");
//        globalAppControllerService.setGlobalAppController(globalAppController);
//        globalAppController = null;
//    }
//
//    private void onGlobalAppControllerServiceDisconnected() {
//        Log.d(TAG, "onGlobalAppControllerServiceDisconnected()");
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
//        doUnbindGlobalAppControllerService();
        super.onDestroy();
    }

    @NonNull
    public GlobalAppController globalAppController() {
        return GlobalAppControllerAccessor.getInstance(this);
    }

    @NonNull
    public AppEventBus getEventBus() {
        return globalAppController().getAppEventBus();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
