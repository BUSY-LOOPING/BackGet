package com.java.busylooping.backget.MainFragment;

import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.AsyncTasks.BackGroundInvoke;
import com.java.busylooping.backget.CallBacks.GetDataCallBack;
import com.java.busylooping.backget.CallBacks.RecyclerViewListener;
import com.java.busylooping.backget.CallBacks.ScrollCallback;
import com.java.busylooping.backget.CustomRecyclerView.WrapContentGridLayoutManager;
import com.java.busylooping.backget.Fragments.ImageDetailFragment;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.busylooping.backget.api.ApiUtilities;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikesModel;
import com.java.busylooping.backget.utils.AppEvent;
import com.java.busylooping.backget.utils.AppEventBus;
import com.java.busylooping.backget.utils.EventDef;
import com.java.busylooping.backget.utils.GlobalAppController;

import java.util.ArrayList;

public class RecentFragment extends AppBaseFragment implements RecyclerViewListener, ScrollCallback {
    private static final String TAG = "MyRecentFragment";
    private Bundle bundle;
    private Context context;
    public static final String IS_LOADING = "RecentFragmentIsLoadingCallBack";

    private ArrayList<GeneralModel> list;
    private int page = 1;
    private final int pageSize = 20;
    private boolean isLoading;
    private boolean isLastPage;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private GridLayoutManager layoutManager;
    private LinearLayout loadingLayout;
    private RecyclerViewListener listener;
    private FragmentManager fragmentManager;
    private boolean registered = false;
    private LikesModel likesModel;

    private RecentFragment.AppEventReceiver receiver;

    public static class AppEventReceiver extends AppEventBus.Receiver<RecentFragment> {
        public static final int[] FILTERS = {
                EventDef.Category.IMAGE_CLICK,
                EventDef.Category.CALLBACK
        };

        public AppEventReceiver(RecentFragment holder, int[] categoryFilter) {
            super(holder, categoryFilter);
        }

        @Override
        protected void onReceiveAppEvent(RecentFragment holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        }
    }

    private void onReceiveAppEvent(AppEvent appEvent) {
        switch (appEvent.category) {
            case EventDef.Category.IMAGE_CLICK:
                handleImageClick(appEvent);
                break;
            case EventDef.Category.CALLBACK:
                handleCallBack(appEvent);
                break;
        }
    }

    private void handleCallBack(AppEvent event) {
        switch (event.event) {
            case EventDef.CALLBACK_EVENTS.SCROLL_CALLBACK:
                Bundle extras = event.extras;
                int pos = extras.getInt(ImageDetailFragment.POS, -1);
                if (pos != -1 && pos < list.size()) {
                    String id = extras.getString(GlobalAppController.GENERAL_MODEL_ID, null);
                    if (id != null) {
                        if (list.get(pos).getImageId().equals(id)) {
                            scrolledTo(pos);
                        }
                    }
                }


                break;
        }
    }

    private void handleImageClick(AppEvent event) {
        Fragment fragment = ImageDetailFragment.newInstance(event.extras);
//        ImageDetailFragment.setScrollCallback(this);
        fragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager, null, "ImageDetailFragment" + fragmentManager.getBackStackEntryCount());
    }

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

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onResumeInViewPager() {
        super.onResumeInViewPager();
        if (!registered) {
            eventBus(context).register(receiver);
            registered = true;
        }
    }

    @Override
    public void onPauseInViewPager() {
        super.onPauseInViewPager();
        eventBus(context).unregister(receiver);
        registered = false;
    }

    private void init(View view) {
        receiver = new AppEventReceiver(this, AppEventReceiver.FILTERS);
        listener = this;
        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
        fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        loadingLayout = view.findViewById(R.id.loadingLayout);
//        loadingLayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerViewAdapter(context, list, listener, R.layout.recycler_view_item);
    }

    private void setAdapter() {
        BackGroundInvoke<Void, Void, Void> backGroundInvoke = new BackGroundInvoke<>(new BackGroundInvoke.Command() {
            @Override
            public void execute(Object[] data) {
        layoutManager = new WrapContentGridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
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
        }, context);
        backGroundInvoke.execute();

    }

    private void getData() {
        {
            AppEvent appEvent = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.LOADING, 0, 0);
            eventBus(context).post(appEvent);
        }
        loadingLayout.setVisibility(View.VISIBLE);
        isLoading = true;
        final int[] initListSize = {list.size()};
        ApiUtilities.getListImageModel(
                context,
                ApiUtilities.getApiInterface().getImages(page, 30, "latest"),
                list,
                adapter,
                pageSize,
                likesModel,
                new GetDataCallBack() {

                    @Override
                    public void isLoading(boolean isLoading) {
                        RecentFragment.this.isLoading = isLoading;
                        AppEvent appEvent1;
                        if (isLoading) {
                            appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.LOADING, 0, 0);
                            loadingLayout.setVisibility(View.VISIBLE);
                        } else {
                            appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.NOT_LOADING, 0, 0);
                            loadingLayout.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingLayout.setVisibility(View.GONE);
                                }
                            });
                        }
                        AppEvent appEvent2 = new AppEvent(EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, initListSize[0], list.size() - initListSize[0]);
                        eventBus(context).post(appEvent2);
                        eventBus(context).post(appEvent1);
                        initListSize[0] = list.size();
                    }

                    @Override
                    public void isLastPage(boolean isLastPage) {
                        RecentFragment.this.isLastPage = isLastPage;
                    }
                });

//        ApiUtilities.getApiInterface().getImages(page, 30, "latest")
//                .enqueue(new Callback<List<ImageModel>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<ImageModel>> call, @NonNull Response<List<ImageModel>> response) {
//                        if (response.body() != null) {
//                            int startIndex = list.size();
//                            for (ImageModel imageModel : response.body()) {
//                                GeneralModel generalModel = new GeneralModel(
//                                        imageModel.getUrls(),
//                                        imageModel.getId(),
//                                        imageModel.getDescription(),
//                                        imageModel.getLikes(),
//                                        imageModel.getUserModel(),
//                                        imageModel.getLinksModel(),
//                                        imageModel.isLikedByUser());
//                                list.add(generalModel);
//
//                                if (likesModel.getCurrentLikedList().getValue() != null && likesModel.getCurrentLikedList().getValue().containsKey(generalModel.getImageId())) {
//                                    likesModel.getLastLikedModel().setValue(generalModel);
//                                }
//                            }
//
//                            adapter.notifyItemRangeInserted(startIndex, response.body().size());
//                            loadingLayout.setVisibility(View.GONE);
//                            AppEvent appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.NOT_LOADING, 0, 0);
//                            AppEvent appEvent2 = new AppEvent(EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, 0, 0);
//                            appEvent2.extras.putSerializable(ImageDetailFragment.NEW_LIST, new ArrayList<>(list.subList(startIndex, list.size())));
//                            eventBus(context).post(appEvent2);
//                            eventBus(context).post(appEvent1);
//                        }
//                        isLoading = false;
//                        if (list.size() > 0) {
//                            isLastPage = list.size() < pageSize;
//                        } else isLastPage = true;
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<ImageModel>> call, @NonNull Throwable t) {
//                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
//                        loadingLayout.setVisibility(View.GONE);
//                        AppEvent appEvent = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.NOT_LOADING, 0, 0);
//                        eventBus(context).post(appEvent);
//                    }
//                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        long start = System.currentTimeMillis();

        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        Log.d(TAG, "time: " + ((System.currentTimeMillis()) - start));

        init(view);
        setAdapter();

        return view;
    }

    @Override
    public void onClick(int position, View container, View view) {
        AppEvent appEvent = new AppEvent(EventDef.Category.IMAGE_CLICK, EventDef.Category.IMAGE_CLICK, 0, 0);
        appEvent.extras.putInt(ImageDetailFragment.POS, position);
        AppEventBus appEventBus = eventBus(context);
        if (!registered) {
            appEventBus.register(receiver);
            registered = true;
        }
        appEventBus.post(appEvent);
        Log.d(TAG, "onClick: ");
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

    @Override
    public void scrolledTo(int pos) {
        layoutManager.scrollToPosition(pos);
    }

    @Override
    public void onPause() {
        super.onPause();
//        eventBus().unregister(receiver);
    }

    @NonNull
    @Override
    public String toString() {
        return "RecentFragment{}";
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        return list;
    }
}