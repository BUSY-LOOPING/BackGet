package com.java.proj.view.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.java.proj.view.R;

import java.util.ArrayList;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.GalleryRecyclerViewAdapterViewHolder> {
    private final Context context;
    private ArrayList<String> uri;

    public GalleryRecyclerViewAdapter(Context context, ArrayList<String> uri) {
        this.context = context;
        this.uri = uri;
    }

    @NonNull
    @Override
    public GalleryRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_recycler_item, parent, false);
        return new GalleryRecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRecyclerViewAdapterViewHolder holder, int position) {
        Glide.with(context)
                .load(uri.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uri.size();
    }

    public static class GalleryRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        public GalleryRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_item_img);
        }
    }
}
