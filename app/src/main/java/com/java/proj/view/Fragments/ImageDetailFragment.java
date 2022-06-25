package com.java.proj.view.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.ScrollCallback;
import com.java.proj.view.CallBacks.SingleImageRecyclerViewListener;
import com.java.proj.view.CustomRecyclerView.SingleImageRecycleView;
import com.java.proj.view.CustomRecyclerView.WrapContentLinearLayoutManager;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.LikesModel;
import com.java.proj.view.Popup.UtilsPopup;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.SingleImageScrollableAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;

import java.util.ArrayList;


public class ImageDetailFragment extends AppBaseFragment implements SingleImageRecyclerViewListener {
    public static final String BUNDLE_VAL = "ImageDetailActivityBundle";
    public static final String POS = "ImageDetailActivityImgCurrentPos";
    public static final String TAG = "ImageDetailFragment";

    public static final String LIKE_UNLIKE_POS = "ImageDetailFragmentLikedUnlikedPos";
    public static final String LIKE_UNLIKE_ID = "ImageDetailFragmentLikedUnlikedId";
    public static ScrollCallback scrollCallback;
//    private LikesModel likesModel;

    private Bundle bundle;
    private Context context;
    private ArrayList<GeneralModel> list;
    private int pos;
    private SingleImageRecyclerViewListener singleImageRecyclerViewListener;

    //views
    private SingleImageRecycleView recycleView;
    private MaterialCardView backBtn;
    private LinearLayout loadingLayout;

    private SingleImageScrollableAdapter adapter;
    private WrapContentLinearLayoutManager layoutManager;

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
                break;
        }
    }

    private void handleRefreshCallBack(AppEvent event) {
//        ArrayList<GeneralModel> listAdded = (ArrayList<GeneralModel>) event.extras.getSerializable(NEW_LIST);
//        int startSize = list.size();
//        list.addAll(listAdded);
        adapter.notifyItemRangeInserted(event.arg1, event.arg2);
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
        eventBus(context).register(appEventReceiver);
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
//        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
        appEventReceiver = new AppEventReceiver(this, AppEventReceiver.FILTERS);
        singleImageRecyclerViewListener = this;
//        list = (ArrayList<GeneralModel>) bundle.getSerializable(LIST);

        ParentFragment parentFragment = (ParentFragment) ((MainActivity)context).getSupportFragmentManager().findFragmentByTag("ParentFragment");
        if (parentFragment != null) {
            list = parentFragment.getCurrentList();
        }
        pos = bundle.getInt(POS, 0);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        backBtn = view.findViewById(R.id.backBtn);
        recycleView = view.findViewById(R.id.recyclerView);
        layoutManager = new WrapContentLinearLayoutManager(context);
        recycleView.setItemViewCacheSize(3);

        adapter = new SingleImageScrollableAdapter(context, list, bundle, singleImageRecyclerViewListener);
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
    public void onLikeClick(boolean isLiked, int pos) {
        AppEvent appEvent = new AppEvent(
                EventDef.Category.LIKE_UNLIKE,
                isLiked ? EventDef.LIKE_UNLIKE_EVENTS.LIKED : EventDef.LIKE_UNLIKE_EVENTS.UNLIKED,
                0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalAppController.GENERAL_MODEL, list.get(pos));
        appEvent.extras = bundle;
        eventBus(context).post(appEvent);


//        AppEvent appEvent = new AppEvent(EventDef.Category.LIKE_UNLIKE, isLiked ? EventDef.LIKE_UNLIKE_EVENTS.LIKED : EventDef.LIKE_UNLIKE_EVENTS.UNLIKED, 0, 0);
//        appEvent.extras.putInt(LIKE_UNLIKE_POS, pos);
//        appEvent.extras.putString(LIKE_UNLIKE_ID, list.get(pos).getImageId());
//        eventBus(context).post(appEvent);
//        AppEvent event;
//        if (isLiked)
//            event = new AppEvent(EventDef.Category.BTN, EventDef.BUTTON_EVENTS.HEART_LIKED, pos, 0);
//        else
//            event = new AppEvent(EventDef.Category.BTN, EventDef.BUTTON_EVENTS.HEART_UNLIKED, pos, 0);
//        eventBus(context).post(event);
    }

    @Override
    public void onDownloadClick(int pos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(UtilsPopup.GENERAL_MODEL, list.get(pos));
        bundle.putString(GlobalAppController.ACCESS_TOKEN , getAppController(context).getAccessToken());
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        showDownloadPopup(fragmentManager, bundle);
    }

    @Override
    public void onUserImgClick(int pos) {
//        ImageDetailFragment.scrollCallback = null;
        Bundle bundle = new Bundle();
        bundle.putString(GlobalAppController.ACCESS_TOKEN, getAppController(context).getAccessToken());
        bundle.putString(ProfileFragment.USER, list.get(pos).getUserModel().getUsername());
        Fragment fragment = ProfileFragment.newInstance(bundle);
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
//        FragmentManager fragmentManager = getParentFragmentManager();
        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager,
                new int[]{R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_left}, "ProfileFragment"+ fragmentManager.getBackStackEntryCount());
        onPause();
    }

    private void showDownloadPopup(FragmentManager fragmentManager, Bundle bundle) {
        UtilsPopup utilsPopup = UtilsPopup.newInstance(bundle);
        utilsPopup.show(fragmentManager, "UtilsPopup");
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus(context).unregister(appEventReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        adapter.detachObservers();
        Log.d(TAG, "onDestroy: ");
        scrollCallback = null;
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        return list;
    }
}