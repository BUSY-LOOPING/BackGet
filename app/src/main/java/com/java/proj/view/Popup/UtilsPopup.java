package com.java.proj.view.Popup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.proj.view.AppBaseBottomSheetDialogFragment;
import com.java.proj.view.Fragments.ParentFragment;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.DownloadModel;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.api.ApiInterface;
import com.java.proj.view.api.ApiUtilities;

import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilsPopup extends AppBaseBottomSheetDialogFragment implements View.OnClickListener {
    private Context context;
    private Bundle bundle;
    private GeneralModel generalModel;
    private LinearLayout download_1080_btn, download_full_btn;
    private DownloadManager manager;

    private final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
    }

    private void init(View view) {
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        download_1080_btn = view.findViewById(R.id.download_1080_btn);
        download_full_btn = view.findViewById(R.id.download_full_btn);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_1080_btn:
                download(EventDef.DOWNLOAD_OPTIONS.download_1080);
                break;
            case R.id.download_full_btn:
                download(EventDef.DOWNLOAD_OPTIONS.download_full);
                break;
        }
    }

    private void download(int flag) {
        if (checkAndRequestPermissions()) {
            switch (flag) {
                case EventDef.DOWNLOAD_OPTIONS.download_full:
                    String accessToken = getAppController(context).getAccessToken();
                    String query = generalModel.getLinksModel().getDownloadLocation().split("https://api.unsplash.com/")[1];
                    if (accessToken == null || accessToken.equals("")){
                        triggerDownload(ApiUtilities.getApiInterface(),generalModel.getLinksModel().getDownloadLocation() + "/?client_id=" + ApiUtilities.API_KEY);
//                        triggerDownload(ApiUtilities.getApiInterface(), query + "/?client_id=" + ApiUtilities.API_KEY);
//                        Log.d("mylog", "download: "+ query + "/?client_id=" + ApiUtilities.API_KEY);
                        Log.d("mylog", "download: "+ generalModel.getLinksModel().getDownloadLocation() + "/?client_id=" + ApiUtilities.API_KEY);
                    }else {
//                        triggerDownload(ApiUtilities.getApiInterface(accessToken), query);
                        triggerDownload(ApiUtilities.getApiInterface(accessToken), generalModel.getLinksModel().getDownloadLocation());
                    }

                    break;
            }
        }
    }

    private void triggerDownload(@NonNull ApiInterface apiInterface, @NonNull String query) {
        apiInterface.triggerDownload(query).enqueue(new Callback<DownloadModel>() {
            @Override
            public void onResponse(@NonNull Call<DownloadModel> call, @NonNull Response<DownloadModel> response) {
                if (response.body() != null) {
                    Log.d("mylog", "onResponse download: response body not null");
                    DownloadModel downloadModel = response.body();
                    Uri uri = Uri.parse(downloadModel.getUrl());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    long reference = manager.enqueue(request);
                } else {
                    Log.d("mylog", "onResponse download: response body null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DownloadModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        if (ActivityCompat.checkSelfPermission(context, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MainActivity)context, permissions, EventDef.REQUEST_CODES.PERMISSION_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }
}
