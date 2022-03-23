package com.java.proj.view.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.busy.looping.like_view_library.LikeView;
import com.busy.looping.like_view_library.OnLikeChangeListener;
import com.google.android.material.card.MaterialCardView;
import com.java.proj.view.CallBacks.SingleImageRecyclerViewListener;
import com.java.proj.view.CustomRecyclerView.SingleImageRecycleView;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;
import com.ortiz.touchview.OnTouchCoordinatesListener;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;

public class SingleImageScrollableAdapter extends RecyclerView.Adapter<SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder> {
    private final Context mContext;
    private ArrayList<GeneralModel> list;
    private final Bundle bundle;
    private final SingleImageRecyclerViewListener listener;
    private SingleImageRecycleView recyclerView;

    public SingleImageScrollableAdapter(Context mContext, ArrayList<GeneralModel> generalModels, Bundle bundle, SingleImageRecyclerViewListener listener, SingleImageRecycleView recyclerView) {
        this.mContext = mContext;
        this.list = generalModels;
        this.bundle = bundle;
        this.listener = listener;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public SingleImageScrollableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.image_detail_recyclerview_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.92);
        view.setLayoutParams(layoutParams);
        return new SingleImageScrollableAdapterViewHolder(view, listener);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SingleImageScrollableAdapterViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SingleImageScrollableAdapterViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.moreBtn.getVisibility() == View.GONE) {
            holder.moreBtn.setVisibility(View.VISIBLE);
            holder.nestedScrollView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageScrollableAdapterViewHolder holder, int position) {
        holder.imageView.setOnTouchCoordinatesListener(new OnTouchCoordinatesListener() {
            @Override
            public void onTouchCoordinate(@NonNull View view, @NonNull MotionEvent motionEvent, @NonNull PointF pointF) {
                if (motionEvent.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
                    recyclerView.enableVerticalScroll(false);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    recyclerView.enableVerticalScroll(true);
            }
        });
        RequestOptions requestOptions = new RequestOptions().dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .asBitmap()
                .load(list.get(position).getUriModel().getRegular())
                .apply(requestOptions)
                .into(holder.imageView);

        Glide.with(mContext)
                .load(list.get(position).getUserModel().getUserProfileImageModel().getMedium())
                .apply(requestOptions)
                .into(holder.userImg);

        holder.description.setText(list.get(position).getDescription());
        holder.likeTxt.setText(list.get(position).getLikes());
        String s = list.get(position).getUserModel().getName() + " " + mContext.getString(R.string.by_unsplash);
        holder.byTxt.setText(s);

        if (list.get(position).isLiked() && !holder.likeView.isLiked()) {
            holder.likeView.performClick();
        } else if (!list.get(position).isLiked() && holder.likeView.isLiked()) {
            holder.likeView.performClick();
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public static class SingleImageScrollableAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, OnLikeChangeListener {
        private final TouchImageView imageView;
        private final LikeView likeView;
        private final TextView title, description, likeTxt, byTxt;
        private final ImageView moreBtn, userImg;
        private final NestedScrollView nestedScrollView;
        private final SingleImageRecyclerViewListener listener;
        private final MaterialCardView downloadBtn;
        private final LottieAnimationView downloadLottieView;

        public SingleImageScrollableAdapterViewHolder(@NonNull View itemView, SingleImageRecyclerViewListener listener) {
            super(itemView);
            this.listener = listener;
            likeView = itemView.findViewById(R.id.likeView);
            imageView = itemView.findViewById(R.id.img);
            userImg = itemView.findViewById(R.id.userImg);
            title = itemView.findViewById(R.id.titleTxt);
            description = itemView.findViewById(R.id.descriptionTxt);
            likeTxt = itemView.findViewById(R.id.likeTxt);
            byTxt = itemView.findViewById(R.id.byTxt);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            nestedScrollView = itemView.findViewById(R.id.nestedScrollableView);
            downloadBtn = itemView.findViewById(R.id.downloadBtn);
            downloadLottieView = itemView.findViewById(R.id.downloadLottieView);
            moreBtn.setOnClickListener(this);
            likeView.setOnClickListener(this);
            imageView.setOnClickListener(this);
            downloadBtn.setOnClickListener(this);
            likeView.setOnLikeChangeListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moreBtn:
                    nestedScrollView.setVisibility(View.VISIBLE);
                    moreBtn.setVisibility(View.GONE);
                    break;
                case R.id.img:
                    if (moreBtn.getVisibility() == View.GONE) {
                        nestedScrollView.setVisibility(View.GONE);
                        moreBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.downloadBtn:
                    listener.onDownloadClick(getAdapterPosition());
                    break;
                case R.id.likeView:
                    likeView.onClick(likeView);
                    break;
            }
        }

        @Override
        public void onLikeChange(boolean isLiked) {
            listener.onLikeClick(isLiked, getAdapterPosition());
        }
    }
}
