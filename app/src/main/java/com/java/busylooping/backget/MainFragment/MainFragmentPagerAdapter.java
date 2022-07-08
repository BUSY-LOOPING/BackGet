package com.java.busylooping.backget.MainFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.java.busylooping.backget.AppBaseFragment;

import java.util.HashMap;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private AppBaseFragment mCurrentFragment;
    private final HashMap<Integer, AppBaseFragment> registeredFragments = new HashMap<>();

    public MainFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MainFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = (AppBaseFragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }


    public AppBaseFragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AppBaseFragment fragment = (AppBaseFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        Log.d("mylog", "instantiateItem: " + position);
        fragmentInstantiationFinished(position, fragment);
        return fragment;
    }

    protected void fragmentInstantiationFinished(int pos, AppBaseFragment fragment) {
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        Log.d("mylog", "destroyItem: pos = "+  position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public AppBaseFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecentFragment.newInstance(new Bundle());
            case 1:
                return PopularFragment.newInstance(new Bundle());
            case 2:
                return SearchFragment.newInstance(new Bundle());
            case 3:
                return UploadFragment.newInstance(new Bundle());
        }
        return RecentFragment.newInstance(new Bundle());
    }

    @Override
    public int getCount() {
        return FragmentList.getSize();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentList.getFragment(position).toString();
    }
}
