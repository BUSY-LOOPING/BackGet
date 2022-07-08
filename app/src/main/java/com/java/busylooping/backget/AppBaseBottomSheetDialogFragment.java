package com.java.busylooping.backget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.busylooping.backget.utils.AppEventBus;
import com.java.busylooping.backget.utils.GlobalAppController;

public class AppBaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    protected GlobalAppController getAppController() {
        return ((MainActivity)getActivity()).globalAppController();
    }
    @NonNull
    protected GlobalAppController getAppController(@NonNull Context context) {
        return ((MainActivity)context).globalAppController();
    }

    @Nullable
    protected AppEventBus eventBus() {
        return ((MainActivity)getActivity()).getEventBus();
    }

    @NonNull
    protected AppEventBus eventBus(@NonNull Context context) {
        return ((MainActivity)context).getEventBus();
    }
}
