package com.java.proj.view.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.MainActivity;
import com.java.proj.view.R;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.api.ApiUtilities;
import com.java.proj.view.databinding.FragmentSettingsBinding;

import vn.luongvo.widget.iosswitchview.SwitchView;

public class SettingsFragment extends AppBaseFragment {
    private Bundle bundle;
    private FragmentSettingsBinding binding;
    private Context context;
    private SharedPreferences.Editor editor;

    public static final String SETTINGS_PREFERENCE = "SettingsSharedPreference";
    public static final String SYNC_FROM_UNSPLASH = "SyncImagesFromUnsplashAccount";

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(Bundle bundle) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container,false);
        init();
        listeners();
        return binding.getRoot();
    }

    private void listeners() {
        binding.switchView.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
//                binding.setSyncFromUnsplash(isChecked);
                editor.putBoolean(SYNC_FROM_UNSPLASH, isChecked);
                editor.commit();
            }
        });
        binding.profileBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(GlobalAppController.ACCESS_TOKEN, getAppController(context).getAccessToken());
            bundle.putBoolean(ProfileFragment.IS_CURRENT_USER, true);
            Fragment fragment = ProfileFragment.newInstance(bundle);
            GlobalAppController.switchFragment(R.id.settingsParent, fragment, ((MainActivity) context).getSupportFragmentManager(),
                    new int[]{R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_right, R.anim.slide_out_left}, "ProfileFragment");
        });
    }

    private void init() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        binding.setSyncFromUnsplash(sharedPreferences.getBoolean(SYNC_FROM_UNSPLASH, false));
        String versionCode = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.setBottomTxt(getApplicationName(context) + " v-" + versionCode);
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}