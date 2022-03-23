package com.java.proj.view.Popup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.proj.view.R;

public class UtilsPopup extends BottomSheetDialogFragment {
    private Context context;
    private Bundle bundle;

    public static final String DOWNLOAD_URI = "DownloadPopupDownloadUri";

    public UtilsPopup() {

    }

    public static UtilsPopup newInstance(Bundle bundle) {
        UtilsPopup downloadPopup = new UtilsPopup();
        downloadPopup.setArguments(bundle);
        return downloadPopup;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
//        downloadRegularClicked(bundle.getString(DOWNLOAD_URI));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        return view;

    }

    private void init(View view) {

    }

    public void downloadRegularClicked(String uri) {
        startDownload(uri);
    }

    public void startDownload(String downloadUri) {
        Glide.with(context)
                .load(downloadUri)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        saveToStorage(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void saveToStorage(Drawable resource) {
        if (checkWritePermission()) {
            permissionGranted();
        } else {
            requestWritePermission();
        }
    }

    private boolean checkWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            try {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse(String.format("package:%s",context.getPackageName())));
//                ((Activity)context).startActivityForResult(intent, 2296);
//            } catch (Exception e) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                ((Activity)context).startActivityForResult(intent, 2296);
//            }
        }
    }

    private void permissionGranted() {
        Toast.makeText(context, "granted", Toast.LENGTH_SHORT).show();
    }
}
