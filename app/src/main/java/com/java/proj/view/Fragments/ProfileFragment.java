package com.java.proj.view.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Api;
import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.GetDataCallBack;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.CallBacks.ScrollCallback;
import com.java.proj.view.CallBacks.SuccessCallBack;
import com.java.proj.view.CustomRecyclerView.GridAutofitLayoutManager;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.ImageModel;
import com.java.proj.view.Models.ProfileModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.proj.view.RecyclerViewAdapters.StaggerredRecyclerViewAdapter;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.api.ApiUtilities;
import com.java.proj.view.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends AppBaseFragment
        implements RecyclerViewListener, ScrollCallback {
    private Bundle bundle;
    private Context context;
    private FragmentProfileBinding binding;
    private ImageView selfImg;
    private RecyclerView recyclerView;
    private StaggerredRecyclerViewAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private ArrayList<GeneralModel> list;
    private String[] accessToken;

    public static final String USER = "ProfileFragmentUser";
    public static final String IS_CURRENT_USER = "ProfileFragment_IsCurrentUser";

    private final SuccessCallBack successCallBack = new SuccessCallBack() {
        @Override
        public void onSuccess() {
            ApiUtilities.getListImageModel(context,
                    ApiUtilities.getApiInterface().getUserImages(binding.getUserName()),
                    list,
                    adapter,
                    20,
                    null,
                    new GetDataCallBack() {
                        @Override
                        public void isLoading(boolean isLoading) {

                        }

                        @Override
                        public void isLastPage(boolean isLastPage) {

                        }
                    });
        }

        @Override
        public void onFailure() {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        list = new ArrayList<>();
        recyclerView = binding.recyclerView;
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StaggerredRecyclerViewAdapter(context, list, this);
        recyclerView.setAdapter(adapter);
        binding.setLoading(true);
        selfImg = binding.selfImg;
        binding.emailTxt.setSelected(true);
        accessToken = new String[]{bundle.getString(GlobalAppController.ACCESS_TOKEN, null)};
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isCurrentUser = bundle.getBoolean(IS_CURRENT_USER, false);
        binding.setErrorFlag(true);
        if (isCurrentUser) {
            binding.logOutBtn.setOnClickListener(v -> {
                SharedPreferences.Editor editor = context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(ApiUtilities.ACCESS_TOKEN, null);
                editor.apply();
                accessToken[0] = null;
                getAppController(context).setAccessToken(null);
                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).getSupportFragmentManager().popBackStack();
            });

            if (accessToken[0] != null)
                ApiUtilities.getApiInterface(accessToken[0]).getCurrentUser().enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                        onSuccess(call, response, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                        onFail(call, t);
                    }
                });
            else {
                Toast.makeText(context, "Log in first", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        ApiUtilities.OAUTH_URL +
                                "?client_id=" + ApiUtilities.API_KEY +
                                "&redirect_uri=" + ApiUtilities.REDIRECT_URI2 +
                                "&response_type=code" +
                                "&scope=" + ApiUtilities.SCOPE));
                startActivity(intent);
            }
        } else {
            String user = bundle.getString(USER);
            ApiUtilities.getApiInterface(accessToken[0]).getUser(user).enqueue(new Callback<ProfileModel>() {
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
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            Log.d("mylog", "err msg: " + response.toString());
        }
    }

    private void onFail(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
        binding.setLoading(false);
        binding.setErrorFlag(true);
        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position, View container, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(ImageDetailFragment.POS, position);
        Fragment fragment = ImageDetailFragment.newInstance(bundle);
        ImageDetailFragment.setScrollCallback(this);
        fragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        GlobalAppController.switchFragment(R.id.container, fragment, ((MainActivity)context).getSupportFragmentManager(), null, "ImageDetailFragment2");
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

    @Override
    public void scrolledTo(int pos) {
        layoutManager.scrollToPosition(pos);
    }

    @NonNull
    @Override
    public ArrayList<GeneralModel> getCurrentList() {
        Toast.makeText(context, "herer", Toast.LENGTH_SHORT).show();
        return list;
    }
}