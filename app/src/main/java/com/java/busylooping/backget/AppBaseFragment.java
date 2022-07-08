package com.java.busylooping.backget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.utils.AppEventBus;
import com.java.busylooping.backget.utils.GlobalAppController;

import java.util.ArrayList;

public class AppBaseFragment extends Fragment {
    private View baseGeneralInnerFragView = null;

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


    public void setBaseGeneralInnerFragView(View baseGeneralInnerFragView) {
        this.baseGeneralInnerFragView = baseGeneralInnerFragView;
    }
}
