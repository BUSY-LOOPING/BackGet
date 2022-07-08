package com.java.busylooping.backget.InnerFragment;

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

public class GeneralInnerFragment extends AppBaseFragment implements RecyclerViewListener, ScrollCallback {
    private final String TAG = "MyGeneralInnerFragment";
    public static final String KEY_QUERY = "general_fragment_query_retrofit";
    public static final String KEY_TOPIC_SLUG_OR_ID = "general_fragment_topic_slug_id_retrofit";
    public static final String KEY_LAYOUT = "general_fragment_layout_file";

//    private static WeakReference<View> viewWeakReference = null;

    private Bundle bundle;
    private Context context;
    private int page = 1;
    private final int pageSize = 20;
    private boolean isLoading, isLastPage;
    private FragmentManager fragmentManager;

    private LinearLayout loadingLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private ArrayList<GeneralModel> list;
    private RecyclerViewListener listener;
    private LikesModel likesModel;

    private AppEventReceiver appEventReceiver;
    private boolean registered = false;

    public static class AppEventReceiver extends AppEventBus.Receiver<GeneralInnerFragment> {
        public static final int[] FILTERS = {
                EventDef.Category.IMAGE_CLICK,
                EventDef.Category.CALLBACK
        };

        public AppEventReceiver(GeneralInnerFragment holder, int[] categoryFilter) {
            super(holder, categoryFilter);
        }

        @Override
        protected void onReceiveAppEvent(GeneralInnerFragment holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        }
    }

    void onReceiveAppEvent(AppEvent event) {
        switch (event.category) {
            case EventDef.Category.IMAGE_CLICK:
                handleImageClick(event);
                break;
            case EventDef.Category.CALLBACK:
                handleCallBack(event);
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
        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager, null, "ImageDetailFragment");
    }


    public GeneralInnerFragment() {
        // Required empty public constructor
    }

    public static AppBaseFragment newInstance(Bundle bundle) {
        AppBaseFragment fragment = new GeneralInnerFragment();
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
    public void onResumeInViewPager() {
        if (!registered) {
            eventBus(context);
            eventBus(context).register(appEventReceiver);
            registered = true;
        }
    }

    @Override
    public void onPauseInViewPager() {
        eventBus(context).unregister(appEventReceiver);
        registered = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        if (viewWeakReference == null || viewWeakReference.get() == null) {
//            long start = System.currentTimeMillis();
//            viewWeakReference = new WeakReference<>(inflater.inflate(bundle.getInt(KEY_LAYOUT), container, false));
//            Log.d(TAG, "time: " + ((System.currentTimeMillis()) - start));
//        } else {
//            View view = viewWeakReference.get();
//            if (view.getParent() != null) {
//                ((ViewGroup)view.getParent()).removeAllViews();
//            }
//        }
        View view = inflater.inflate(bundle.getInt(KEY_LAYOUT), container, false);
        init(view);
        setAdapter();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(View view) {
        list = new ArrayList<>();
        appEventReceiver = new AppEventReceiver(this, AppEventReceiver.FILTERS);
        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
//        likesModel.getCurrentLikedList().observe((LifecycleOwner) context, observer);
        fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        listener = this;
        loadingLayout = view.findViewById(R.id.loadingLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(context, list, listener, R.layout.recycler_view_item);

    }

    private void setAdapter() {
        BackGroundInvoke<Void, Void, Void> backGroundInvoke = new BackGroundInvoke<>(new BackGroundInvoke.Command() {
            @Override
            public void execute(Object[] data) {
                layoutManager = new WrapContentGridLayoutManager(context, 2);
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
        final int[] initListSize = new int[]{list.size()};

        ApiUtilities.getTopicsData(context, page, pageSize, bundle.getString(KEY_TOPIC_SLUG_OR_ID), loadingLayout, adapter, list, likesModel, new GetDataCallBack() {
            @Override
            public void isLoading(boolean isLoading) {
                GeneralInnerFragment.this.isLoading = isLoading;
                if (isLoading) {
                    AppEvent appEvent = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.LOADING, 0, 0);
                    eventBus(context).post(appEvent);
                } else {
                    AppEvent appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.NOT_LOADING, 0, 0);
                    AppEvent appEvent2 = new AppEvent(EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, initListSize[0], list.size() - initListSize[0]);
//                    appEvent2.extras.putSerializable(ImageDetailFragment.NEW_LIST, new ArrayList<>(list.subList(startIndex, list.size())));
                    eventBus(context).post(appEvent2);
                    eventBus(context).post(appEvent1);
                    initListSize[0] = list.size();
                }
            }

            @Override
            public void isLastPage(boolean isLastPage) {
                GeneralInnerFragment.this.isLastPage = isLastPage;
            }
        });
    }

    @Override
    public void onClick(int position, View container, View view) {
        AppEvent appEvent = new AppEvent(EventDef.Category.IMAGE_CLICK, EventDef.Category.IMAGE_CLICK, 0, 0);
        appEvent.extras.putInt(ImageDetailFragment.POS, position);
        AppEventBus appEventBus = eventBus(context);
        if (!registered) {
            appEventBus.register(appEventReceiver);
            registered = true;
        }
        appEventBus.post(appEvent);
        Log.d(TAG, "onClick(GeneralInnerFragment): ");
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

//        String accessToken = context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ApiUtilities.ACCESS_TOKEN, "");
//        LikedDataBase db = LikedDataBase.getInstance(context);
//        db.likePicture(list.get(position));
//        db.close();
//        likesModel.addModel(list.get(position), true);
//        ApiUtilities.getApiInterface(accessToken).likePicture(list.get(position).getImageId()).enqueue(new Callback<LikeUnlikeModel>() {
//            @Override
//            public void onResponse(@NonNull Call<LikeUnlikeModel> call, @NonNull Response<LikeUnlikeModel> response) {
//                if (response.body() != null) {
//
//                } else{
//                    Log.d("mylog", response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<LikeUnlikeModel> call, @NonNull Throwable t) {
//                Log.e("myerr", t.getLocalizedMessage());
//            }
//        });

    }

    @Override
    public void onUnlike(GeneralModel generalModel) {
        AppEvent appEvent = new AppEvent(EventDef.Category.LIKE_UNLIKE, EventDef.LIKE_UNLIKE_EVENTS.UNLIKED, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalAppController.GENERAL_MODEL, generalModel);
        appEvent.extras = bundle;
        eventBus(context).post(appEvent);

//        LikedDataBase db = LikedDataBase.getInstance(context);
//        db.unlikePicture(list.get(position));
//        db.close();
//        likesModel.removeModel(list.get(position), true);
//        String accessToken = context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ApiUtilities.ACCESS_TOKEN, "");
//        ApiUtilities.getApiInterface(accessToken).unLikePicture(list.get(position).getImageId()).enqueue(new Callback<LikeUnlikeModel>() {
//            @Override
//            public void onResponse(@NonNull Call<LikeUnlikeModel> call, @NonNull Response<LikeUnlikeModel> response) {
//                if (response.body() != null) {
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<LikeUnlikeModel> call, @NonNull Throwable t) {
//                Log.e("myerr", t.getLocalizedMessage());
//            }
//        });


    }

    @Override
    public void scrolledTo(int pos) {
        layoutManager.scrollToPosition(pos);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @NonNull
    @Override
    public String toString() {
        return bundle.getString(KEY_TOPIC_SLUG_OR_ID) + "{}";
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + this);
    }
}