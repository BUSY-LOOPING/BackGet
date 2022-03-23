package com.java.proj.view;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;

public class AppBaseBottomSheetDialogFragment extends AppCompatDialogFragment {

    protected GlobalAppController getAppController() {
        return ((MainActivity)getActivity()).globalAppController();
    }

    protected AppEventBus eventBus() {
        return ((MainActivity)getActivity()).getEventBus();
    }
}
