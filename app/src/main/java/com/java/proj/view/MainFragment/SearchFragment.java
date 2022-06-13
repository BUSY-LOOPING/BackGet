package com.java.proj.view.MainFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.proj.view.AppBaseFragment;
import com.java.proj.view.CallBacks.GetDataCallBack;
import com.java.proj.view.CallBacks.RecyclerViewListener;
import com.java.proj.view.CallBacks.ScrollCallback;
import com.java.proj.view.DataBase.LikedDataBase;
import com.java.proj.view.Fragments.ImageDetailFragment;
import com.java.proj.view.MainActivity;
import com.java.proj.view.Models.GeneralModel;
import com.java.proj.view.Models.LikeUnlikeModel;
import com.java.proj.view.Models.LikesModel;
import com.java.proj.view.R;
import com.java.proj.view.RecyclerViewAdapters.RecyclerViewAdapter;
import com.java.proj.view.Utils.AppEvent;
import com.java.proj.view.Utils.AppEventBus;
import com.java.proj.view.Utils.EventDef;
import com.java.proj.view.Utils.GlobalAppController;
import com.java.proj.view.api.ApiUtilities;
import com.java.proj.view.databinding.FragmentSearchBinding;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends AppBaseFragment
        implements TextView.OnEditorActionListener, RecyclerViewListener, ScrollCallback {
    private Bundle bundle;
    private Context context;
    private FragmentSearchBinding binding;

    private EditText searchBox;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private boolean isLoading = false, isLastPage = false;
    private int page = 1, pageSize = 20;
    private TextView centerTxt;
    private LikesModel likesModel;
    private FragmentManager fragmentManager;
    private boolean registered = false;

    private ArrayList<GeneralModel> list;
    private SearchFragment.AppEventReceiver appEventReceiver;



    public static class AppEventReceiver extends AppEventBus.Receiver<SearchFragment> {
        public static final int[] FILTERS = {
                EventDef.Category.IMAGE_CLICK,
                EventDef.Category.LIKE_UNLIKE
        };

        public AppEventReceiver(SearchFragment holder, int[] categoryFilter) {
            super(holder, categoryFilter);
        }

        @Override
        protected void onReceiveAppEvent(SearchFragment holder, AppEvent event) {
            holder.onReceiveAppEvent(event);
        }
    }

    private void onReceiveAppEvent(AppEvent appEvent) {
        switch (appEvent.category) {
            case EventDef.Category.IMAGE_CLICK:
                handleImageClick(appEvent);
                break;
            case EventDef.Category.LIKE_UNLIKE:
                handleLikeUnlikeClick(appEvent);
                break;
        }
    }

    private void handleLikeUnlikeClick(AppEvent appEvent) {
        int index= appEvent.extras.getInt(ImageDetailFragment.LIKE_UNLIKE_POS);
        String id = appEvent.extras.getString(ImageDetailFragment.LIKE_UNLIKE_POS);
        if (list.get(index).getImageId().equals(id)) {
            switch (appEvent.event) {
                case EventDef.LIKE_UNLIKE_EVENTS.LIKED:
                    this.onLike(index);
                    break;
                case EventDef.LIKE_UNLIKE_EVENTS.UNLIKED:
                    this.onUnlike(index);
                    break;
            }
        }
    }

    private void handleImageClick(AppEvent event) {
        Fragment fragment = ImageDetailFragment.newInstance(event.extras);
        ImageDetailFragment.setScrollCallback(this);
//        fragment.setSharedElementEnterTransition(new CustomTransition());
        fragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
//        fragment.setSharedElementReturnTransition(new CustomTransition());
//        postponeEnterTransition();
        GlobalAppController.switchFragment(R.id.container, fragment, fragmentManager, null, "RecentFragment");
//        GlobalAppController.switchContent(context, ImageDetailActivity.class, event.extras, (View) event.weakReferenceList.get().get(0).second, (View) event.weakReferenceList.get().get(1).second);
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,false);
        View view = binding.getRoot();
        init();
        setAdapter();
        listeners();
        return view;
    }

    private void listeners() {
        searchBox.setOnEditorActionListener(this);
    }

    public void bind(int id, Object obj) {
        binding.setVariable(id, obj);
        binding.executePendingBindings();
    }

    private void init() {
        appEventReceiver = new SearchFragment.AppEventReceiver(this, SearchFragment.AppEventReceiver.FILTERS);
        bind(BR.isLoading, isLoading);
        list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(context, list, this, R.layout.recycler_view_item);
        likesModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LikesModel.class);
        searchBox = binding.searchView;
        centerTxt = binding.centerString;
        recyclerView = binding.recyclerView;
        fragmentManager = ((MainActivity)context).getSupportFragmentManager();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            textView.clearFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

            performSearch();
            return true;
        }
        return false;
    }

    private void performSearch() {
        int size = list.size();
        list.clear();
        adapter.notifyItemRangeRemoved(0, size);
        getData();
    }

    private void setAdapter() {
        layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
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
        ApiUtilities.getSearchData(context, page, pageSize, searchBox.getText().toString().trim(), "relevant", adapter, list,likesModel , new GetDataCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void isLoading(boolean isLoading) {
                SearchFragment.this.isLoading = isLoading;
                binding.setIsLoading(isLoading);
                if (!isLoading) {
                    if (list.size() == 0) {
                        centerTxt.setText("No result found");
                        centerTxt.setVisibility(View.VISIBLE);
                    } else {
                        centerTxt.setVisibility(View.INVISIBLE);
                    }
                } else {
                    centerTxt.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void isLastPage(boolean isLastPage) {
                SearchFragment.this.isLastPage = isLastPage;
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
    }

    @Override
    public void onDownload(int position) {

    }

    @Override
    public void onLike(int position) {
        String accessToken = context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ApiUtilities.ACCESS_TOKEN, "");
        LikedDataBase db = LikedDataBase.getInstance(context);
        db.likePicture(list.get(position));
        db.close();
        likesModel.addModel(list.get(position), true);
        ApiUtilities.getApiInterface(accessToken).likePicture(list.get(position).getImageId()).enqueue(new Callback<LikeUnlikeModel>() {
            @Override
            public void onResponse(@NonNull Call<LikeUnlikeModel> call, @NonNull Response<LikeUnlikeModel> response) {
                if (response.body() != null) {

                } else{
                    Log.d("mylog", response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LikeUnlikeModel> call, @NonNull Throwable t) {
                Log.e("myerr", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onUnlike(int position) {
        LikedDataBase db = LikedDataBase.getInstance(context);
        db.unlikePicture(list.get(position));
        db.close();
        likesModel.removeModel(list.get(position), true);
        String accessToken = context.getSharedPreferences(ApiUtilities.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ApiUtilities.ACCESS_TOKEN, "");
        ApiUtilities.getApiInterface(accessToken).unLikePicture(list.get(position).getImageId()).enqueue(new Callback<LikeUnlikeModel>() {
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

    @Override
    public void onResumeInViewPager() {
        super.onResumeInViewPager();
        if (!registered) {
            eventBus(context).register(appEventReceiver);
            registered = true;
        }
    }

    @Override
    public void onPauseInViewPager() {
        super.onPauseInViewPager();
        eventBus(context).unregister(appEventReceiver);
        registered = false;
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
}