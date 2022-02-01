package com.java.proj.view.InnerFragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.java.proj.view.R;

public class InnerFragmentAdapter extends FragmentStateAdapter {
    private Context context;

    public InnerFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
        context = fragment.getContext();
    }

    public InnerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public InnerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        context = fragmentActivity.getApplicationContext();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(GeneralInnerFragment.KEY_LAYOUT, R.layout.fragment_inner_general);
        bundle.putString(GeneralInnerFragment.KEY_QUERY, InnerFragmentList.getFragment(position).getQuery());
        bundle.putString(GeneralInnerFragment.KEY_TOPIC_SLUG_OR_ID, InnerFragmentList.getFragment(position).getTopicIDorSlug());
        return GeneralInnerFragment.newInstance(bundle);
    }

    @Override
    public int getItemCount() {
        return InnerFragmentList.getSize();
    }
}
