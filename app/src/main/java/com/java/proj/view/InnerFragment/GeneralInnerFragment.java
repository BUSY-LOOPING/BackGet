package com.java.proj.view.InnerFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.java.proj.view.Activities.ImageDetailActivity;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.CallBacks.GetDataCallBack;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.Events;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.Models.CollectionModel;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.proj.view.api.ApiUtilities;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GeneralInnerFragment extends Fragment implements RecyclerViewListener{

    public static final String KEY_QUERY = "general_fragment_query_retrofit";
    public static final String KEY_TOPIC_SLUG_OR_ID = "general_fragment_topic_slug_id_retrofit";
    public static final String KEY_LAYOUT = "general_fragment_layout_file";

    private Bundle bundle;
    private Context context;
    private CollectionModel collectionModel;
    private int page = 1;
    private final int pageSize = 20;
    private boolean isLoading, isLastPage;

    private FrameLayout loadingLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private ArrayList<GeneralModel> list;
    private RecyclerViewListener listener;

    public GeneralInnerFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Bundle bundle) {
        GeneralInnerFragment fragment = new GeneralInnerFragment();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(bundle.getInt(KEY_LAYOUT), container, false);
        init(view);
        setAdapter();
        return view;
    }


    private void init(View view) {
        listener = this;
        collectionModel = new CollectionModel(new ArrayList<>());
        loadingLayout = view.findViewById(R.id.loadingLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(context, list, listener);
    }

    private void setAdapter() {
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItems = layoutManager.getChildCount();
                int totalItems = layoutManager.getItemCount();
                int firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItems + firstVisibleItemPos >= totalItems) && firstVisibleItemPos >= 0 && totalItems >= pageSize) {
                        page++;
                        getData();
                    }
                }
            }
        });
        getData();
    }

    private void getData() {
//        ApiUtilities.getCollectionsData(context, page, pageSize, bundle.getString(KEY_QUERY), loadingLayout, adapter, list, new GetDataCallBack() {
//            @Override
//            public void isLoading(boolean isLoading) {
//                GeneralInnerFragment.this.isLoading = isLoading;
//            }
//
//            @Override
//            public void isLastPage(boolean isLastPage) {
//                GeneralInnerFragment.this.isLastPage = isLastPage;
//            }
//        });
        ApiUtilities.getTopicsData(context, page, pageSize, bundle.getString(KEY_TOPIC_SLUG_OR_ID), loadingLayout, adapter, list, new GetDataCallBack() {
            @Override
            public void isLoading(boolean isLoading) {
                GeneralInnerFragment.this.isLoading = isLoading;
            }

            @Override
            public void isLastPage(boolean isLastPage) {
                GeneralInnerFragment.this.isLastPage = isLastPage;
            }
        });
    }

    @Override
    public void onClick(int position, View container, View view) {
        AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.IMAGE_CLICK.ordinal(), 0, 0);
        List<View> viewList = new ArrayList<>();
        viewList.add(container);
        viewList.add(view);
        appEvent.weakReferenceList = new WeakReference<>(new ArrayList<>(viewList));
        appEvent.extras.putSerializable(ImageDetailActivity.URL_LIST,(Serializable) list);
        appEvent.extras.putInt(ImageDetailActivity.LOADED_POS, position);
//        appEvent.extras.putString(ImageDetailActivity.LOADED_URL, list.get(position).getUriModel().getRegular());
        AppEventBus appEventBus = new AppEventBus(context);
        appEventBus.post(appEvent);
    }

    @Override
    public void onDownload(int position) {

    }

    @Override
    public void onLike(int position) {

    }

    @Override
    public void onUnlike(int position) {

    }
}