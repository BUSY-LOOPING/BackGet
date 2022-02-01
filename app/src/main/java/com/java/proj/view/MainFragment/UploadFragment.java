package com.java.proj.view.MainFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.R;

public class UploadFragment extends AppBaseFragment {

    private Bundle bundle;

    public UploadFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(Bundle bundle) {
        UploadFragment fragment = new UploadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }
}