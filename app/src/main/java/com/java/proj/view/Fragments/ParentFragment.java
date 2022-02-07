package com.java.proj.view.Fragments;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Slide;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionSet;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.android.material.tabs.TabLayout;
import com.java.proj.view.Activities.ImageDetailActivity;
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CustomTransition;
import com.java.proj.view.Events;
import com.java.proj.view.MainActivity;
import com.java.proj.view.MainFragment.MainFragmentPagerAdapter;
import com.java.proj.view.R;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;

public class ParentFragment extends AppBaseFragment {
    public static final String PARENT_CONTAINER = "ParentFragmentRootContainer";
    private Bundle bundle;
    private Context context;

    //views
    private LottieAnimationView lottieAnimationView, download_icon;
    private LinearLayout galleryBtn, customBtn;

    private FragmentManager fragmentManager;
    //    private ViewPager2 mainViewPager;
    private ViewPager mainViewPager1;
    private TabLayout mainTabLayout;
    //    private MainFragmentAdapter mainFragmentAdapter;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private ParentFragment.AppEventReceiver appEventReceiver;

    public static class AppEventReceiver extends AppEventBus.Receiver<ParentFragment> {
        private static final int[] FILTER = new int[]{
//                EventDef.Category.IMAGE_CLICK,
                EventDef.Category.GALLERY_BTN
        };

        public AppEventReceiver(ParentFragment holder, int[] categoryFilter) {
            super(holder, categoryFilter);
        }

        @Override
        protected void onReceiveAppEvent(ParentFragment holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        }
    }

    public ParentFragment() {
        // Required empty public constructor
    }


    public static ParentFragment newInstance(Bundle bundle) {
        ParentFragment fragment = new ParentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appEventReceiver == null)
            appEventReceiver = new AppEventReceiver(this, AppEventReceiver.FILTER);
        eventBus().register(appEventReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent, container, false);
        init(view);
        listeners();
        LottieAnimationView confetti = view.findViewById(R.id.confetti_lottie);
        confetti.setSpeed(0.5f);
        confetti.setRepeatCount(LottieDrawable.INFINITE);
        return view;
    }

    private void init(View view) {
        lottieAnimationView = view.findViewById(R.id.share_btn);
        download_icon = view.findViewById(R.id.download_btn_lottie);
        download_icon.addValueCallback(new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.pink), PorterDuff.Mode.SRC_ATOP);
                    }
                });
        galleryBtn = view.findViewById(R.id.galleryBtn);
        customBtn = view.findViewById(R.id.customBtn);
//        fragmentManager = getChildFragmentManager();
        fragmentManager = getChildFragmentManager();

//        mainViewPager = activity.findViewById(R.id.viewPagerMain);
        mainViewPager1 = view.findViewById(R.id.viewPagerMain);
        mainTabLayout = view.findViewById(R.id.tabLayoutMain);
//        mainViewPager.setAdapter(createMainAdapter());
        mainViewPager1.setAdapter(createMainPagerAdapter());
        mainTabLayout.setupWithViewPager(mainViewPager1);
//        {
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mainTabLayout, mainViewPager,
//                    (tab, position) -> tab.setText(FragmentList.getFragment(position).toString()));
//            tabLayoutMediator.attach();
//        }

    }

    private void listeners() {
        lottieAnimationView.setOnClickListener(v -> {
            AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.SHARE_BUTTON_PRESSED.ordinal(), 0, 0);
            eventBus().post(appEvent);
        });

        galleryBtn.setOnClickListener(v -> {
            AppEvent appEvent = new AppEvent(EventDef.Category.GALLERY_BTN, EventDef.Category.GALLERY_BTN, 0, 0);
            Bundle bundle = new Bundle();
            bundle.putInt(PARENT_CONTAINER, R.id.fragment_parent_container);
            appEvent.extras = bundle;
            eventBus().post(appEvent);
        });
    }

    private MainFragmentPagerAdapter createMainPagerAdapter() {
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(fragmentManager);
        return mainFragmentPagerAdapter;
    }

    void onReceiveAppEvent(AppEvent event) {
        switch (event.category) {
            case EventDef.Category.IMAGE_CLICK:
                handleImageClick(event);
                break;
            case EventDef.Category.GALLERY_BTN:
                Fragment fragment = GalleryFragment.newInstance(new Bundle());

                GlobalAppController.switchFragment(R.id.container, fragment, ((MainActivity)context).getSupportFragmentManager(),
                        new int[] {R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_right, R.anim.slide_out_left});
                break;
        }
    }

    private void handleImageClick(AppEvent event) {
//        Fragment fragment = ImageDetailFragment.newInstance(event.extras);
//        fragment.setSharedElementEnterTransition(new CustomTransition());
//        fragment.setEnterTransition(new Fade());
//        setExitTransition(new Fade());
//        fragment.setSharedElementReturnTransition(new CustomTransition());
//        postponeEnterTransition();
////        GlobalAppController.switchFragment(R.id.container, fragment, this,fragmentManager, event.weakReferenceList.get());
//        GlobalAppController.switchContent(context, ImageDetailActivity.class, event.extras, (View) event.weakReferenceList.get().get(0).second, (View) event.weakReferenceList.get().get(1).second);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus().unregister(appEventReceiver);
    }
}