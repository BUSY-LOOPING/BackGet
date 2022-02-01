package com.java.proj.view.MainFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    public MainFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MainFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
