package com.java.busylooping.backget.MainFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainFragmentAdapter extends FragmentStateAdapter {
    private Context context;

    public MainFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public MainFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        context = fragmentActivity.getApplicationContext();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
    public int getItemCount() {
        return FragmentList.getSize();
    }

}
