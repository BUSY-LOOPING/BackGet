package com.java.busylooping.backget.Popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.java.busylooping.backget.BuildConfig;
import com.java.busylooping.backget.R;

public class SharePopup extends DialogFragment {
    private Context context;
    private ImageView cancelBtn;
    private LottieAnimationView lottieBtn;

//    public SharePopup(Context context) {
//        super(context);
//        this.context = context;
//        View view = LayoutInflater.from(context).inflate(R.layout.share_popup, null);
//        setContentView(view);
//
//        init(view);
//
//    }

//    public void show(View anchor)
//    {
//        // you can edit display location is here
//        showAtLocation(anchor, Gravity.CENTER, 0, 0);
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_popup, container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        init(view);
        listeners();
        return view;
    }

    private void listeners() {
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
        lottieBtn.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BackGet");
                String shareMessage= "Enjoy free wallpapers, images, backgrounds from a collection of over more than 1M + artists!\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int) (getResources().getDisplayMetrics().widthPixels / 1.2);
        int height = getResources().getDisplayMetrics().heightPixels / 7;
        if (getDialog() != null && getDialog().getWindow() != null) {

            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = width;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    private void init(View view) {
        cancelBtn = view.findViewById(R.id.cancelBtn);
        lottieBtn = view.findViewById(R.id.share_btn_lottie);
        ConstraintLayout constraintLayout = view.findViewById(R.id.share_popup_parent);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }
}
