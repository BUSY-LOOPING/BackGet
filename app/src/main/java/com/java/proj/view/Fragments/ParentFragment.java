package com.java.proj.view.Fragments;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.android.material.tabs.TabLayout;
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.DataBase.LikedDataBase;
import com.java.proj.view.MainActivity;
import com.java.proj.view.MainFragment.MainFragmentPagerAdapter;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.LikesModel;
import com.java.proj.view.Popup.SharePopup;
import com.java.proj.view.R;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.databinding.FragmentParentBinding;

import java.util.ArrayList;
import java.util.Map;

public class ParentFragment extends AppBaseFragment {
    public static final String PARENT_CONTAINER = "ParentFragmentRootContainer";
    private Bundle bundle;
    private Context context;
    private FragmentParentBinding binding;
    private LikesModel likesModel;
    private final Observer<Map<String, GeneralModel>> observer = new Observer<Map<String, GeneralModel>>() {
        @Override
        public void onChanged(Map<String, GeneralModel> generalModels) {
            binding.setTotalLikes("" + generalModels.size());
        }
    };

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
    private int prevPos = 0;
    private boolean resumedFirstFrag = false;

    private TextView totalLikedText;
    private ImageView settingsBtn;

    public static class AppEventReceiver extends AppEventBus.Receiver<ParentFragment> {
        private static final int[] FILTER = new int[]{
                EventDef.Category.TOOLBAR,
                EventDef.Category.BTN
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
        eventBus(context).register(appEventReceiver);
        AppBaseFragment fragment = mainFragmentPagerAdapter.getRegisteredFragment(prevPos);
        if (fragment != null)
            fragment.onResumeInViewPager();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent, container, false);
        View view = binding.getRoot();
        init(view);
        listeners();
        LottieAnimationView confetti = view.findViewById(R.id.confetti_lottie);
        confetti.setSpeed(0.5f);
        confetti.setRepeatCount(LottieDrawable.INFINITE);
        return view;
    }

    private void init(@NonNull View view) {
        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
        LikedDataBase likedDataBase = LikedDataBase.getInstance(context);
        Map<String, GeneralModel> map = likedDataBase.getGeneralModelList();
        for (GeneralModel generalModel : map.values()) {
            likesModel.getLastLikedModel().setValue(generalModel);
        }
        likesModel.getCurrentLikedList().setValue(map);
        likesModel.getCurrentLikedList().observe((LifecycleOwner) context, observer);
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
        download_icon.playAnimation();
        galleryBtn = view.findViewById(R.id.galleryBtn);
        customBtn = view.findViewById(R.id.customBtn);
//        fragmentManager = getChildFragmentManager();
        fragmentManager = ((MainActivity)context).getSupportFragmentManager();

//        mainViewPager = activity.findViewById(R.id.viewPagerMain);
        mainViewPager1 = view.findViewById(R.id.viewPagerMain);
        mainTabLayout = view.findViewById(R.id.tabLayoutMain);
//        mainViewPager.setAdapter(createMainAdapter());
        mainViewPager1.setAdapter(createMainPagerAdapter());
        mainViewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppBaseFragment prevFragment = mainFragmentPagerAdapter.getRegisteredFragment(prevPos);
//                AppBaseFragment prevFragment = (AppBaseFragment) fragmentManager
//                        .findFragmentByTag("android:switcher:" + mainViewPager1.getId() + ":" + prevPos);
                if (prevFragment != null)
                    prevFragment.onPauseInViewPager();

                AppBaseFragment fragment = mainFragmentPagerAdapter.getRegisteredFragment(position);
//                AppBaseFragment fragment = (AppBaseFragment) fragmentManager
//                        .findFragmentByTag("android:switcher:" + mainViewPager1.getId() + ":" + position);
                if (fragment != null)
                    fragment.onResumeInViewPager();

                prevPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainTabLayout.setupWithViewPager(mainViewPager1);
        totalLikedText = binding.likedNo;
        settingsBtn = binding.settingsBtn;
//        {
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mainTabLayout, mainViewPager,
//                    (tab, position) -> tab.setText(FragmentList.getFragment(position).toString()));
//            tabLayoutMediator.attach();
//        }
        if (likesModel.getCurrentLikedList().getValue() != null)
            bind(BR.total_likes, likesModel.getCurrentLikedList().getValue().size() + "");
        else {
            bind(BR.total_likes, "0");
        }
    }

    public void bind(int id, Object obj) {
        binding.setVariable(id, obj);
        binding.executePendingBindings();
    }

    private void listeners() {


        lottieAnimationView.setOnClickListener(v -> {
//            AppEvent appEvent = new AppEvent(EventDef.Category.TOOLBAR, EventDef.TOOLBAR_EVENTS.SHARE_BTN, 0, 0);
//            eventBus().post(appEvent);
            SharePopup sharePopup = new SharePopup();
            sharePopup.show(((MainActivity) context).getSupportFragmentManager(), "SharePopup");
            lottieAnimationView.playAnimation();
        });

        settingsBtn.setOnClickListener(v -> {
            Fragment fragment = SettingsFragment.newInstance(new Bundle());
            GlobalAppController.switchFragment(R.id.container, fragment, ((MainActivity) context).getSupportFragmentManager(),
                    new int[]{R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_right, R.anim.slide_out_left}, "SettingsFragment");
        });

        galleryBtn.setOnClickListener(v -> {
            Fragment fragment = GalleryFragment.newInstance(new Bundle());
            GlobalAppController.switchFragment(R.id.container, fragment, ((MainActivity) context).getSupportFragmentManager(),
                    new int[]{R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_right, R.anim.slide_out_left}, "GalleryFragment");
        });

    }

    private MainFragmentPagerAdapter createMainPagerAdapter() {
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(fragmentManager) {
        };
        return mainFragmentPagerAdapter;
    }

    void onReceiveAppEvent(@NonNull AppEvent event) {
        switch (event.category) {
            case EventDef.Category.BTN:
                switch (event.event) {
                    case EventDef.BUTTON_EVENTS.GALLERY_BUTTON:
                        Fragment fragment = GalleryFragment.newInstance(new Bundle());
                        GlobalAppController.switchFragment(R.id.container, fragment, ((MainActivity) context).getSupportFragmentManager(),
                                new int[]{R.anim.slide_in_right, R.anim.slide_out_left,
                                        R.anim.slide_in_right, R.anim.slide_out_left}, "GalleryFragment");
                        break;
                }
                break;
            case EventDef.Category.TOOLBAR:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppBaseFragment prevFragment = mainFragmentPagerAdapter.getRegisteredFragment(prevPos);
        if (prevFragment != null)
            prevFragment.onPauseInViewPager();
        eventBus(context).unregister(appEventReceiver);
    }


    @NonNull
    @Override
    public String toString() {
        return "ParentFragment{}";
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        FragmentManager mainFragmentManager = ((MainActivity) context).getSupportFragmentManager();
        int index = mainFragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = mainFragmentManager.getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Toast.makeText(context, "name = " + tag, Toast.LENGTH_SHORT).show();
        if (mainFragmentManager.getBackStackEntryCount() > 1) {
            AppBaseFragment fragment = (AppBaseFragment) mainFragmentManager.findFragmentByTag(tag);
            if (fragment != null) {
                Log.d("mylog", "getCurrentList(ParentFrag): " + fragment.getCurrentList().size());
                return fragment.getCurrentList();
            }
            else {
                Log.d("mylog", "getCurrentList(ParentFrag):frag null ");
                return super.getCurrentList();
            }
        }
        if (mainFragmentPagerAdapter.getCurrentFragment() != null)
            return mainFragmentPagerAdapter.getCurrentFragment().getCurrentList();
        return super.getCurrentList();
    }
}