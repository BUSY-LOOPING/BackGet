package com.java.busylooping.backget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

public abstract class GeneralAppBaseFragment extends AppBaseFragment {

    public void initializeView(@NonNull View view) {
        Context context = view.getContext();
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        view.setFocusable(true);

        TextView textView = new TextView(context);
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(getTitle());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.product_sans_bold);
        textView.setTypeface(typeface);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView, 12, 100, 2, TypedValue.COMPLEX_UNIT_SP);
        }
        textView.setId(View.generateViewId());
        if (view instanceof ConstraintLayout) {
            ConstraintLayout layout = (ConstraintLayout) view;
            ConstraintSet set = new ConstraintSet();

//            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(textView, 0);

            set.clone(layout);
            set.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            set.connect(textView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            set.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ApplicationClass.dpToPx(context, 30));
            set.applyTo(layout);

            View secondView = layout.getChildAt(1);
            if (secondView != null) {
                set.connect(secondView.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM, 10);
                set.applyTo(layout);
            }
        }
    }

    public abstract String getTitle();
}
