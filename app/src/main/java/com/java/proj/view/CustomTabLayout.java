package com.java.proj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.tabs.TabLayout;

public class CustomTabLayout extends TabLayout {
    private Context context;
    private int selectedTabPos = 0;
    private static final String TAG = "customtab_tag";
    private Drawable originalDrawable = null;
    private Tab currentTab = null;

    private void init(Context context) {
        this.context = context;
    }

    public CustomTabLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public int getSelectedTabPosition() {
        selectedTabPos = super.getSelectedTabPosition();
//        if (selectedTabPos == 1) {
//            setSelectedTabIndicator(new Drawable() {
//                @Override
//                public void draw(@NonNull Canvas canvas) {
//
//                }
//
//                @Override
//                public void setAlpha(int i) {
//
//                }
//
//                @Override
//                public void setColorFilter(@Nullable ColorFilter colorFilter) {
//
//                }
//
//                @Override
//                public int getOpacity() {
//                    return PixelFormat.TRANSPARENT;
//                }
//            });
//        } else {
//            setSelectedTabIndicator(ContextCompat.getDrawable(context, R.drawable.inner_tab_indicator));
//            setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.inner_custom_separator));
//        }
        Log.d(TAG, "selectedPos = " + selectedTabPos);
        return selectedTabPos;
    }

    @Nullable
    @Override
    public Tab getTabAt(int index) {
        currentTab = super.getTabAt(index);
        return currentTab;
    }


    @Override
    public void setSelectedTabIndicator(int tabSelectedIndicatorResourceId) {
        Log.d(TAG, "setSelectedTabIndicator(int) called = " + tabSelectedIndicatorResourceId);
        if (originalDrawable == null)
            originalDrawable = AppCompatResources.getDrawable(context, tabSelectedIndicatorResourceId);
        super.setSelectedTabIndicator(tabSelectedIndicatorResourceId);
    }

    @Override
    public void setSelectedTabIndicator(@Nullable Drawable tabSelectedIndicator) {
        Log.d(TAG, "setSelectedTabIndicator(Drawable) called");
        if (originalDrawable == null)
            originalDrawable = tabSelectedIndicator;

        super.setSelectedTabIndicator(tabSelectedIndicator);
    }

    @Override
    public void setSelectedTabIndicatorColor(int color) {
        Log.d(TAG, "setSelectedTabIndicatorColor called = " + color);
        super.setSelectedTabIndicatorColor(color);
    }

    @Override
    public Animation.AnimationListener getLayoutAnimationListener() {
        return super.getLayoutAnimationListener();
    }

    @Override
    public LayoutAnimationController getLayoutAnimation() {
        return super.getLayoutAnimation();
    }

    @Override
    public int getTabIndicatorAnimationMode() {
        return super.getTabIndicatorAnimationMode();
    }


    @Override
    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        Log.d(TAG, "addOnTabSelectedListener called");
        super.addOnTabSelectedListener(listener);
    }


    @NonNull
    @Override
    public Drawable getTabSelectedIndicator() {
        Log.d(TAG, "getTabSelectedIndicator");
        if (selectedTabPos == 1) {
            Log.d(TAG, "getTabSelectedIndicator when pos = 1");
            return new Drawable() {
                @Override
                public void draw(@NonNull Canvas canvas) {

                }

                @Override
                public void setAlpha(int i) {

                }

                @Override
                public void setColorFilter(@Nullable ColorFilter colorFilter) {

                }

                @Override
                public int getOpacity() {
                    return PixelFormat.TRANSPARENT;
                }
            };
        }
        return super.getTabSelectedIndicator();
    }
}
