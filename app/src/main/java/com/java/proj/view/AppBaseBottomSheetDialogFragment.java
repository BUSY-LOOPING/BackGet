package com.java.proj.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;

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
