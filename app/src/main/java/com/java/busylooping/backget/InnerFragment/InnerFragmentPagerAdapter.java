package com.java.busylooping.backget.InnerFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.R;

import java.util.HashMap;

public class InnerFragmentPagerAdapter extends FragmentPagerAdapter {
    private AppBaseFragment mCurrentFragment;
    private final HashMap<Integer, AppBaseFragment> registeredFragments = new HashMap<>();
    private Context context;

    public InnerFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public InnerFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
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
        if (!registeredFragments.containsKey(position))
            registeredFragments.put(position, fragment);
        return fragment;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public AppBaseFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GeneralInnerFragment.KEY_LAYOUT, R.layout.fragment_inner_general);
        bundle.putString(GeneralInnerFragment.KEY_QUERY, InnerFragmentList.getFragment(position).getQuery());
        String s = InnerFragmentList.getFragment(position).getTopicIDorSlug();
        bundle.putString(GeneralInnerFragment.KEY_TOPIC_SLUG_OR_ID, s);
        Log.d("myadapter", "pos: " + position + " = " + s);
        return GeneralInnerFragment.newInstance(bundle);
    }

    @Override
    public int getCount() {
        return InnerFragmentList.getSize();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return InnerFragmentList.getFragment(position).toString();
    }
}
