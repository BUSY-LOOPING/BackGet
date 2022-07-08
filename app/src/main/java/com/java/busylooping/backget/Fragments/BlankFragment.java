package com.java.busylooping.backget.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.R;


public class BlankFragment extends AppBaseFragment {

    private Bundle bundle;

    public BlankFragment() {
        // Required empty public constructor
    }


    public static BlankFragment newInstance(Bundle bundle) {
        BlankFragment fragment = new BlankFragment();
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
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}