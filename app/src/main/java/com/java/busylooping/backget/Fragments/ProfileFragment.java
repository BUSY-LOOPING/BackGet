package com.java.busylooping.backget.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.CallBacks.GetDataCallBack;
import com.java.busylooping.backget.CallBacks.RecyclerViewListener;
import com.java.busylooping.backget.CallBacks.ScrollCallback;
import com.java.busylooping.backget.CallBacks.SuccessCallBack;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.busylooping.backget.api.ApiUtilities;
import com.java.busylooping.backget.databinding.FragmentProfileBinding;
import com.java.busylooping.backget.models.GeneralAppModel;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.ProfileModel;
import com.java.busylooping.backget.utils.AppEvent;
import com.java.busylooping.backget.utils.AppEventBus;
import com.java.busylooping.backget.utils.EventDef;
import com.java.busylooping.backget.utils.GlobalAppController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends AppBaseFragment
        implements RecyclerViewListener, ScrollCallback {
    private static final String TAG = "MyProfileFragment";

    private Bundle bundle;
    private Context context;
    private FragmentProfileBinding binding;
    private ImageView selfImg, twitterIcon, instagramIcon;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private GridLayoutManager layoutManager;
    private ArrayList<GeneralModel> list;

    public static final String USER = "ProfileFragmentUser";
    public static final String IS_CURRENT_USER = "ProfileFragment_IsCurrentUser";

    private boolean isLoading = false, isLastPage = false;
    int page = 0, pageSize = 20;

    private AppEventReceiver appEventReceiver;
    private boolean askedOnce = false;
    private Handler handler = new Handler(Looper.getMainLooper());


    public static class AppEventReceiver extends AppEventBus.Receiver<ProfileFragment> {
        public static final int[] FILTERS = {
                EventDef.CALLBACK_EVENTS.SCROLL_CALLBACK
        };

        public AppEventReceiver(ProfileFragment holder, int[] categoryFilter) {
            super(holder, categoryFilter);
        }

        @Override
        protected void onReceiveAppEvent(ProfileFragment holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        }
    }

    public void onReceiveAppEvent(@NonNull AppEvent appEvent) {
        switch (appEvent.category) {
            case EventDef.CALLBACK_EVENTS.SCROLL_CALLBACK:
                handleScrollCallBack(appEvent);
                break;
        }
    }

    private void handleScrollCallBack(AppEvent appEvent) {
        if (layoutManager != null)
            layoutManager.scrollToPosition(appEvent.event);
    }

    private final SuccessCallBack successCallBack = new SuccessCallBack() {
        @Override
        public void onSuccess(@Nullable Object obj) {
            getData();
        }

        @Override
        public void onFailure() {
            Log.d(TAG, "onFailure (successCallBack): ");
            binding.setMoreFromLoading(false);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void getData() {
        Log.d(TAG, "getData: ");
        binding.setMoreFromLoading(true);
        final int[] initListSize = {list.size()};
        ApiUtilities.getListImageModel(context,
                ApiUtilities.getApiInterface().getUserImages(binding.getProfileModel().getUsername(), page, pageSize),
                list,
                adapter,
                pageSize,
                getAppController(context).getLikesModel(),
                new GetDataCallBack() {
                    @Override
                    public void isLoading(boolean isLoading) {
                        AppEvent appEvent1;
                        ProfileFragment.this.isLoading = isLoading;
                        if (isLoading) {
                            appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.LOADING, 0, 0);
//                            loadingLayout.setVisibility(View.VISIBLE);
                        } else {
                            binding.setMoreFromLoading(false);
                            appEvent1 = new AppEvent(EventDef.CALLBACK_EVENTS.LOADING_CALLBACK, EventDef.CALLBACK_EVENTS.NOT_LOADING, 0, 0);
//                            loadingLayout.setVisibility(View.GONE);
                        }
                        AppEvent appEvent2 = new AppEvent(EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, EventDef.CALLBACK_EVENTS.LIST_REFRESH_CALLBACK, initListSize[0], list.size() - initListSize[0]);
                        eventBus(context).post(appEvent2);
                        eventBus(context).post(appEvent1);
                        initListSize[0] = list.size();
                    }

                    @Override
                    public void isLastPage(boolean isLastPage) {
                        ProfileFragment.this.isLastPage = isLastPage;
                    }
                });
    }

    private final Observer<String> accessTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s != null) {
                Log.d(TAG, "onChanged: here");
                checkAndFetch();
            }
        }
    };



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static ProfileFragment newInstance(Bundle bundle) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        if (container != null)
//            container.removeAllViews();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        selfImg = binding.selfImg;
        twitterIcon = binding.twitterIcon;
        instagramIcon = binding.instagramIcon;
        recyclerView = binding.recyclerView;


        list = new ArrayList<>();
        GeneralAppModel generalAppModel = new ViewModelProvider((ViewModelStoreOwner)context).get(GeneralAppModel.class);
        generalAppModel.getAccessTokenLiveData().observe((LifecycleOwner) context, accessTokenObserver);
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(context, list, this, R.layout.recycler_view_item);
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

//                int[] firstVisibleItems = null;
//                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
//                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
//                    int firstVisibleItemPos = firstVisibleItems[0];
//                    if (!isLoading && !isLastPage) {
//                        if ((visibleItems + firstVisibleItemPos >= totalItems) && firstVisibleItemPos >= 0 && totalItems >= pageSize) {
//                            page++;
//                            getData();
//                        }
//                    }
//                }

            }
        });
        binding.setLoading(true);
        binding.emailTxt.setSelected(true);

        twitterIcon.setOnClickListener(v -> {
            ProfileModel.launchTwitterForUserName(context, binding.getProfileModel().getUserSocialsModel().getTwitter_username());
        });

        instagramIcon.setOnClickListener(v -> {
            ProfileModel.launchInstagramForUserName(context, binding.getProfileModel().getUserSocialsModel().getInstagram_username());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        eventBus(context).register(appEventReceiver);
        Log.d(TAG, "onResume: Prfile Fragment");
        checkAndFetch();
    }

    private void checkAndFetch() {
        boolean isCurrentUser = bundle.getBoolean(IS_CURRENT_USER, false);
        binding.setErrorFlag(true);
        if (isCurrentUser) {
            binding.logOutBtn.setOnClickListener(v -> {
                getAppController(context).setAccessToken(null);
                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).getSupportFragmentManager().popBackStack();
            });

            if (getAppController(context).getAccessToken() != null) {

                ApiUtilities.getApiInterface(getAppController(context).getAccessToken()).getCurrentUser().enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                        onSuccess(call, response, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                        onFail(call, t);
                    }
                });
            }
            else {
                if (!askedOnce) {
                    Toast.makeText(context, "Log in first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            ApiUtilities.OAUTH_URL +
                                    "?client_id=" + ApiUtilities.API_KEY +
                                    "&redirect_uri=" + ApiUtilities.REDIRECT_URI2 +
                                    "&response_type=code" +
                                    "&scope=" + ApiUtilities.SCOPE));
                    context.startActivity(intent);
                    askedOnce = true;
                }
            }
        } else {
            String user = bundle.getString(USER);
            ProfileModel profileModel = (ProfileModel) bundle.getSerializable(GlobalAppController.PROFILE_MODEL);
            if (profileModel != null) {
                setProfileModel(profileModel);
            }
            ApiUtilities.getApiInterface().getUser(user).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                    onSuccess(call, response, successCallBack);
                }

                @Override
                public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                    onFail(call, t);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        eventBus(context).unregister(appEventReceiver);
    }

    private void onSuccess(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response, @Nullable SuccessCallBack successCallBack) {
        binding.setLoading(false);
        binding.setErrorFlag(false);
        ProfileModel profileModel = response.body();
        if (profileModel != null) {
            setProfileModel(profileModel);

            if (successCallBack != null) {
                successCallBack.onSuccess(null);
            }
        } else {
            if (successCallBack != null) {
                successCallBack.onFailure();
            }
            binding.setErrorFlag(true);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "err msg: " + response);
        }
    }

    private void setProfileModel(@NonNull ProfileModel profileModel) {
        binding.setProfileModel(profileModel);
        RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(profileModel.getUserProfileImageModel().getLarge())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        selfImg.setBackground(null);
                        selfImg.setPadding(0, 0, 0, 0);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(selfImg);
    }

    private void onFail(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
        binding.setLoading(false);
        binding.setErrorFlag(true);
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position, View container, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(ImageDetailFragment.POS, position);
        Fragment fragment = ImageDetailFragment.newInstance(bundle);
//        ImageDetailFragment.setScrollCallback(this);
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

    @Override
    public void scrolledTo(int pos) {
        layoutManager.scrollToPosition(pos);
    }


    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}