package com.java.busylooping.backget;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.java.busylooping.backget.utils.AppEventBus;
import com.java.busylooping.backget.utils.GlobalAppController;
import com.java.busylooping.backget.utils.GlobalAppControllerAccessor;

public class AppBaseActivity extends AppCompatActivity {

    protected GlobalAppController getAppController() {
        return GlobalAppControllerAccessor.getInstance(this);
    }

    protected AppEventBus eventBus() {
        return GlobalAppControllerAccessor.getInstance(this).getAppEventBus();
    }

    protected static void hideStatusBar(View decorView) {
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

    protected static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
