package com.java.proj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SingleImageRecycleView extends RecyclerView {
    public SingleImageRecycleView(@NonNull Context context) {
        super(context);
    }

    public SingleImageRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleImageRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ViewHolder getChildViewHolder(@NonNull View child) {
        return super.getChildViewHolder(child);
    }
}
