package com.java.busylooping.backget.MainFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.InnerFragment.InnerFragmentAdapter;
import com.java.busylooping.backget.InnerFragment.InnerFragmentPagerAdapter;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.models.GeneralModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularFragment extends AppBaseFragment {

    public PopularFragment() {
        // Required empty public constructor
    }

    //    private ViewPager2 innerViewPager;
    private ViewPager viewPager1;
    private TabLayout innerTabLayout;
    private InnerFragmentPagerAdapter fragmentPagerAdapter;

    private View view;
    private Context context;
    private Bundle bundle;
    private int prevPos = 0;

    @NonNull
    public static PopularFragment newInstance(Bundle bundle) {
        PopularFragment fragment = new PopularFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_popular, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResumeInViewPager() {
        super.onResumeInViewPager();
        AppBaseFragment currFragment = fragmentPagerAdapter.getRegisteredFragment(viewPager1.getCurrentItem());
        if (currFragment != null)
            currFragment.onResumeInViewPager();
    }

    @Override
    public void onPauseInViewPager() {
        super.onPauseInViewPager();
        AppBaseFragment currFragment = fragmentPagerAdapter.getRegisteredFragment(viewPager1.getCurrentItem());
        if (currFragment != null)
            currFragment.onPauseInViewPager();
    }

    private void init(@NonNull View view) {
//        innerViewPager = view.findViewById(R.id.innerViewPager);
        viewPager1 = view.findViewById(R.id.innerViewPager);
        innerTabLayout = view.findViewById(R.id.innerTabLayout);
//        innerViewPager.setAdapter(createInnerAdapter());
//        {
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(innerTabLayout, innerViewPager,
//                    (tab, position) -> tab.setText(InnerFragmentList.getFragment(position).toString()));
//            tabLayoutMediator.attach();
//        }
        fragmentPagerAdapter = createInnerFragmentAdapter();
        viewPager1.setAdapter(fragmentPagerAdapter);
        viewPager1.setOffscreenPageLimit(3);
        innerTabLayout.setupWithViewPager(viewPager1);
        viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppBaseFragment prevFragment = fragmentPagerAdapter.getRegisteredFragment(prevPos);
//                AppBaseFragment prevFragment = (AppBaseFragment) getChildFragmentManager()
//                        .findFragmentByTag("android:switcher:" + viewPager1.getId() + prevPos);
                if (prevFragment != null)
                    prevFragment.onPauseInViewPager();

                AppBaseFragment fragment = fragmentPagerAdapter.getRegisteredFragment(position);
//                AppBaseFragment fragment = (AppBaseFragment) getChildFragmentManager()
//                        .findFragmentByTag("android:switcher:" + viewPager1.getId() + position);
                if (fragment != null)
                    fragment.onResumeInViewPager();


                prevPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        TabLayout.Tab tab_ = innerTabLayout.getTabAt(0);
//        if (tab_ != null) {
//            tab_.setIcon(R.drawable.ic_heart_filled);
//            tab_.setCustomView(R.layout.custom_tab);
//            View root = innerTabLayout.getChildAt(0);
//            if (root instanceof LinearLayout) {
//                ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//                GradientDrawable drawable = new GradientDrawable();
//                drawable.setColor(getResources().getColor(R.color.gray));
//                drawable.setSize(2, 1);
//                ((LinearLayout) root).setDividerPadding(10);
//                ((LinearLayout) root).setDividerDrawable(drawable);
//            }
//        }
    }

    @NonNull
    private InnerFragmentPagerAdapter createInnerFragmentAdapter() {
        return new InnerFragmentPagerAdapter(getChildFragmentManager());
    }

    private InnerFragmentAdapter createInnerAdapter() {
        return new InnerFragmentAdapter(this);
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        if (fragmentPagerAdapter.getCurrentFragment() != null) {
            return fragmentPagerAdapter.getCurrentFragment().getCurrentList();
        }
        return super.getCurrentList();
    }
}