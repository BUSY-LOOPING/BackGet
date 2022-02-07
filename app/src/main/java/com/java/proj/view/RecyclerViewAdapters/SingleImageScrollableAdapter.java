package com.java.proj.view.RecyclerViewAdapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
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
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.92);
        view.setLayoutParams(layoutParams);
        return new SingleImageScrollableAdapterViewHolder(view);
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
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public static class SingleImageScrollableAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private final ImageView imageView, userImg;
        private final TextView title, description, likeTxt, byTxt;
        private final ImageView moreBtn;
        private final NestedScrollView nestedScrollView;

        public SingleImageScrollableAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            userImg = itemView.findViewById(R.id.userImg);
            title = itemView.findViewById(R.id.titleTxt);
            description = itemView.findViewById(R.id.descriptionTxt);
            likeTxt = itemView.findViewById(R.id.likeTxt);
            byTxt = itemView.findViewById(R.id.byTxt);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            nestedScrollView = itemView.findViewById(R.id.nestedScrollableView);
            moreBtn.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == moreBtn) {
                nestedScrollView.setVisibility(View.VISIBLE);
                moreBtn.setVisibility(View.GONE);
            } else if (view == imageView) {
                if (moreBtn.getVisibility() == View.GONE) {
                    nestedScrollView.setVisibility(View.GONE);
                    moreBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
