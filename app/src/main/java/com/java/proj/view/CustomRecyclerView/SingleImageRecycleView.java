package com.java.proj.view.CustomRecyclerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SingleImageRecycleView extends RecyclerView {
    private boolean verticalScrollingEnabled = true;

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

    public void enableVerticalScroll (boolean enabled) {
        verticalScrollingEnabled = enabled;
    }

    public boolean isVerticalScrollingEnabled() {
        return verticalScrollingEnabled;
    }

    @Override
    public int computeVerticalScrollRange() {

        if (isVerticalScrollingEnabled())
            return super.computeVerticalScrollRange();
        return 0;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        if(isVerticalScrollingEnabled())
            return super.onInterceptTouchEvent(e);
        return false;

    }

    private void init() {
//        LinearSnapHelper snapHelper = new SnapHelperOneByOne();
//        PagerSnapHelper snapHelper = new PagerSnapHelper() {
//        };
        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(this);
    }


}
