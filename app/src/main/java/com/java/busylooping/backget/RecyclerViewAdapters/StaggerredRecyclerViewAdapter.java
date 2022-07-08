package com.java.busylooping.backget.RecyclerViewAdapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.busy.looping.like_view_library.LikeView;
import com.busy.looping.like_view_library.OnLikeChangeListener;
import com.google.android.material.card.MaterialCardView;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.java.busylooping.backget.BaseViewHolder;
import com.java.busylooping.backget.CallBacks.RecyclerViewListener;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.databinding.RecyclerViewItem2Binding;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikesModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class StaggerredRecyclerViewAdapter extends RecyclerView.Adapter<StaggerredRecyclerViewAdapter.StaggerredRecyclerViewHolder> {
    private final Context mContext;
    private final ArrayList<GeneralModel> list;
    private final RecyclerViewListener listener;
    private RecyclerView recyclerView;
    private final int[] colors = new int[]{R.color.pink, R.color.yellow, R.color.orange, R.color.brown, R.color.purple};
    private final Random random;

    private final Observer<GeneralModel> likedObserver = new Observer<GeneralModel>() {
        @Override
        public void onChanged(GeneralModel model) {
            int index = Iterables.indexOf(list,
                    new Predicate<GeneralModel>() {
                        @Override
                        public boolean apply(GeneralModel input) {
                            return input.getImageId().equals(model.getImageId());
                        }
                    });
            if (index > -1) {
//                list.get(index).setLiked(true);
//                list.get(index).setLikes(model.getLikes());
                if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
                    StaggerredRecyclerViewAdapter.StaggerredRecyclerViewHolder viewHolder =
                            (StaggerredRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                    if (viewHolder != null) {
                        viewHolder.binding.setIsLiked(true);
                        viewHolder.binding.setLikes(model.getLikes());
                    }
                }
            }

        }
    };

    private final Observer<GeneralModel> unLikedObserver = new Observer<GeneralModel>() {
        @Override
        public void onChanged(GeneralModel model) {
            int index = Iterables.indexOf(list,
                    new Predicate<GeneralModel>() {
                        @Override
                        public boolean apply(GeneralModel input) {
                            return input.getImageId().equals(model.getImageId());
                        }
                    });
            if (index > -1) {
//                list.get(index).setLiked(false);
//                list.get(index).setLikes(model.getLikes());
                if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
                    StaggerredRecyclerViewAdapter.StaggerredRecyclerViewHolder viewHolder =
                            (StaggerredRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                    if (viewHolder != null) {
                        viewHolder.binding.setIsLiked(false);
                        viewHolder.binding.setLikes(model.getLikes());
                    }
                }
            }
        }
    };

    private final Observer<Map<String, GeneralModel>> observer = new Observer<Map<String, GeneralModel>>() {
        @Override
        public void onChanged(Map<String, GeneralModel> generalModels) {
            for (GeneralModel generalModel : generalModels.values()) {
                int index = Iterables.indexOf(list,
                        new Predicate<GeneralModel>() {
                            @Override
                            public boolean apply(GeneralModel input) {
                                return input.getImageId().equals(generalModel.getImageId());
                            }
                        });
                if (index > -1) {
                    list.get(index).setLikes(generalModel.getLikes());
                    list.get(index).setLiked(true);
                }
//                GeneralModel model = Iterables.tryFind(list,
//                        new Predicate<GeneralModel>() {
//                            @Override
//                            public boolean apply(GeneralModel input) {
//                                return input.getImageId().equals(generalModel.getImageId());
//                            }
//                        }).orNull();
//                if (model != null) {
//                    model.setLikes(generalModel.getLikes());
//                    model.setLiked(true);
//                }
            }
        }
    };

    public StaggerredRecyclerViewAdapter(Context mContext, ArrayList<GeneralModel> list, RecyclerViewListener listener) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listener;
        random = new Random();


//        new ViewModelProvider((ViewModelStoreOwner) mContext).get(LikesModel.class).
//                getCurrentLikedList().observe((LifecycleOwner) mContext, observer);

        LikesModel likesModel = new ViewModelProvider((ViewModelStoreOwner) mContext)
                .get(LikesModel.class);
        likesModel.getLastLikedModel().observe((LifecycleOwner) mContext, likedObserver);
        likesModel.getLastUnLikedModel().observe((LifecycleOwner) mContext, unLikedObserver);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public StaggerredRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ViewDataBinding view = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_item_2, parent, false);
        return new StaggerredRecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggerredRecyclerViewHolder holder, int position) {
        long start =  System.currentTimeMillis();
        holder.bind(BR.likes, list.get(position).getLikes());
        holder.bind(BR.isLiked, list.get(position).isLiked());
        if (list.get(position) != null) {
            RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(Uri.parse(list.get(position).getUriModel().getRegular()))
                    .placeholder(colors[random.nextInt(colors.length)])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView);
//            holder.likeNumber.setText(list.get(position).getLikes());
            ViewCompat.setTransitionName(holder.cardView, "_card_" + position);
            ViewCompat.setTransitionName(holder.imageView, "_image_" + position);
            long end = System.currentTimeMillis() - start;
            Log.d("mylog", "onBindViewHolder: pos = " + position + " time : " + end);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StaggerredRecyclerViewHolder extends BaseViewHolder
            implements View.OnClickListener, OnLikeChangeListener {
        private final ImageView imageView;
        private final MaterialCardView cardView;
        private final RecyclerViewListener listener;
        private final LikeView likeButton;
        private final RecyclerViewItem2Binding binding;


        public StaggerredRecyclerViewHolder(@NonNull ViewDataBinding binding, RecyclerViewListener listener) {
            super(binding);
            this.listener = listener;
            this.binding = (RecyclerViewItem2Binding) binding;
            imageView = this.binding.img;
            likeButton = this.binding.likeBtn;
            cardView = this.binding.imgParent;

            cardView.setOnClickListener(this);
            likeButton.setOnLikeChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == cardView) {
                listener.onClick(getAdapterPosition(), cardView, imageView);
            }
        }

        @Override
        public void onLikeChange(boolean isLiked) {
            if (isLiked) {
                listener.onLike(list.get(getAdapterPosition()));
            } else {
                listener.onUnlike(list.get(getAdapterPosition()));
            }
//            binding.setLikes(list.get(getAdapterPosition()).getLikes());
        }
    }

}
