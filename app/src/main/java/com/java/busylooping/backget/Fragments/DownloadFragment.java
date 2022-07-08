package com.java.busylooping.backget.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.java.busylooping.backget.CallBacks.RecyclerViewListener;
import com.java.busylooping.backget.DataBase.LikedDataBase;
import com.java.busylooping.backget.GeneralAppBaseFragment;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.RecyclerViewAdapters.DownloadRecyclerViewAdapter;
import com.java.busylooping.backget.databinding.FragmentDownloadBinding;
import com.java.busylooping.backget.models.DownloadGeneralModel;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.utils.AppEvent;
import com.java.busylooping.backget.utils.EventDef;
import com.java.busylooping.backget.utils.GlobalAppController;

import java.util.ArrayList;

public class DownloadFragment extends GeneralAppBaseFragment implements RecyclerViewListener {

    private Context context;
    private Bundle bundle;
    private FragmentDownloadBinding binding;

    private ArrayList<DownloadGeneralModel> list;

    private RecyclerView recyclerView;
    private DownloadRecyclerViewAdapter adapter;


    public DownloadFragment() {
        // Required empty public constructor
    }

    public static DownloadFragment newInstance(Bundle bundle) {
        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false);
        View view = binding.getRoot();
        init(binding);
        listeners();
        super.initializeView(view);
        return view;
    }

    private void listeners() {
    }

    private void init(FragmentDownloadBinding binding) {
        LikedDataBase likedDataBase = LikedDataBase.getInstance(context);
        list = likedDataBase.getDownloadList();

        recyclerView = binding.recyclerView;
        adapter = new DownloadRecyclerViewAdapter(context, list, this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public String getTitle() {
        return "Downloads";
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        return new ArrayList<>(list);
    }

    @Override
    public void onClick(int position, View container, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(ImageDetailFragment.POS, position);
        Fragment fragment = ImageDetailFragment.newInstance(bundle);
        fragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager, null, "ImageDetailFragment" + fragmentManager.getBackStackEntryCount());
    }

    @Override
    public void onUtils(GeneralModel generalModel) {

    }

    @Override
    public void onLike(GeneralModel generalModel) {
        AppEvent appEvent = new AppEvent(EventDef.Category.LIKE_UNLIKE, EventDef.LIKE_UNLIKE_EVENTS.LIKED, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalAppController.GENERAL_MODEL, generalModel);
        appEvent.extras = bundle;
        eventBus(context).post(appEvent);
    }

    @Override
    public void onUnlike(GeneralModel generalModel) {
        AppEvent appEvent = new AppEvent(EventDef.Category.LIKE_UNLIKE, EventDef.LIKE_UNLIKE_EVENTS.UNLIKED, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalAppController.GENERAL_MODEL, generalModel);
        appEvent.extras = bundle;
        eventBus(context).post(appEvent);
    }
}