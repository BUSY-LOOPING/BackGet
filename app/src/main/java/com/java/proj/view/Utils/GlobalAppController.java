package com.java.proj.view.Utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.java.proj.view.Activities.ImageDetailActivity;
import com.java.proj.view.R;

import java.util.ArrayList;

public class GlobalAppController {
    private final Context context;
    private final AppEventBus appEventBus;
    private final AppEventReceiver appEventReceiver;
    private Service holderService;


    public GlobalAppController(Context context, Bundle savedInstanceState) {
        this.context = context;

        appEventBus = new AppEventBus(context);
        appEventReceiver = new AppEventReceiver(this);
        appEventBus.register(appEventReceiver);
        init(context);
    }


    public void postToBus(AppEvent appEvent) {
        if (appEvent != null) {
            appEventBus.post(appEvent);
        }
    }

    void init(Context context) {

    }


    public static class AppEventReceiver extends AppEventBus.Receiver<GlobalAppController> {
        private static final int[] FILTER = new int[]{

        };

        public AppEventReceiver(GlobalAppController holder) {
            super(holder, FILTER);
        }

        @Override
        protected void onReceiveAppEvent(GlobalAppController holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        } //3


    }

    void onReceiveAppEvent(AppEvent event) {

        switch (event.category) {
            case 0:
                break;
            case 1:
                handleClick(event);
                break;
            case 2:
                break;
        }

    }

    private void handleClick(AppEvent event) {
    }

    public static void switchFragment(int id, Fragment fragment, FragmentManager fragmentManager, @Size(4) @Nullable int[] anim) {
        if (!fragmentManager.isDestroyed()) {
            FragmentTransaction fragmentTransaction =
                    fragmentManager
                            .beginTransaction();

            if (anim != null) {
                fragmentTransaction.setCustomAnimations(anim[0], anim[1], anim[2], anim[3]);
            }

            fragmentTransaction.addToBackStack(fragment.getTag())
                    .add(id, fragment)
                    .commit();
        }
    }

    public static void switchContent(@NonNull Context context, Class<?> c, @NonNull Bundle bundle, @NonNull View container, @NonNull View view) {
        String transitionNameImgContainer = context.getString(R.string.target_img_transition_parent);
        String transitionNameImg = context.getString(R.string.target_img_transition);
        Activity activity = (Activity) context;

        Pair<View, String> pair1 = Pair.create(container, transitionNameImgContainer);
        Pair<View, String> pair2 = Pair.create(view, transitionNameImg);
//        activity.supportStartPostponedEnterTransition();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2);
        Intent intent = new Intent(context, c);
        intent.putExtra(ImageDetailActivity.BUNDLE_VAL, bundle);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public void bindHolderService(Service service) {
        holderService = service;
    }

    public Context getContext() {
        return context;
    }

    public AppEventBus getAppEventBus() {
        return appEventBus;
    }

    public static void switchFragment(int id, Fragment fragment, Fragment currentFragment, FragmentManager fragmentManager, @Nullable ArrayList<Pair<String, Object>> list) {
        if (!fragmentManager.isDestroyed()) {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .addToBackStack("transaction")
                    .add(id, fragment, fragment.getTag())
                    .setReorderingAllowed(true);

            if (list != null) {
                for (Pair<String, Object> pair : list) {
                    fragmentTransaction = fragmentTransaction.addSharedElement((View) pair.second, pair.first);
                }
            }
            fragmentTransaction.commit();
        }
    }

    public static void postEventToBus(AppEventBus eventBus, AppEvent event) {
        if (eventBus != null && event != null) {
            eventBus.post(event);
        }
    }


}
