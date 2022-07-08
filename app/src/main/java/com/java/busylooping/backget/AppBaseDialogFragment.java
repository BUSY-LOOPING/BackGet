package com.java.busylooping.backget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.java.busylooping.backget.utils.AppEventBus;
import com.java.busylooping.backget.utils.GlobalAppController;

public class AppBaseDialogFragment extends DialogFragment {
    protected GlobalAppController getAppController() {
        return ((MainActivity)getActivity()).globalAppController();
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
