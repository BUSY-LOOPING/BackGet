package com.java.proj.view.MainFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.proj.view.Activities.ImageDetailActivity;
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.Events;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.ImageModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.api.ApiUtilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentFragment extends AppBaseFragment implements RecyclerViewListener {
    private Bundle bundle;
    private Context context;
    private ArrayList<GeneralModel> list;
    private int page = 1;
    private final int pageSize = 20;
    private boolean isLoading;
    private boolean isLastPage;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private GridLayoutManager layoutManager;
    private FrameLayout loadingLayout;
    private RecyclerViewListener listener;
    private FragmentManager fragmentManager;

    public RecentFragment() {
        // Required empty public constructor
    }

    public static RecentFragment newInstance(Bundle bundle) {
        RecentFragment fragment = new RecentFragment();
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

    private void init(View view) {
        listener = this;
        fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        loadingLayout = view.findViewById(R.id.loadingLayout);
//        loadingLayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerViewAdapter(context, list, listener);
    }

    private void setAdapter() {
        layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
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
        loadingLayout.setVisibility(View.VISIBLE);
        isLoading = true;
        ApiUtilities.getApiInterface().getImages(page, 30, "latest")
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ImageModel>> call, @NonNull Response<List<ImageModel>> response) {
                        if (response.body() != null) {
                            int startIndex = list.size();
                            for (ImageModel imageModel : response.body()) {
                                list.add(new GeneralModel(imageModel.getUrls(), imageModel.getId(), imageModel.getDescription(), imageModel.getLikes(), imageModel.getUserModel()));
                            }
                            adapter.notifyItemRangeInserted(startIndex, response.body().size());
                            loadingLayout.setVisibility(View.GONE);
                        }
                        isLoading = false;
                        if (list.size() > 0) {
                            isLastPage = list.size() < pageSize;
                        } else isLastPage = true;
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ImageModel>> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        init(view);
        setAdapter();
        return view;
    }


    @Override
    public void onClick(int position, View container, View view) {
        AppEvent appEvent = new AppEvent(AppEvent.AppEventCategory.BUTTON_CLICK.getValue(), Events.IMAGE_CLICK.ordinal(), 0, 0);
        List<View> viewList = new ArrayList<>();
        viewList.add(container);
        viewList.add(view);
        appEvent.weakReferenceList = new WeakReference<>(new ArrayList<>(viewList));
        appEvent.extras.putString(ImageDetailActivity.LOADED_URL, list.get(position).getUriModel().getRegular());
        AppEventBus appEventBus = eventBus();
        if (appEventBus != null)
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