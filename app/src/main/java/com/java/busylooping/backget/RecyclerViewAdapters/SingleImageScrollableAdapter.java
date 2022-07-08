package com.java.busylooping.backget.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.busy.looping.like_view_library.LikeView;
import com.busy.looping.like_view_library.OnLikeChangeListener;
import com.google.android.material.card.MaterialCardView;
import com.java.busylooping.backget.AsyncTasks.SearchInList;
import com.java.busylooping.backget.BR;
import com.java.busylooping.backget.BaseViewHolder;
import com.java.busylooping.backget.CallBacks.AsyncResponse;
import com.java.busylooping.backget.CallBacks.SingleImageRecyclerViewListener;
import com.java.busylooping.backget.CustomRecyclerView.SingleImageRecycleView;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.databinding.ImageDetailRecyclerviewItemBinding;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikesModel;
import com.ortiz.touchview.OnTouchCoordinatesListener;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;

public class SingleImageScrollableAdapter extends RecyclerView.Adapter<SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder> {
    private final Context mContext;
    private ArrayList<GeneralModel> list;
    private final Bundle bundle;
    private final SingleImageRecyclerViewListener listener;
    private SingleImageRecycleView recyclerView;

    private final Observer<GeneralModel> likedObserver = new Observer<GeneralModel>() {
        @Override
        public void onChanged(GeneralModel model) {
            SearchInList<GeneralModel, Void, Void> searchInList = new SearchInList<>(new AsyncResponse<GeneralModel, Void, ArrayList<Integer>>() {
                @Override
                public void onProcessFinished(ArrayList<Integer> output) {
                    for (int i = 0; i < output.size(); i++) {
                        int index = output.get(i);
                        if (index > -1 && index < list.size()) {
//                list.get(index).setLiked(true);
//                list.get(index).setLikes(model.getLikes());
                            if (recyclerView != null && recyclerView.findViewHolderForAdapterPosition(index) != null) {
                                SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder viewHolder =
                                        (SingleImageScrollableAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                                if (viewHolder != null) {
                                    viewHolder.binding.setVariable(BR.generalModel, list.get(index));
//                                    if (!viewHolder.binding.getIsLiked()) {
//                                        viewHolder.binding.setIsLiked(true);
////                            viewHolder.binding.setLikes(model.getLikes());
//                                        viewHolder.binding.setLikes(list.get(index).getLikes());
//                                    }
                                }
                            }
                        }
//                        else if (index == -1) {
//                            if (recyclerView != null) {
//                                WrapContentLinearLayoutManager layoutManager = (WrapContentLinearLayoutManager) recyclerView.getLayoutManager();
//                                if (layoutManager != null) {
//                                    SingleImageScrollableAdapter.this.notifyItemRemoved(layoutManager.findLastCompletelyVisibleItemPosition());
//                                }
//                            }
//                        }
                    }
                }
            }, mContext, list);
            searchInList.execute(model);
//            int index = Iterables.indexOf(list,
//                    new Predicate<GeneralModel>() {
//                        @Override
//                        public boolean apply(GeneralModel input) {
//                            return input.getImageId().equals(model.getImageId());
//                        }
//                    });
//            if (index > -1) {
////                list.get(index).setLiked(true);
////                list.get(index).setLikes(model.getLikes());
//                if (recyclerView != null && recyclerView.findViewHolderForAdapterPosition(index) != null) {
//                    SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder viewHolder =
//                            (SingleImageScrollableAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
//                    if (viewHolder != null) {
//                        if (!viewHolder.binding.getIsLiked()) {
//                            viewHolder.binding.setIsLiked(true);
////                            viewHolder.binding.setLikes(model.getLikes());
//                            viewHolder.binding.setLikes(list.get(index).getLikes());
//                        }
//                    }
//                }
//            }
        }
    };

    private final Observer<GeneralModel> unLikedObserver = new Observer<GeneralModel>() {
        @Override
        public void onChanged(GeneralModel model) {
            SearchInList<GeneralModel, Void, Void> searchInList = new SearchInList<>(new AsyncResponse<GeneralModel, Void, ArrayList<Integer>>() {
                @Override
                public void onProcessFinished(ArrayList<Integer> output) {
                    for (int i = 0; i < output.size(); i++) {
                        int index = output.get(i);
                        if (index > -1 && index < list.size()) {
//                list.get(index).setLiked(true);
//                list.get(index).setLikes(model.getLikes());
                            if (recyclerView != null && recyclerView.findViewHolderForAdapterPosition(index) != null) {
                                SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder viewHolder =
                                        (SingleImageScrollableAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                                if (viewHolder != null) {
                                    viewHolder.binding.setVariable(BR.generalModel, list.get(index));
//                                    if (viewHolder.binding.getIsLiked()) {
//                                        viewHolder.binding.setIsLiked(false);
////                            viewHolder.binding.setLikes(model.getLikes());
//                                        viewHolder.binding.setLikes(list.get(index).getLikes());
//                                    }
                                }
                            }
                        } else if (index == -1) {
                        }
                    }
                }
            }, mContext, list);
            searchInList.execute(model);

//            int index = Iterables.indexOf(list,
//                    new Predicate<GeneralModel>() {
//                        @Override
//                        public boolean apply(GeneralModel input) {
//                            return input.getImageId().equals(model.getImageId());
//                        }
//                    });
//            if (index > -1) {
////                list.get(index).setLiked(false);
////                list.get(index).setLikes(model.getLikes());
//                if (recyclerView != null && recyclerView.findViewHolderForAdapterPosition(index) != null) {
//                    SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder viewHolder =
//                            (SingleImageScrollableAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
//                    if (viewHolder != null) {
//                        Log.d("mylog", "onChanged: model likes = " + model.getLikes() + "list likes = " + list.get(index).getLikes());
//                        if (viewHolder.binding.getIsLiked()) {
//                            viewHolder.binding.setIsLiked(false);
//                            viewHolder.binding.setLikes(list.get(index).getLikes());
////                            viewHolder.binding.setLikes(model.getLikes());
//                        }
//                    }
//                }
//            }
        }
    };

//    private final Observer<GeneralModel> likedObserver = new Observer<GeneralModel>() {
//        @Override
//        public void onChanged(GeneralModel model) {
//            int index = Iterables.indexOf(list,
//                    new Predicate<GeneralModel>() {
//                        @Override
//                        public boolean apply(GeneralModel input) {
//                            return input.getImageId().equals(model.getImageId());
//                        }
//                    });
//            if (index > -1) {
//                if (!list.get(index).isLiked()) {
//                    list.get(index).setLiked(true);
//                    list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) + 1) + "");
//                    Log.d("myrecycler", "name: " + model.getUserModel().getName() + " likes = " + model.getLikes());
//                    if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
//                        SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder viewHolder =
//                                (SingleImageScrollableAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
//                        if (viewHolder != null) {
//                            viewHolder.binding.setIsLiked(true);
//                            viewHolder.binding.setLikes(list.get(index).getLikes());
//                            Log.d("mylog", "onChanged: set likes");
//                        } else {
//                            Log.d("mylog", "onChanged: viewHolder is null");
//                        }
//                    } else {
//                        Log.d("mylog", "onChanged: findViewHolderForAdapterPosition is null");
//                    }
//                }
//            }
//        }
//    };
//
//    private final Observer<GeneralModel> unLikedObserver = new Observer<GeneralModel>() {
//        @Override
//        public void onChanged(GeneralModel model) {
//            int index = Iterables.indexOf(list,
//                    new Predicate<GeneralModel>() {
//                        @Override
//                        public boolean apply(GeneralModel input) {
//                            return input.getImageId().equals(model.getImageId());
//                        }
//                    });
//            if (index > -1) {
//                if (list.get(index).isLiked()) {
//                    list.get(index).setLiked(false);
////                list.get(index).setLikes(model.getLikes());
//                    list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) - 1) + "");
//                    if (recyclerView != null && recyclerView.findViewHolderForAdapterPosition(index) != null) {
//                        SingleImageScrollableAdapter.SingleImageScrollableAdapterViewHolder viewHolder =
//                                (SingleImageScrollableAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
//                        if (viewHolder != null) {
//                            viewHolder.binding.setIsLiked(false);
////                        viewHolder.binding.setLikes(model.getLikes());
//                            viewHolder.binding.setLikes(list.get(index).getLikes());
//                        }
//                    }
//                }
//            }
//        }
//    };

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = (SingleImageRecycleView) recyclerView;
    }

    public SingleImageScrollableAdapter(Context mContext, ArrayList<GeneralModel> generalModels, Bundle bundle, SingleImageRecyclerViewListener listener) {
        this.mContext = mContext;
        this.list = generalModels;
        this.bundle = bundle;
        this.listener = listener;

        Log.d("mylog", "SingleImageScrollableAdapter: ");
        LikesModel likesModel = new ViewModelProvider((ViewModelStoreOwner) mContext)
                .get(LikesModel.class);
//        likesModel.getLastLikedModel().removeObservers((LifecycleOwner) mContext);
//        likesModel.getLastUnLikedModel().removeObservers((LifecycleOwner) mContext);
        likesModel.getLastLikedModel().observe((LifecycleOwner) mContext, likedObserver);

//        likesModel.getLastLikedModel().observe(lifecycleOwner, likedObserver);
//        likesModel.getLastUnLikedModel().observe(lifecycleOwner, unLikedObserver);
        likesModel.getLastUnLikedModel().observe((LifecycleOwner) mContext, unLikedObserver);
    }

    @NonNull
    @Override
    public SingleImageScrollableAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.image_detail_recyclerview_item, parent, false);
        View view = viewDataBinding.getRoot();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.92);
        view.setLayoutParams(layoutParams);
        return new SingleImageScrollableAdapterViewHolder(viewDataBinding, listener);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SingleImageScrollableAdapterViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SingleImageScrollableAdapterViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.moreBtn.getVisibility() == View.GONE) {
            holder.moreBtn.setVisibility(View.VISIBLE);
            holder.descriptionViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageScrollableAdapterViewHolder holder, int position) {
//        holder.bind(BR.likes, list.get(position).getLikes());
//        holder.bind(BR.isLiked, list.get(position).isLiked());
//        holder.bind(BR.description, list.get(position).getDescription());
        holder.bind(BR.generalModel, list.get(position));
        String s = list.get(position).getProfileModel().getName() + " " + mContext.getString(R.string.by_unsplash);
        holder.binding.setByString(s);
        holder.imageView.setOnTouchCoordinatesListener(new OnTouchCoordinatesListener() {
            @Override
            public void onTouchCoordinate(@NonNull View view, @NonNull MotionEvent motionEvent, @NonNull PointF pointF) {
                if (motionEvent.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
                    recyclerView.enableVerticalScroll(false);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    recyclerView.enableVerticalScroll(true);
            }
        });
        RequestOptions requestOptions = new RequestOptions().dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(list.get(position).getUriModel().getRegular())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(holder.imageView);

        Glide.with(mContext)
                .load(list.get(position).getProfileModel().getUserProfileImageModel().getMedium())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(holder.userImg);
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public void detachObservers() {
        LikesModel likesModel = new ViewModelProvider((ViewModelStoreOwner) mContext)
                .get(LikesModel.class);
        likesModel.getLastLikedModel().removeObserver(likedObserver);
        likesModel.getLastUnLikedModel().removeObserver(unLikedObserver);
    }

    public static class SingleImageScrollableAdapterViewHolder extends BaseViewHolder
            implements View.OnClickListener, OnLikeChangeListener {
        private final TouchImageView imageView;
        private final LikeView likeView;
        //        private final TextView title, description, likeTxt, byTxt;
        private final ImageView moreBtn, userImg;
        private final View descriptionViewContainer;
        private final SingleImageRecyclerViewListener listener;
        private final MaterialCardView downloadBtn;
        private final LottieAnimationView downloadLottieView;
        private ImageDetailRecyclerviewItemBinding binding;

        public SingleImageScrollableAdapterViewHolder(@NonNull ViewDataBinding binding, SingleImageRecyclerViewListener listener) {
            super(binding);
            this.binding = (ImageDetailRecyclerviewItemBinding) binding;
            this.listener = listener;
            likeView = this.binding.likeView;
            imageView = this.binding.img;
            userImg = this.binding.userImg;
//            title = this.binding.titleTxt;
//            description = this.binding.descriptionTxt;
//            likeTxt = this.binding.likeTxt;
//            byTxt = this.binding.byTxt;
            moreBtn = this.binding.moreBtn;
            descriptionViewContainer = this.binding.nestedScrollableView;
            downloadBtn = this.binding.downloadBtn;
            downloadLottieView = this.binding.downloadLottieView;
            moreBtn.setOnClickListener(this);
            likeView.setOnClickListener(this);
            imageView.setOnClickListener(this);
            downloadBtn.setOnClickListener(this);
            likeView.setOnLikeChangeListener(this);
            userImg.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.moreBtn:
                    descriptionViewContainer.setVisibility(View.VISIBLE);
                    moreBtn.setVisibility(View.GONE);
                    break;
                case R.id.img:
                    if (moreBtn.getVisibility() == View.GONE) {
                        descriptionViewContainer.setVisibility(View.GONE);
                        moreBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.downloadBtn:
                    listener.onUtilsClick(getAdapterPosition());
                    break;
                case R.id.likeView:
                    likeView.onClick(likeView);
                    break;
                case R.id.userImg:
                    listener.onUserImgClick(getAdapterPosition());
                    break;
            }
        }

        @Override
        public void onLikeChange(boolean isLiked) {
            listener.onLikeClick(isLiked, getAdapterPosition());
        }
    }

}
