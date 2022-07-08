package com.java.busylooping.backget.Activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.java.busylooping.backget.AppBaseActivity;
import com.java.busylooping.backget.R;

public class ImageDetailActivity extends AppBaseActivity {
    public static final String BUNDLE_VAL = "ImageDetailActivityBundle";
    public static final String LOADED_URL = "ImageDetailActivityImgUrl";
    //    public static final String LOADED_POS = "ImageDetailActivityLoadedImgPos";
//    public static final String URL_LIST = "ImageDetailActivityUrlList";
    public static final String TAG = "ImageDetailActivity";
    private ImageView imageView, closeBtn;
    private MaterialCardView cardView;
    private Bundle bundle;

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

        setContentView(R.layout.activity_image_detail);
        init();
        set();
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        postponeEnterTransition();
        imageView = findViewById(R.id.img);
        closeBtn = findViewById(R.id.closeBtn);
        cardView = findViewById(R.id.img_parent);
        bundle = getIntent().getBundleExtra(BUNDLE_VAL);
    }

    private void set() {
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

        closeBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });
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

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}