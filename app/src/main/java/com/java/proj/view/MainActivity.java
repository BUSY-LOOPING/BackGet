package com.java.proj.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.tabs.TabLayout;
import com.java.proj.view.MainFragment.MainFragmentPagerAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerAccessor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyMainActivity";
    public static final String MAIN_CONTAINER = "MainActivityRootContainer";

    //views
    private LottieAnimationView lottieAnimationView;
    private LinearLayout galleryBtn, customBtn;

    private FragmentManager fragmentManager;
    //    private ViewPager2 mainViewPager;
    private ViewPager mainViewPager1;
    private TabLayout mainTabLayout;
    //    private MainFragmentAdapter mainFragmentAdapter;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
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
        init();
        listeners();

        LottieAnimationView confetti = findViewById(R.id.confetti_lottie);
        confetti.setSpeed(0.5f);
        confetti.setRepeatCount(LottieDrawable.INFINITE);
    }

    private void init() {
        lottieAnimationView = findViewById(R.id.share_btn);
        galleryBtn = findViewById(R.id.galleryBtn);
        customBtn = findViewById(R.id.customBtn);
        fragmentManager = getSupportFragmentManager();

//        mainViewPager = activity.findViewById(R.id.viewPagerMain);
        mainViewPager1 = findViewById(R.id.viewPagerMain);
        mainTabLayout = findViewById(R.id.tabLayoutMain);
//        mainViewPager.setAdapter(createMainAdapter());
        mainViewPager1.setAdapter(createMainPagerAdapter());
        mainTabLayout.setupWithViewPager(mainViewPager1);
//        {
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mainTabLayout, mainViewPager,
//                    (tab, position) -> tab.setText(FragmentList.getFragment(position).toString()));
//            tabLayoutMediator.attach();
//        }

    }

    private void listeners() {
        lottieAnimationView.setOnClickListener(v -> {
            AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.SHARE_BUTTON_PRESSED.ordinal(), 0, 0);
            getEventBus().post(appEvent);
        });

        galleryBtn.setOnClickListener(v -> {
            AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.GALLERY_BUTTON_PRESSED.ordinal(), 0, 0);
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.MAIN_CONTAINER, R.id.activity_main_container);
            appEvent.extras = bundle;
            getEventBus().post(appEvent);
        });
    }

    private MainFragmentPagerAdapter createMainPagerAdapter() {
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(fragmentManager);
        return mainFragmentPagerAdapter;
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
}
