package com.java.proj.view;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

import kotlin.UnsafeVariance;

public class AppBaseFragment extends Fragment {

    @Nullable
    protected GlobalAppController getAppController() {
        return ((MainActivity) getActivity()).globalAppController();
    }


    @NonNull
    protected GlobalAppController getAppController(@NonNull Context context) {
        return ((MainActivity) context).globalAppController();
    }


    @NonNull
    protected AppEventBus eventBus(@NonNull Context context) {
        return ((MainActivity) context).getEventBus();
    }

    public void onResumeInViewPager() {
    }

    public void onPauseInViewPager() {
    }

    @NonNull
    public ArrayList<GeneralModel> getCurrentList() {
        return new ArrayList<>();
    }


}
