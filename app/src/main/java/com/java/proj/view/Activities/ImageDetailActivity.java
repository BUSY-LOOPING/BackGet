package com.java.proj.view.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.NestedScrollableHost;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.SingleImageScrollableAdapter;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Utils.GlobalAppControllerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageDetailActivity extends AppCompatActivity implements ServiceConnection {
    public static final String BUNDLE_VAL = "ImageDetailActivityBundle";
    public static final String LOADED_URL = "ImageDetailActivityImgUrl";
//    public static final String LOADED_POS = "ImageDetailActivityLoadedImgPos";
//    public static final String URL_LIST = "ImageDetailActivityUrlList";
    public static final String TAG = "ImageDetailActivity";
    private ImageView imageView;
    private MaterialCardView cardView;
    private Bundle bundle;
    private GlobalAppControllerService service;
    private GlobalAppController globalAppController;
    private GlobalAppController.AppEventReceiver appEventReceiver;

//    private LinearLayoutManager layoutManager;
//    private RecyclerView recyclerView;
//    private SingleImageScrollableAdapter adapter;
//    private ArrayList<GeneralModel> list;
//    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_image_detail);
        init();
        set();
        doBindService();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        postponeEnterTransition();
        imageView = findViewById(R.id.img);
        cardView = findViewById(R.id.img_parent);
        bundle = getIntent().getBundleExtra(BUNDLE_VAL);

//        list = new ArrayList<>();
//        list = (ArrayList<GeneralModel>) bundle.getSerializable(URL_LIST);
//        pos = bundle.getInt(LOADED_POS, 0);

//        recyclerView = findViewById(R.id.recyclerViewImageDetail);
//        adapter = new SingleImageScrollableAdapter(this, list, bundle);
    }

    private void set() {
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//        layoutManager.scrollToPosition(pos);
        RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .asBitmap()
                .load(bundle.getString(LOADED_URL))
                .apply(requestOptions)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        scheduleStartPostponedTransition(imageView);
                        scheduleStartPostponedTransition(cardView);
                        return false;
                    }
                })
                .into(imageView);
//
        Transition transition = getWindow().getSharedElementExitTransition();
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Toast.makeText(ImageDetailActivity.this, "size(app) = "+ sharedElements.size(), Toast.LENGTH_SHORT).show();
                for (String set : sharedElements.keySet()) {
                    Log.d(TAG, "onMapSharedElements(app): " + set);
                }
            }
        });


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }
    public void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
    
    private void doBindService() {
        Intent intent = new Intent(this, GlobalAppControllerService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = ((GlobalAppControllerService.LocalBinder) iBinder).getService();
        if (service != null) {
            globalAppController = service.getGlobalAppController();

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
    }


    @Override
    protected void onDestroy() {
        unbindService(this);
        service = null;
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}