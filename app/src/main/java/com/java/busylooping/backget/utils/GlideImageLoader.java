package com.java.busylooping.backget.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.java.busylooping.backget.CallBacks.DownloadCallBack;

public class GlideImageLoader {

    private ImageView mImageView;
    private ProgressBar mProgressBar;

    public GlideImageLoader(ImageView imageView, ProgressBar progressBar) {
        mImageView = imageView;
        mProgressBar = progressBar;
    }

    public void load(final String url, RequestOptions options, @NonNull DownloadCallBack callBack) {
        if (url == null || options == null) return;

        onConnecting();

        //set Listener & start
        ProgressAppGlideModule.expect(url, new ProgressAppGlideModule.UIonProgressListener() {
            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                if (mProgressBar != null) {
                    int progress = (int) (100 * bytesRead / expectedLength);
                    callBack.completed(progress);
                    mProgressBar.setProgress(progress);
                }
            }

            @Override
            public float getGranualityPercentage() {
                return 1.0f;
            }
        });
        //Get Image
        Glide.with(mImageView.getContext())
                .asBitmap()
                .load(url)
                .apply(options.skipMemoryCache(true))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        callBack.onFailed();
                        onFinished();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressAppGlideModule.forget(url);
                        onFinished();
                        return false;
                    }
                })
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        onBtnSavePng(resource);
                        callBack.finalBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        callBack.onFailed();
                    }
                });
    }


    private void onConnecting() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
    }

    private void onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
    }
}
