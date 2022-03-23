package com.java.proj.view;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;

@SuppressWarnings({"ConstantConditions"})
public class AppBaseFragment extends Fragment {
    protected GlobalAppController getAppController() {
        return ((MainActivity) getActivity()).globalAppController();
    }

    protected AppEventBus eventBus() {
        return ((MainActivity) getActivity()).getEventBus();
    }

    @NonNull
    protected AppEventBus eventBus(@NonNull Context context) {
        return ((MainActivity) context).getEventBus();
    }

    public void onResumeInViewPager() {
    }

    public void onPauseInViewPager() {
    }

}
