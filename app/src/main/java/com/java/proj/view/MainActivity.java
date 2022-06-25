package com.java.proj.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.java.proj.view.Fragments.ParentFragment;
import com.java.proj.view.Models.AccessToken;
import com.java.proj.view.Popup.UtilsPopup;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerAccessor;
import com.java.proj.view.Utils.GlobalAppControllerService;
import com.java.proj.view.Utils.NetworkStateReceiver;
import com.java.proj.view.api.ApiUtilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        NetworkStateReceiver.NetworkStateReceiverListener, GlobalAppControllerAccessor.Provider{
    private static final String TAG = "MyMainActivity";

    private NetworkStateReceiver networkStateReceiver;

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
        init();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ParentFragment.newInstance(new Bundle()), "ParentFragment")
                .commit();
    }

    private void init() {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //this statement makes the content full screen but does not remove status bar
        //for removing the status bar-
        hideStatusBar(decorView);

        globalAppController = new GlobalAppController(this, null);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        doBindGlobalAppControllerService();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = getIntent().getData();
        Log.d(TAG, "onResume: uri = " + uri);
        if (uri != null && uri.toString().startsWith(ApiUtilities.REDIRECT_URI2)) {
            String code = uri.getQueryParameter("code");
            ApiUtilities.getApiInterfaceForBase2().getAccessToken(ApiUtilities.API_KEY, ApiUtilities.API_SECRET, ApiUtilities.REDIRECT_URI2, code, "authorization_code").enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                    if (response.body() != null) {
                        String accessToken = response.body().getAccessToken();
                        globalAppController().setAccessToken(accessToken);
                        Log.d(TAG, "onResponse: " + accessToken);
                    } else {
                        Log.d(TAG, "onResponse: null");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: ");
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
//        doUnbindGlobalAppControllerService();
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
        doUnbindGlobalAppControllerService();
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
            if (v instanceof TextView) {
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

    @Override
    public void networkAvailable() {

    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(this, "No internet connection detected!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
//            if (requestCode == EventDef.REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //do
//                } else {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
//                        //User has denied permission but not permanently
//                        ActivityCompat.requestPermissions(this, permissions, EventDef.REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE);
//                    } else {
//                        Toast.makeText(this, "Provide write permission to proceed", Toast.LENGTH_SHORT).show();
//                        // Permission denied permanently.
//                        // open Permission setting's page from here now.
//                        Intent intent;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                            intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                        } else {
//                            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        }
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivity(intent);
//                    }
////                    ActivityCompat.requestPermissions(this, permissions, EventDef.REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE);
//                }
//            }
        }
//        // Forward results to EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public GlobalAppController getGlobalAppControllerInstance() {
        if (globalAppControllerService != null) {
            return globalAppControllerService.getGlobalAppController();
        } else {
            return globalAppController;
        }
    }

    private void doBindGlobalAppControllerService() {
        if (globalAppControllerService != null) {
            return;
        }
        Log.d(TAG, "doBindGlobalAppControllerService: ");
        bindService(new Intent(this, GlobalAppControllerService.class), globalAppControllerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindGlobalAppControllerService() {
        if (globalAppControllerService == null)
            return;

        unbindService(globalAppControllerServiceConnection);
        globalAppControllerService = null;
        Log.d(TAG, "doUnbindGlobalAppControllerService: ");
    }

    private void onGlobalAppControllerServiceConnected() {
        Log.d(TAG, "onGlobalAppControllerServiceConnected()");
        globalAppControllerService.setGlobalAppController(globalAppController);
        globalAppController = null;
    }

    private void onGlobalAppControllerServiceDisconnected() {
        Log.d(TAG, "onGlobalAppControllerServiceDisconnected()");
    }

    public void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, UtilsPopup.permissions[0])) {
            //User has denied permission but not permanently
            ActivityCompat.requestPermissions(this, UtilsPopup.permissions, EventDef.REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission denied permanently.
            // open Permission setting's page from here now.
            new MaterialAlertDialogBuilder(this, R.style.permission_style)
                    .setTitle("App permissions required")
                    .setMessage("This app may not work correctly without the requested permissions.\nOpen the app settings screen to modify app permissions.")
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with yes operation
                            Intent intent;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            } else {
                                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            }
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with cancel operation
                            dialog.dismiss();
                        }
                    }).show();

        }
    }


//    @Override
//    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
//        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//                new AppSettingsDialog.Builder(this)
//                        .setThemeResId(R.style.permission_style_app_settings)
//                        .build().show();
//        }
////        else {
////            requestWritePermission();
////        }
//    }
//
//    @Override
//    public void onRationaleAccepted(int requestCode) {
//
//    }
//
//    @Override
//    public void onRationaleDenied(int requestCode) {
//
//    }
//
//    public void requestWritePermission() {

//        EasyPermissions.requestPermissions(
//                new PermissionRequest.Builder(this, EventDef.REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE, UtilsPopup.permissions)
//                        .setRationale("App requires Write Permission")
//                        .setTheme(R.style.permission_style)
//                        .build());
//    }
}
