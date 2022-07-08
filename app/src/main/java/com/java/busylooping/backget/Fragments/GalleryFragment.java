package com.java.busylooping.backget.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.RecyclerViewAdapters.GalleryRecyclerViewAdapter;

import java.util.ArrayList;

public class GalleryFragment extends AppBaseFragment {
    private static String TAG = "GalleryFragment";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Bundle bundle;
    private Context context;
    private final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private TextView providePermTxtView;
    private RecyclerView recyclerView;
    private GalleryRecyclerViewAdapter adapter;
    private ArrayList<String> uri;

    private GridLayoutManager layoutManager;
    private final ActivityResultLauncher<String> permissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        set();
                    }
                }
            }
    );

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(Bundle bundle) {
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission()) {
            set();
        }
    }

    private void init(View view) {
        uri = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new GalleryRecyclerViewAdapter(context, uri);
        layoutManager = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
        providePermTxtView = view.findViewById(R.id.provide_perm_txt);
        providePermTxtView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", context.getPackageName())));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            }
        });
    }

    private void set() {
        providePermTxtView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        uri.addAll(getList());
        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getList() {
        ArrayList<String> res = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media.DATA
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                if (path != null) {
                    res.add(path);
                }
            }
            cursor.close();
        }
        return res;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(context, permissions[0]);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",context.getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
//        below android 11
            ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_REQUEST_CODE);
        }
        permissionResult.launch(permissions[0]);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(context, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //this method gets called when ActivityCompat.requestPermissions is called
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (READ_EXTERNAL_STORAGE) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(context, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "length = 0", Toast.LENGTH_SHORT).show();
            }
        }
    }
}