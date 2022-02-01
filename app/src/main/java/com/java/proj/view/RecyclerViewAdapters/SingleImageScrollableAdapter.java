package com.java.proj.view.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.java.proj.view.Activities.ImageDetailActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;

import java.util.ArrayList;

public class SingleImageScrollableAdapter extends RecyclerView.Adapter<SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder> {
    private final Context mContext;
    private ArrayList<GeneralModel> list;
    private final Bundle bundle;

    public SingleImageScrollableAdapter(Context mContext, ArrayList<GeneralModel> generalModels, Bundle bundle) {
        this.mContext = mContext;
        this.list = generalModels;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public SingleImageScrollableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.image_detail_recyclerview_item, parent, false);
        return new SingleImageScrollableAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageScrollableAdapterViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions().dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .asBitmap()
                .load(list.get(position).getUriModel().getRegular())
                .apply(requestOptions)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        ((ImageDetailActivity) mContext).scheduleStartPostponedTransition(holder.imageView);
                        return false;
                    }
                })
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SingleImageScrollableAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final MaterialCardView cardView;

        public SingleImageScrollableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            cardView = itemView.findViewById(R.id.img_parent);
        }
    }
}
