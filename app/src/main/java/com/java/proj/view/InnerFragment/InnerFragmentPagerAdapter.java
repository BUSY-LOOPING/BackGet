package com.java.proj.view.InnerFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.java.proj.view.R;

public class InnerFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public InnerFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public InnerFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GeneralInnerFragment.KEY_LAYOUT, R.layout.fragment_inner_general);
        bundle.putString(GeneralInnerFragment.KEY_QUERY, InnerFragmentList.getFragment(position).getQuery());
        String s = InnerFragmentList.getFragment(position).getTopicIDorSlug();
        bundle.putString(GeneralInnerFragment.KEY_TOPIC_SLUG_OR_ID, s);
        Log.d("myadapter", "pos: " + position + " = " +s);
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
