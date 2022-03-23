package com.java.proj.view.InnerFragment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.GetDataCallBack;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.CallBacks.ScrollCallback;
import com.java.proj.view.DataBase.LikedDataBase;
import com.java.proj.view.Fragments.ImageDetailFragment;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.LikesModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.api.ApiUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GeneralInnerFragment extends AppBaseFragment implements RecyclerViewListener, ScrollCallback {
    private final String TAG = "MyGeneralInnerFragment";
    public static final String KEY_QUERY = "general_fragment_query_retrofit";
    public static final String KEY_TOPIC_SLUG_OR_ID = "general_fragment_topic_slug_id_retrofit";
    public static final String KEY_LAYOUT = "general_fragment_layout_file";

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
    private final Observer<List<GeneralModel>> observer = new Observer<List<GeneralModel>>() {
        @Override
        public void onChanged(List<GeneralModel> generalModels) {
//            for (GeneralModel generalModel : generalModels) {
//                GeneralModel model = Iterables.tryFind(list,
//                        new Predicate<GeneralModel>() {
//                            @Override
//                            public boolean apply(GeneralModel input) {
//                                return input.getImageId().equals(generalModel.getImageId());
//                            }
//                        }).orNull();
//                if (model != null) {
//                    model.setLikes(generalModel.getLikes());
//                    model.setLiked(true);
//                }
//            }
        }
    };

    private AppEventReceiver appEventReceiver;
    private boolean registered = false;

    public static class AppEventReceiver extends AppEventBus.Receiver<GeneralInnerFragment> {
        public static final int[] FILTERS = {
                EventDef.Category.IMAGE_CLICK
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
        }
    }

    private void handleImageClick(AppEvent event) {
        Log.d(TAG, "handleImageClick: general");
        Fragment fragment = ImageDetailFragment.newInstance(event.extras);
        ImageDetailFragment.setScrollCallback(this);
        fragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager, null);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        int startIndex = list.size();
        ApiUtilities.getTopicsData(context, page, pageSize, bundle.getString(KEY_TOPIC_SLUG_OR_ID), loadingLayout, adapter, list, new GetDataCallBack() {
            @Override
            public void isLoading(boolean isLoading) {
                GeneralInnerFragment.this.isLoading = isLoading;
                if (isLoading) {
                    AppEvent appEvent = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.LOADING, 0, 0);
                    eventBus(context).post(appEvent);
                } else {
                    AppEvent appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.NOT_LOADING, 0, 0);
                    AppEvent appEvent2 = new AppEvent(EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, 0, 0);
                    appEvent2.extras.putSerializable(ImageDetailFragment.NEW_LIST, new ArrayList<>(list.subList(startIndex, list.size())));
                    eventBus(context).post(appEvent2);
                    eventBus(context).post(appEvent1);
                    observer.onChanged(likesModel.getCurrentLikedList().getValue());
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
        String transitionNameImgContainer = context.getString(R.string.target_img_transition_parent);
        String transitionNameImg = context.getString(R.string.target_img_transition);
        AppEvent appEvent = new AppEvent(EventDef.Category.IMAGE_CLICK, EventDef.Category.IMAGE_CLICK, 0, 0);
//        ArrayList<Pair<String, Object>> viewList = new ArrayList<>();
//        viewList.add(new Pair<>(transitionNameImgContainer, container));
//        viewList.add(new Pair<>(transitionNameImg, view));
//        appEvent.weakReferenceList = new WeakReference<>(viewList);
        appEvent.extras.putSerializable(ImageDetailFragment.LIST, (Serializable) list);
        appEvent.extras.putInt(ImageDetailFragment.POS, position);
//        appEvent.extras.putString(ImageDetailFragment.LOADED_URL, list.get(position).getUriModel().getRegular());
        AppEventBus appEventBus = eventBus(context);
        if (!registered) {
            appEventBus.register(appEventReceiver);
            registered = true;
        }
        appEventBus.post(appEvent);
        Log.d(TAG, "onClick(GeneralInnerFragment): ");
    }

    @Override
    public void onDownload(int position) {

    }

    @Override
    public void onLike(int position) {
//        LikesModel.InsertModelAsyncTask task = new LikesModel.InsertModelAsyncTask(context, likesModel.getCurrentLikedList());
//        task.execute(list.get(position));
        LikedDataBase db = LikedDataBase.getInstance(context);
        db.likePicture(list.get(position));
        db.close();
        likesModel.addModel(list.get(position));
    }

    @Override
    public void onUnlike(int position) {
//        LikesModel.DeleteModelAsyncTask task = new LikesModel.DeleteModelAsyncTask(context, likesModel.getCurrentLikedList());
//        task.execute(list.get(position));
        LikedDataBase db = LikedDataBase.getInstance(context);
        db.unlikePicture(list.get(position));
        db.close();
        likesModel.removeModel(list.get(position));

    }

    @Override
    public void scrolledTo(int pos) {
        layoutManager.scrollToPosition(pos);
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}