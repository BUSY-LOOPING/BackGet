package com.java.proj.view.MainFragment;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.proj.view.InnerFragment.InnerFragmentAdapter;
import com.java.proj.view.InnerFragment.InnerFragmentList;
import com.java.proj.view.InnerFragment.InnerFragmentPagerAdapter;
import com.java.proj.view.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularFragment extends Fragment {

    public PopularFragment() {
        // Required empty public constructor
    }

//    private ViewPager2 innerViewPager;
    private ViewPager viewPager1;
    private TabLayout innerTabLayout;

    private View view;
    private Context context;
    private Bundle bundle;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_popular, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
//        innerViewPager = view.findViewById(R.id.innerViewPager);
        viewPager1 = view.findViewById(R.id.innerViewPager);
        innerTabLayout = view.findViewById(R.id.innerTabLayout);
//        innerViewPager.setAdapter(createInnerAdapter());
//        {
//            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(innerTabLayout, innerViewPager,
//                    (tab, position) -> tab.setText(InnerFragmentList.getFragment(position).toString()));
//            tabLayoutMediator.attach();
//        }
        viewPager1.setAdapter(createInnerFragmentAdapter());
        innerTabLayout.setupWithViewPager(viewPager1);
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

    private InnerFragmentPagerAdapter createInnerFragmentAdapter() {
        return new InnerFragmentPagerAdapter(getChildFragmentManager());
    }

    private InnerFragmentAdapter createInnerAdapter() {
        return new InnerFragmentAdapter(this);
    }

}