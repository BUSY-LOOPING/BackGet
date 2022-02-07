package com.java.proj.view.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.ScrollCallback;
import com.java.proj.view.CustomRecyclerView.SingleImageRecycleView;
import com.java.proj.view.MainFragment.RecentFragment;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.SingleImageScrollableAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;

import java.util.ArrayList;


public class ImageDetailFragment extends AppBaseFragment {
    public static final String BUNDLE_VAL = "ImageDetailActivityBundle";
    public static final String LOADED_URL = "ImageDetailActivityImgUrl";
    public static final String LIST = "ImageDetailActivityImgList";
    public static final String POS = "ImageDetailActivityImgCurrentPos";
    public static final String NEW_LIST = "ImageDetailActivityNewListAppended";
    public static final String TAG = "ImageDetailFragment";
    public static ScrollCallback scrollCallback;

    private Bundle bundle;
    private Context context;
    private ArrayList<GeneralModel> list;
    private int pos;

    //views
    private ImageView backImg;
    private SingleImageRecycleView recycleView;
    private MaterialCardView backBtn;
    private LinearLayout loadingLayout;

    private SingleImageScrollableAdapter adapter;
    private LinearLayoutManager layoutManager;

    private AppEventReceiver appEventReceiver;

    public static class AppEventReceiver extends AppEventBus.Receiver<ImageDetailFragment> {
        public static final int[] FILTERS = {
                EventDef.CALLBACK_EVENTS.LOADING_CALLBACK,
                EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK
        };

        public AppEventReceiver(ImageDetailFragment holder, int[] categoryFilter) {
            super(holder, categoryFilter);
        }

        @Override
        protected void onReceiveAppEvent(ImageDetailFragment holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        }
    }

    void onReceiveAppEvent(AppEvent event) {
        switch (event.category) {
            case EventDef.CALLBACK_EVENTS.LOADING_CALLBACK:
                handleLoadingCallBack(event);
                break;
            case EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK:
                handleRefreshCallBack(event);
        }
    }

    private void handleRefreshCallBack(AppEvent event) {
        ArrayList<GeneralModel> listAdded = (ArrayList<GeneralModel>) event.extras.getSerializable(NEW_LIST);
        int startSize = list.size();
        list.addAll(listAdded);
        adapter.notifyItemRangeInserted(startSize, listAdded.size());
    }

    private void handleLoadingCallBack(AppEvent event) {
        boolean isLoading = event.event == EventDef.CALLBACK_EVENTS.LOADING;
        if (isLoading) {
            loadingLayout.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    public ImageDetailFragment() {
        // Required empty public constructor
    }

    public static ImageDetailFragment newInstance(Bundle bundle) {
        ImageDetailFragment fragment = new ImageDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        init(view);
        set();
        return view;
    }

    private void init(View view) {
        appEventReceiver = new AppEventReceiver(this, AppEventReceiver.FILTERS);
        eventBus().register(appEventReceiver);
        list = (ArrayList<GeneralModel>) bundle.getSerializable(LIST);
        pos = bundle.getInt(POS, 0);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        backBtn = view.findViewById(R.id.backBtn);
        backImg = view.findViewById(R.id.backImg);
        recycleView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        recycleView.setItemViewCacheSize(3);
        adapter = new SingleImageScrollableAdapter(context, list, bundle);
    }

    public static int getAlphaFor(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        return (int) ((alpha * 100) / 255);
    }

    private void set() {
//        backBtn.getBackground().setAlpha(getAlphaFor(0.5f));
        backBtn.setOnClickListener(v -> {
            ((Activity) context).onBackPressed();
//            getParentFragmentManager().popBackStack();
        });
        layoutManager.scrollToPosition(pos);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(adapter);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrollCallback != null) {
                    scrollCallback.scrolledTo(layoutManager.findFirstVisibleItemPosition());
                }
            }
        });
//        RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
//        Glide.with(this)
//                .asBitmap()
//                .load(bundle.getString(LOADED_URL))
//                .apply(requestOptions)
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        scheduleStartPostponedTransition(imageView);
//                        scheduleStartPostponedTransition(cardView);
//                        return false;
//                    }
//                })
//                .into(imageView);
    }

    public void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    public static void setScrollCallback(ScrollCallback scrollCallback) {
        ImageDetailFragment.scrollCallback = scrollCallback;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus().unregister(appEventReceiver);
        scrollCallback = null;
    }
}