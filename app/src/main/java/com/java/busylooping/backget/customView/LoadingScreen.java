package com.java.busylooping.backget.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.java.busylooping.backget.R;
import com.java.busylooping.backget.databinding.CustomViewLoadingLayoutBinding;

public class LoadingScreen extends LinearLayout {
    private CustomViewLoadingLayoutBinding binding;

    public LoadingScreen(Context context) {
        super(context);
        initView(null);
    }

    public LoadingScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public LoadingScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public LoadingScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        // TODO Auto-generated method stub
//        final int count = getChildCount();
//        int curWidth, curHeight, curLeft, curTop, maxHeight;
//
//        //get the available size of child view
//        int childLeft = this.getPaddingLeft();
//        int childTop = this.getPaddingTop();
//        int childRight = this.getMeasuredWidth() - this.getPaddingRight();
//        int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
//        int childWidth = childRight - childLeft;
//        int childHeight = childBottom - childTop;
//
//        maxHeight = 0;
//        curLeft = childLeft;
//        curTop = childTop;
//        //walk through each child, and arrange it from left to right
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                //Get the maximum size of the child
//                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
//                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
//                curWidth = child.getMeasuredWidth();
//                curHeight = child.getMeasuredHeight();
//                //wrap is reach to the end
//                if (curLeft + curWidth >= childRight) {
//                    curLeft = childLeft;
//                    curTop += maxHeight;
//                    maxHeight = 0;
//                }
//                //do the layout
//                child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
//                //store the max height
//                if (maxHeight < curHeight)
//                    maxHeight = curHeight;
//                curLeft += curWidth;
//            }
//        }
//    }

    public void setText(@NonNull String newText) {
        binding.setLoadingTxt(newText);
    }

    public String getText() {
        return binding.getLoadingTxt();
//        return "";
    }

    private void initView(@Nullable AttributeSet attributeSet) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        binding = CustomViewLoadingLayoutBinding.inflate(layoutInflater);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.custom_view_loading_layout, this,true);
        binding.setLoadingTxt("Loading");

        if (attributeSet != null) {
            TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.LoadingScreen);
            setText((String) a.getText(R.styleable.LoadingScreen_text));
            if (a.getBoolean(R.styleable.LoadingScreen_equiSize, false)) {
//                LayoutParams params = (LayoutParams) binding.loadingLayout.getLayoutParams();
//                LayoutParams layoutParams = new LayoutParams(params.width, params.width);
//                binding.loadingLayout.setLayoutParams(layoutParams);
            }
            a.recycle();
        }
//        inflate(getContext(), R.layout.custom_view_loading_layout, (ViewGroup) getRootView());
    }
}
