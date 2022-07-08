package com.java.busylooping.backget.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.java.busylooping.backget.Activities.ImageDetailActivity;
import com.java.busylooping.backget.AppBaseFragment;
import com.java.busylooping.backget.CallBacks.ScrollCallback;
import com.java.busylooping.backget.DataBase.LikedDataBase;
import com.java.busylooping.backget.MainActivity;
import com.java.busylooping.backget.Popup.UtilsPopup;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.api.ApiUtilities;
import com.java.busylooping.backget.models.GeneralAppModel;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikeUnlikeModel;
import com.java.busylooping.backget.models.LikesModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalAppController {
    private final Context context;
    private final AppEventBus appEventBus;
    private final AppEventReceiver appEventReceiver;
    private GlobalAppControllerService holderService;
    private LikesModel likesModel;
    private GeneralAppModel generalAppModel;

    public static final String ACCESS_TOKEN = "AccessToken";
    public static final String GENERAL_MODEL = "GeneralModel";
    public static final String PROFILE_MODEL = "ProfileModel";
    public static final String GENERAL_MODEL_ID = "GeneralModelId";
    public static final String FROM_NOTIFICATION = "FromNotification";
    public static final String NOTIFICATION_ACTION = "NotificationAction";


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
        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
        generalAppModel = new ViewModelProvider((ViewModelStoreOwner) context).get(GeneralAppModel.class);
    }


    public static class AppEventReceiver extends AppEventBus.Receiver<GlobalAppController> {
        private static final int[] FILTER = new int[]{
                EventDef.Category.LIKE_UNLIKE,
                EventDef.Category.VIEW_DIALOG
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
            case EventDef.Category.LIKE_UNLIKE:
                handleLikeUnlikeEvents(event);
                break;
            case EventDef.Category.VIEW_DIALOG:
                handleViewdialogEvents(event);
                break;
        }

    }

    private void handleViewdialogEvents(AppEvent event) {
        switch (event.event) {
            case EventDef.VIEW_DIALOG.SHOW_UTILS_DIALOG:
                handleShowUtilEvent(event);
                break;
        }
    }

    private void handleLikeUnlikeEvents(@NonNull AppEvent appEvent) {
        switch (appEvent.event) {
            case EventDef.LIKE_UNLIKE_EVENTS.LIKED:
                handleLikeEvent(appEvent);
                break;
            case EventDef.LIKE_UNLIKE_EVENTS.UNLIKED:
                handleUnlikeEvent(appEvent);
                break;
        }
    }

    private void handleLikeEvent(@NonNull AppEvent appEvent) {
        GeneralModel model = new GeneralModel((GeneralModel) appEvent.extras.getSerializable(GENERAL_MODEL));
        model.setLiked(true);
        likesModel.addModel(model, true);


        LikedDataBase db = LikedDataBase.getInstance(context);
        db.insertOrUpdatePicture(model);
        db.close();


        String accessToken = getAccessToken();
        ApiUtilities.getApiInterface(accessToken).likePicture(model.getImageId()).enqueue(new Callback<LikeUnlikeModel>() {
            @Override
            public void onResponse(@NonNull Call<LikeUnlikeModel> call, @NonNull Response<LikeUnlikeModel> response) {
                if (response.body() != null) {

                } else {
                    Log.d("mylog", response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LikeUnlikeModel> call, @NonNull Throwable t) {
                Log.e("myerr", t.getLocalizedMessage());
            }
        });
    }

    private void handleUnlikeEvent(@NonNull AppEvent appEvent) {
        GeneralModel model = new GeneralModel((GeneralModel) appEvent.extras.getSerializable(GENERAL_MODEL));
        model.setLiked(false);
        likesModel.removeModel(model, true);

        LikedDataBase db = LikedDataBase.getInstance(context);
        db.unlikePicture(model);
        db.close();

        String accessToken = getAccessToken();
        ApiUtilities.getApiInterface(accessToken).unLikePicture(model.getImageId()).enqueue(new Callback<LikeUnlikeModel>() {
            @Override
            public void onResponse(@NonNull Call<LikeUnlikeModel> call, @NonNull Response<LikeUnlikeModel> response) {
                if (response.body() != null) {
                }
            }

            @Override
            public void onFailure(@NonNull Call<LikeUnlikeModel> call, @NonNull Throwable t) {
                Log.e("myerr", t.getLocalizedMessage());
            }
        });

    }

    private void handleShowUtilEvent(@NonNull AppEvent event) {
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        if (!((MainActivity) context).isFinishing() && !fragmentManager.isDestroyed()) {
            UtilsPopup utilsPopup = UtilsPopup.newInstance(event.extras);
            utilsPopup.show(fragmentManager, "UtilsPopup");
        }
    }


    public static void switchFragment(int id, Fragment fragment, FragmentManager fragmentManager, @Size(4) @Nullable int[] anim, @Nullable String tag) {
        if (!fragmentManager.isDestroyed()) {
            FragmentTransaction fragmentTransaction =
                    fragmentManager
                            .beginTransaction();

            if (anim != null) {
                fragmentTransaction.setCustomAnimations(anim[0], anim[1], anim[2], anim[3]);
            }


            fragmentTransaction.addToBackStack(tag == null ? fragment.toString() : tag)
                    .add(id, fragment, tag)
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

    public void bindHolderService(GlobalAppControllerService service) {
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

    public static void launchImageDetailFragment(@NonNull Fragment currFragment, @NonNull ScrollCallback scrollCallback, @NonNull FragmentManager fragmentManager, @NonNull ArrayList<GeneralModel> list) {
//        Fragment fragment = ImageDetailFragment.newInstance(event.extras);
//        ImageDetailFragment.setScrollCallback(scrollCallback);
//        fragment.setEnterTransition(new Fade());
//        currFragment.setExitTransition(new Fade());
//        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager, null);
    }


    @Nullable
    public String getAccessToken() {
        if (generalAppModel.getAccessTokenLiveData().getValue() == null) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread())
                generalAppModel.getAccessTokenLiveData().setValue(context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ApiUtilities.ACCESS_TOKEN, null));
            else
                generalAppModel.getAccessTokenLiveData().postValue(context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ApiUtilities.ACCESS_TOKEN, null));

        }
        return generalAppModel.getAccessTokenLiveData().getValue();
    }

    public void setAccessToken(@Nullable String accessToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(ApiUtilities.ACCESS_TOKEN, accessToken);
        editor.apply();
        generalAppModel.getAccessTokenLiveData().setValue(accessToken);
    }

    public LikesModel getLikesModel() {
        return likesModel;
    }

    public static AppBaseFragment getCallerFragment(@NonNull FragmentManager fragmentManager) {
        int count = fragmentManager.getBackStackEntryCount();
        String tag = fragmentManager.getBackStackEntryAt(count - 2).getName();
        return (AppBaseFragment) fragmentManager.findFragmentByTag(tag);
    }
}
