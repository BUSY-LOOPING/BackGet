package com.java.proj.view;

import androidx.fragment.app.Fragment;

import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.GlobalAppController;

public class AppBaseFragment extends Fragment {

    protected GlobalAppController getAppController() {
        return ((MainActivity)getActivity()).globalAppController();
    }

    protected AppEventBus eventBus() {
        return ((MainActivity)getActivity()).getEventBus();
    }
}
