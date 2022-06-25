package com.java.proj.view.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.GetDataCallBack;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.CallBacks.ScrollCallback;
import com.java.proj.view.CallBacks.SuccessCallBack;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.ProfileModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.api.ApiUtilities;
import com.java.proj.view.databinding.FragmentProfileBinding;

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
    private ImageView selfImg;
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
        public void onSuccess() {
            getData();
        }

        @Override
        public void onFailure() {
            Log.d(TAG, "onFailure (successCallBack): ");
            binding.setMoreFromLoading(false);
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    };

    private void getData() {
        Log.d(TAG, "getData: ");
        binding.setMoreFromLoading(true);
        final int[] initListSize = {list.size()};
        ApiUtilities.getListImageModel(context,
                ApiUtilities.getApiInterface().getUserImages(binding.getUserName(), page, pageSize),
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
        list = new ArrayList<>();
        recyclerView = binding.recyclerView;
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
        selfImg = binding.selfImg;
        binding.emailTxt.setSelected(true);
    }

    @Override
    public void onResume() {
        super.onResume();
//        eventBus(context).register(appEventReceiver);
        Log.d(TAG, "onResume: Prfile Fragment");
        boolean isCurrentUser = bundle.getBoolean(IS_CURRENT_USER, false);
        binding.setErrorFlag(true);
        if (isCurrentUser) {
            Log.d(TAG, "isCurrentUser: true ");
            binding.logOutBtn.setOnClickListener(v -> {
                getAppController(context).setAccessToken(null);
                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).getSupportFragmentManager().popBackStack();
            });

            if (getAppController(context).getAccessToken() != null) {
                Log.d(TAG, "access token: " + getAppController(context).getAccessToken());
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
                Toast.makeText(context, "Log in first", Toast.LENGTH_SHORT).show();
                if (!askedOnce) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            ApiUtilities.OAUTH_URL +
                                    "?client_id=" + ApiUtilities.API_KEY +
                                    "&redirect_uri=" + ApiUtilities.REDIRECT_URI2 +
                                    "&response_type=code" +
                                    "&scope=" + ApiUtilities.SCOPE));
                    startActivity(intent);
                    askedOnce = true;
                }
            }
        } else {
            String user = bundle.getString(USER);
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
            binding.setDownloads(profileModel.getDownloads());
            binding.setEmail(profileModel.getEmail());
            binding.setUserName(profileModel.getUsername());
            binding.setName(profileModel.getName());
            binding.setId(profileModel.getId());
            binding.setBio(profileModel.getBio());
//                    binding.setProfileVar(profileModel);
            RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(profileModel.getProfileImageModel().getLarge())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            selfImg.setPadding(0, 0, 0, 0);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(selfImg);

            if (successCallBack != null) {
                successCallBack.onSuccess();
            }
        } else {
            if (successCallBack != null) {
                successCallBack.onFailure();
            }
            binding.setErrorFlag(true);
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "err msg: " + response);
        }
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
    public void onDownload(int position) {

    }

    @Override
    public void onLike(int position) {
        AppEvent appEvent = new AppEvent(EventDef.Category.LIKE_UNLIKE, EventDef.LIKE_UNLIKE_EVENTS.LIKED, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalAppController.GENERAL_MODEL, list.get(position));
        appEvent.extras = bundle;
        eventBus(context).post(appEvent);
    }

    @Override
    public void onUnlike(int position) {
        AppEvent appEvent = new AppEvent(EventDef.Category.LIKE_UNLIKE, EventDef.LIKE_UNLIKE_EVENTS.UNLIKED, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GlobalAppController.GENERAL_MODEL, list.get(position));
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