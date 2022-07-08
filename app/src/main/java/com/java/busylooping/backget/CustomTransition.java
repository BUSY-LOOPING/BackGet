package com.java.busylooping.backget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.Gravity;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CustomTransition extends TransitionSet {
    public CustomTransition() {
        init();
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    public CustomTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new Fade(Fade.MODE_IN))
                .addTransition(new Slide(Gravity.END));
    }
}

