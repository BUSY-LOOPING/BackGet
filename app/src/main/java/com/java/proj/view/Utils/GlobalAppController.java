package com.java.proj.view.Utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

    //views
    private LottieAnimationView lottieAnimationView;
    private LinearLayout galleryBtn, customBtn;

    private FragmentManager fragmentManager;
    private Service holderService;
    //    private ViewPager2 mainViewPager;
    private ViewPager mainViewPager1;
    private TabLayout mainTabLayout;
    //    private MainFragmentAdapter mainFragmentAdapter;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;

    public GlobalAppController(Context context, Bundle savedInstanceState) {
        this.context = context;
        appEventBus = new AppEventBus(context);
        appEventReceiver = new AppEventReceiver(this);
        appEventBus.register(appEventReceiver);
        init((MainActivity) context);
        listeners();
    }

    public void postToBus(AppEvent appEvent) {
        if (appEvent != null) {
            appEventBus.post(appEvent);
        }
    }

    void init(MainActivity activity) {
        fragmentManager = activity.getSupportFragmentManager();
        lottieAnimationView = activity.findViewById(R.id.share_btn);
        galleryBtn = activity.findViewById(R.id.galleryBtn);
        customBtn = activity.findViewById(R.id.customBtn);
//        mainViewPager = activity.findViewById(R.id.viewPagerMain);
        mainViewPager1 = activity.findViewById(R.id.viewPagerMain);
        mainTabLayout = activity.findViewById(R.id.tabLayoutMain);
//        mainViewPager.setAdapter(createMainAdapter());
        mainViewPager1.setAdapter(createMainPagerAdapter());
        mainTabLayout.setupWithViewPager(mainViewPager1);
//        {
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mainTabLayout, mainViewPager,
//                    (tab, position) -> tab.setText(FragmentList.getFragment(position).toString()));
//            tabLayoutMediator.attach();
//        }


    }

    private MainFragmentPagerAdapter createMainPagerAdapter() {
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(((MainActivity) context).getSupportFragmentManager());
        return mainFragmentPagerAdapter;
    }


//    private MainFragmentAdapter createMainAdapter() {
//        mainFragmentAdapter = new MainFragmentAdapter((MainActivity) context);
//        return mainFragmentAdapter;
//    }

    private void listeners() {
        lottieAnimationView.setOnClickListener(v -> {
            AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.SHARE_BUTTON_PRESSED.ordinal(), 0, 0);
            appEventBus.post(appEvent);
        });

        galleryBtn.setOnClickListener(v -> {
            AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.GALLERY_BUTTON_PRESSED.ordinal(), 0, 0);
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.MAIN_CONTAINER, R.id.activity_main_container);
            appEvent.extras = bundle;
            appEventBus.post(appEvent);
        });
    }

    private static class AppEventReceiver extends AppEventBus.Receiver<GlobalAppController> {
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
            lottieAnimationView.playAnimation();
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
        if (!fragmentManager.isDestroyed()) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                    .addToBackStack("fragment")
                    .replace(id, fragment)
                    .commit();
        }
    }

    public void switchContent(@NonNull Context context, Class<?> c, @NonNull Bundle bundle, @NonNull View container, @NonNull View view) {
        String transitionNameImgContainer = context.getString(R.string.target_img_transition_parent);
        String transitionNameImg = context.getString(R.string.target_img_transition);
        MainActivity activity = (MainActivity) context;

        Pair<View, String> pair1 = Pair.create(container, transitionNameImgContainer);
        Pair<View, String> pair2 = Pair.create(view, transitionNameImg);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2);
        Intent intent = new Intent(context, c);
        intent.putExtra(ImageDetailActivity.BUNDLE_VAL, bundle);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }


    public void bindHolderService(Service service) {
        holderService = service;
    }

    public Context getContext() {
        return context;
    }
}
