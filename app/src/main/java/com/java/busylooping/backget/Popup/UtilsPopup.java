package com.java.busylooping.backget.Popup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.java.busylooping.backget.AppBaseBottomSheetDialogFragment;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.utils.AppEvent;
import com.java.busylooping.backget.utils.EventDef;
import com.java.busylooping.backget.utils.GlobalAppController;
import com.java.busylooping.backget.utils.GlobalAppControllerService;


public class UtilsPopup extends AppBaseBottomSheetDialogFragment implements View.OnClickListener {
    private Context context;
    private Bundle bundle;
    private GeneralModel generalModel;
    private LinearLayout download_1080_btn, download_full_btn,
            setAsHomeScreenWallpaperBtn, setAsLockScreenWallpaperBtn, setAsBothBtn, closeBtn;
    private DownloadManager manager;

    public static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final String GENERAL_MODEL = "UtilsPopupGeneralModel";


//    public static class AppEventReceiver extends AppEventBus.Receiver<UtilsPopup>{
//        private static final int[] FILTER = new int[]{
//                EventDef.Category.PERMISSION
//        };
//        public AppEventReceiver(UtilsPopup holder, int[] categoryFilter) {
//            super(holder, categoryFilter);
//        }
//
//        @Override
//        protected void onReceiveAppEvent(UtilsPopup holder, AppEvent event) {
//            holder.onReceiveAppEvent(event);
//        }
//    }
//    private AppEventReceiver eventReceiver;

    void onReceiveAppEvent(@NonNull AppEvent event) {
        switch (event.category) {
            case EventDef.Category.PERMISSION:
                handlePermissionEvents(event);
                break;
        }
    }

    private void handlePermissionEvents(@NonNull AppEvent appEvent) {

    }

    public UtilsPopup() {

    }

    public static UtilsPopup newInstance(Bundle bundle) {
        UtilsPopup utilsPopup = new UtilsPopup();
        utilsPopup.setArguments(bundle);
        return utilsPopup;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
            generalModel = (GeneralModel) bundle.getSerializable(GENERAL_MODEL);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            permissions = new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE};
//        } else {
//            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        }
//        downloadRegularClicked(bundle.getString(DOWNLOAD_URI));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
//        eventReceiver = new AppEventReceiver(this, AppEventReceiver.FILTER);
//        eventBus(context).register(eventReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        eventBus(context).unregister(eventReceiver);
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_bottomsheet, container, false);
        init(view);
        listeners();
        return view;

    }

    private void listeners() {
        download_1080_btn.setOnClickListener(this);
        download_full_btn.setOnClickListener(this);
        setAsHomeScreenWallpaperBtn.setOnClickListener(this);
        setAsLockScreenWallpaperBtn.setOnClickListener(this);
        setAsBothBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }

    private void init(View view) {
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        download_1080_btn = view.findViewById(R.id.download_1080_btn);
        download_full_btn = view.findViewById(R.id.download_full_btn);
        setAsHomeScreenWallpaperBtn = view.findViewById(R.id.setAsHomeScreenBtn);
        setAsLockScreenWallpaperBtn = view.findViewById(R.id.setAsLockScreenBtn);
        setAsBothBtn = view.findViewById(R.id.setAsHomeNLockScreenBtn);
        closeBtn = view.findViewById(R.id.closeBtn);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int flag = EventDef.Category.NONE;
        switch (view.getId()) {
            case R.id.download_1080_btn:
                flag = EventDef.DOWNLOAD_OPTIONS.download_1080;
                break;
            case R.id.download_full_btn:
                flag = EventDef.DOWNLOAD_OPTIONS.download_full;
                break;
            case R.id.setAsHomeScreenBtn:
                flag = EventDef.WALLPAPER_OPTIONS.home_screen_wallpaper;
                break;
            case R.id.setAsLockScreenBtn:
                flag = EventDef.WALLPAPER_OPTIONS.lock_screen_wallpaper;
                break;
            case R.id.setAsHomeNLockScreenBtn:
                flag = EventDef.WALLPAPER_OPTIONS.home_n_lock_screen_wallpaper;
                break;
        }
        handleEvent(flag);
        dismiss();
    }

    private void handleWallpaperEvent(int flag) {
        Intent intentService = new Intent(context, GlobalAppControllerService.class);
        intentService.putExtra(GlobalAppControllerService.FLAGS, flag);
        intentService.putExtra(GlobalAppController.GENERAL_MODEL, generalModel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }


    private void handleEvent(int flag) {
        if (flag != EventDef.Category.NONE) {
            if (hasPermissions()) {
                Intent intentService = new Intent(context, GlobalAppControllerService.class);
                intentService.putExtra(GlobalAppControllerService.FLAGS, flag);
                intentService.putExtra(GlobalAppController.GENERAL_MODEL, generalModel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intentService);
                } else {
                    context.startService(intentService);
                }
            } else {
                ((MainActivity) context).requestPermissions();
            }
        }
    }


    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return Environment.isExternalStorageManager();
        return ActivityCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_GRANTED;
    }
}
