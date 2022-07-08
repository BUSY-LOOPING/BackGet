package com.java.busylooping.backget.utils;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

public class ShrinkGrowMaterialCardView extends MaterialCardView {
    private int MAX_CLICK_DURATION = 100;
    private long startClickTime, duration = 200;
    private boolean scaledDown = false;

    private ObjectAnimator objectAnimator;
    private PropertyValuesHolder pvhScaleDownX, pvhScaleUpX;
    private PropertyValuesHolder pvhScaleDownY, pvhScaleUpY;

    public ShrinkGrowMaterialCardView(Context context) {
        super(context);
        init(null);
    }

    public ShrinkGrowMaterialCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShrinkGrowMaterialCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        pvhScaleDownX = PropertyValuesHolder.ofFloat(SCALE_X, 0.95f);
        pvhScaleDownY = PropertyValuesHolder.ofFloat(SCALE_Y, 0.95f);
        pvhScaleUpX = PropertyValuesHolder.ofFloat(SCALE_X, 1f);
        pvhScaleUpY = PropertyValuesHolder.ofFloat(SCALE_Y, 1f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startClickTime = System.currentTimeMillis();
            Log.d("myview", "ACTION_DOWN: ");
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE ) {
            long clickDuration = System.currentTimeMillis() - startClickTime;
            if (clickDuration > MAX_CLICK_DURATION) {
                objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhScaleDownX, pvhScaleDownY);
                objectAnimator.setDuration(duration);
                objectAnimator.setInterpolator(new LinearInterpolator());
                if (!objectAnimator.isRunning()) {
                    objectAnimator.start();
                    scaledDown = true;
                }
                return true;
            }
        }


        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            long clickDuration = System.currentTimeMillis() - startClickTime;
            if (scaledDown) {
                objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhScaleUpX, pvhScaleUpY);
                objectAnimator.setDuration(duration);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.start();
                scaledDown = false;
                setPressed(false);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public int getStrokeWidth() {
        return super.getStrokeWidth();
    }
}
