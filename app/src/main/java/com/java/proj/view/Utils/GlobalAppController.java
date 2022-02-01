package com.java.proj.view.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.java.proj.view.Activities.ImageDetailActivity;
import com.java.proj.view.Events;
import com.java.proj.view.Fragments.GalleryFragment;
import com.java.proj.view.MainActivity;
import com.java.proj.view.MainFragment.MainFragmentPagerAdapter;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;

import java.util.ArrayList;

public class GlobalAppController {
    private final Context context;
    private final AppEventBus appEventBus;
    private final AppEventReceiver appEventReceiver;
    private Service holderService;


    public GlobalAppController(Context context, Bundle savedInstanceState) {
        this.context = context;
        appEventBus = new AppEventBus(context);
        appEventReceiver = new AppEventReceiver(this);
        appEventBus.register(appEventReceiver);
        init(context);
    }

    public void postToBus(AppEvent appEvent) {
        if (appEvent != null) {
            appEventBus.post(appEvent);
        }
    }

    void init(Context context) {
    }



//    private MainFragmentAdapter createMainAdapter() {
//        mainFragmentAdapter = new MainFragmentAdapter((MainActivity) context);
//        return mainFragmentAdapter;
//    }



    public static class AppEventReceiver extends AppEventBus.Receiver<GlobalAppController> {
        private static final int[] FILTER = new int[]{
                0,
                1,
                2
        };

        public AppEventReceiver(GlobalAppController holder) {
            super(holder, FILTER);
        }

        @Override
        protected void onReceiveAppEvent(GlobalAppController holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        } //3


    }

    void onReceiveAppEvent(AppEvent event) {

        switch (event.category) {
            case 0:
                break;
            case 1:
                handleClick(event);
                break;
            case 2:
                break;
        }

    }

    private void handleClick(AppEvent event) {
        int eventInt = event.event;
        Bundle extras = event.extras;

        if (Events.SHARE_BUTTON_PRESSED.ordinal() == eventInt) {
//            lottieAnimationView.playAnimation();
        } else if (Events.IMAGE_CLICK.ordinal() == eventInt) {
//            Bundle bundle = new Bundle();
//            bundle.putString(ImageDetailActivity.LOADED_URL, extras.getString(ImageDetailActivity.LOADED_URL, ""));
//            bundle.putInt(ImageDetailActivity.LOADED_POS, extras.getInt(ImageDetailActivity.LOADED_POS, 0));
//            bundle.putSerializable(ImageDetailActivity.URL_LIST, extras.getSerializable(ImageDetailActivity.URL_LIST));
            MaterialCardView cardView = (MaterialCardView) event.weakReferenceList.get().get(0);
            ImageView imageView = (ImageView) event.weakReferenceList.get().get(1);
            if (cardView == null || imageView == null) {
                return;
            }
            switchContent(context, ImageDetailActivity.class, extras, cardView, imageView);
        } else if (Events.GALLERY_BUTTON_PRESSED.ordinal() == eventInt) {
            Fragment fragment = GalleryFragment.newInstance(new Bundle());
            switchFragment(android.R.id.content, fragment);
        }
    }

    public void switchFragment(int id, Fragment fragment) {
//        if (!fragmentManager.isDestroyed()) {
//            fragmentManager
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
//                    .addToBackStack("fragment")
//                    .replace(id, fragment)
//                    .commit();
//        }
    }

    public void switchContent(@NonNull Context context, Class<?> c, @NonNull Bundle bundle, @NonNull View container, @NonNull View view) {
//        String transitionNameImgContainer = context.getString(R.string.target_img_transition_parent);
//        String transitionNameImg = context.getString(R.string.target_img_transition);
//        MainActivity activity = (MainActivity) context;
//
//        Pair<View, String> pair1 = Pair.create(container, transitionNameImgContainer);
//        Pair<View, String> pair2 = Pair.create(view, transitionNameImg);
//        activity.supportStartPostponedEnterTransition();
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2);
//        Intent intent = new Intent(context, c);
//        intent.putExtra(ImageDetailActivity.BUNDLE_VAL, bundle);
//        ActivityCompat.startActivity(context, intent, options.toBundle());
    }


    public void bindHolderService(Service service) {
        holderService = service;
    }

    public Context getContext() {
        return context;
    }

    public AppEventBus getAppEventBus() {
        return appEventBus;
    }
}
