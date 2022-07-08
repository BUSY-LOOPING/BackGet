package com.java.busylooping.backget.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cjj.sva.JJSearchView;
import com.cjj.sva.anim.JJBaseController;
import com.cjj.sva.anim.controller.JJChangeArrowController;
import com.java.busylooping.backget.CallBacks.RecyclerViewListener;
import com.java.busylooping.backget.GeneralAppBaseFragment;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.Popup.UtilsPopup;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.RecyclerViewAdapters.RecyclerViewAdapter2;
import com.java.busylooping.backget.databinding.FragmentLikesBinding;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikesModel;
import com.java.busylooping.backget.utils.AppEvent;
import com.java.busylooping.backget.utils.EventDef;
import com.java.busylooping.backget.utils.GlobalAppController;

import java.util.ArrayList;

public class LikesFragment extends GeneralAppBaseFragment implements RecyclerViewListener {
    private Context context;
    private Bundle bundle;
    private FragmentLikesBinding binding;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter2 adapter;
    private ArrayList<GeneralModel> list;

    private JJSearchView jjSearchView;
    private EditText searchEt;

    public LikesFragment() {
        //empty constructor
    }

    public static LikesFragment newInstance(Bundle bundle) {
        LikesFragment likesFragment = new LikesFragment();
        likesFragment.setArguments(bundle);
        return likesFragment;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_likes, container, false);
        View view = binding.getRoot();
        init(binding);
        listeners();
        super.initializeView(view);
        return view;
    }

    private void init(@NonNull FragmentLikesBinding binding) {
        recyclerView = binding.recyclerView;
        searchEt = binding.searchEt;
        jjSearchView = binding.icon;

        JJBaseController jjBaseController = new JJChangeArrowController();
        jjBaseController.setColor(ContextCompat.getColor(context, R.color.white));
        jjBaseController.setBackgroundTint(ContextCompat.getColor(context, R.color.hint));
        jjBaseController.setSearchView(jjSearchView);
        jjSearchView.setController(jjBaseController);


        LikesModel likesModel = new ViewModelProvider((ViewModelStoreOwner) context)
                .get(LikesModel.class);
        if (likesModel.getCurrentLikedList().getValue() != null) {
            list = new ArrayList<>(likesModel.getCurrentLikedList().getValue().values());
        }
        else {
            list = new ArrayList<>();
        }

        adapter = new RecyclerViewAdapter2(context, list, this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//        SpannedGridLayoutManager layoutManager = new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
//            @Override
//            public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
//                // Conditions for 2x2 items
//                if (list.get(position).getDescription() != null && Objects.requireNonNull(list.get(position).getDescription()).length() > 50) {
//                    return new SpannedGridLayoutManager.SpanInfo(2, 3);
//                } else {
//                    return new SpannedGridLayoutManager.SpanInfo(1, 3);
//                }
//            }
//        }, 2, 1f);
//        layoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void listeners() {
        searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    jjSearchView.startAnim();
                } else jjSearchView.resetAnim();
            }
        });
    }

    @Override
    public String getTitle() {
        return "Likes";
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
        Bundle bundle = new Bundle();
        bundle.putSerializable(UtilsPopup.GENERAL_MODEL, generalModel);
        AppEvent appEvent = new AppEvent(EventDef.Category.VIEW_DIALOG, EventDef.VIEW_DIALOG.SHOW_UTILS_DIALOG, 0, 0);
        appEvent.extras = bundle;
        eventBus(context).post(appEvent);
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

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        return new ArrayList<>(list);
    }
}
