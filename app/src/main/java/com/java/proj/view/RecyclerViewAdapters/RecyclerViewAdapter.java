package com.java.proj.view.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.ImageModel;
import com.java.proj.view.Models.UriModel;
import com.java.proj.view.NestedScrollableHost;
import com.java.proj.view.R;
import com.like.LikeButton;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecentRecyclerViewHolder> {
    private final Context mContext;
    private final ArrayList<GeneralModel> list;
    private final RecyclerViewListener listener;

    public RecyclerViewAdapter(Context mContext, ArrayList<GeneralModel> list, RecyclerViewListener listener) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        return new RecentRecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRecyclerViewHolder holder, int position) {
        if (list.get(position) != null) {
            RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(list.get(position).getUriModel().getRegular())
                    .into(holder.imageView);
            holder.likeNumber.setText(list.get(position).getLikes());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecentRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final MaterialCardView cardView;
        private final RecyclerViewListener listener;
        private final TextView likeNumber;


        public RecentRecyclerViewHolder(@NonNull View itemView, RecyclerViewListener listener) {
            super(itemView);
            this.listener = listener;
            imageView = itemView.findViewById(R.id.img);
            cardView = itemView.findViewById(R.id.img_parent);
            likeNumber = itemView.findViewById(R.id.likeNumber);
//            ViewCompat.setTransitionName(imageView, "recycler_view_item_transition_parent");
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == cardView) {
                listener.onClick(getAdapterPosition(), cardView, imageView);
            }
        }
    }
}
