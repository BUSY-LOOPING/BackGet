package com.java.proj.view.CustomRecyclerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SingleImageRecycleView extends RecyclerView {
    public SingleImageRecycleView(@NonNull Context context) {
        super(context);
        init();
    }

    public SingleImageRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleImageRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        LinearSnapHelper snapHelper = new SnapHelperOneByOne();
//        PagerSnapHelper snapHelper = new PagerSnapHelper() {
//        };
        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(this);
    }


}
