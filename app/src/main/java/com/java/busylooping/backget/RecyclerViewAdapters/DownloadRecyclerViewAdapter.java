package com.java.busylooping.backget.RecyclerViewAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.busy.looping.like_view_library.LikeView;
import com.busy.looping.like_view_library.OnLikeChangeListener;
import com.java.busylooping.backget.AsyncTasks.SearchInList;
import com.java.busylooping.backget.BR;
import com.java.busylooping.backget.BaseViewHolder;
import com.java.busylooping.backget.CallBacks.AsyncResponse;
import com.java.busylooping.backget.CallBacks.RecyclerViewListener;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.databinding.DownloadItemBinding;
import com.java.busylooping.backget.models.DownloadGeneralModel;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikesModel;

import java.util.ArrayList;
import java.util.Random;

public class DownloadRecyclerViewAdapter extends RecyclerView.Adapter<DownloadRecyclerViewAdapter.DownloadRecyclerViewAdapterViewHolder> {
    private final Context mContext;
    private ArrayList<DownloadGeneralModel> list;
    private final RecyclerViewListener listener;
    private RecyclerView recyclerView;

    private final int[] colors = new int[]{R.color.pink, R.color.yellow, R.color.orange, R.color.brown, R.color.purple};
    private final Random random;

    private final Observer<GeneralModel> likedObserver = new Observer<GeneralModel>() {
        @Override
        public void onChanged(GeneralModel model) {
            SearchInList<GeneralModel, Integer, Void> searchInList = new SearchInList<GeneralModel, Integer, Void>(new AsyncResponse<GeneralModel, Integer, ArrayList<Integer>>() {
                @Override
                public void onProcessFinished(ArrayList<Integer> output) {
                    for (int i = 0; i < output.size(); i++) {
                        int index = output.get(i);
                        if (index > -1) {
                            if (!list.get(index).isLiked()) {
                                list.get(index).setLiked(true);
//                                list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) + 1) + "");
                                if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
                                    DownloadRecyclerViewAdapter.DownloadRecyclerViewAdapterViewHolder viewHolder =
                                            (DownloadRecyclerViewAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                                    if (viewHolder != null) {
                                        viewHolder.binding.setVariable(BR.generalDownloadModel, list.get(index));
                                    }
                                }
                            }
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
//                if (!list.get(index).isLiked()) {
//                    list.get(index).setLiked(true);
//                    list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) + 1) + "");
//                    if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
//                        RecyclerViewAdapter.RecentRecyclerViewHolder viewHolder =
//                                (RecentRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
//                        if (viewHolder != null) {
//                            viewHolder.binding.setIsLiked(true);
//                            viewHolder.binding.setLikes(list.get(index).getLikes());
//                        } else {
//                        }
//                    } else {
//                    }
//                }
//            }
        }
    };

    private final Observer<GeneralModel> unLikedObserver = new Observer<GeneralModel>() {
        @Override
        public void onChanged(GeneralModel model) {
            SearchInList<GeneralModel, Void, ArrayList<Integer>> searchInList = new SearchInList<>(new AsyncResponse<GeneralModel, Void, ArrayList<Integer>>() {
                @Override
                public void onProcessFinished(ArrayList<Integer> output) {
                    for (int i = 0 ;i < output.size(); i++) {
                        int index = output.get(i);
                        if (index > -1) {
                            if (list.get(index).isLiked()) {
                                list.get(index).setLiked(false);
//                                list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) - 1) + "");
                                if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
                                    DownloadRecyclerViewAdapter.DownloadRecyclerViewAdapterViewHolder viewHolder =
                                            (DownloadRecyclerViewAdapterViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
                                    if (viewHolder != null) {
                                        viewHolder.binding.setVariable(BR.generalDownloadModel, list.get(index));
                                    }
                                }
                            }
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
//                if (list.get(index).isLiked()) {
//                    Log.d("myrecycler", "list element is liked ");
//                    list.get(index).setLiked(false);
////                list.get(index).setLikes(model.getLikes());
//                    list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) - 1) + "");
//                    if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
//                        Log.d("myrecycler", "findViewHolderForAdapterPosition is not null");
//                        RecyclerViewAdapter.RecentRecyclerViewHolder viewHolder =
//                                (RecentRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(index);
//                        if (viewHolder != null) {
//                            Log.d("myrecycler", "viewHolder is not null");
//                            viewHolder.binding.setIsLiked(false);
////                        viewHolder.binding.setLikes(model.getLikes());
//                            viewHolder.binding.setLikes(list.get(index).getLikes());
//                        }
//                    } else {
//                        Log.d("myrecycler", "findViewHolderForAdapterPosition is null");
//                    }
//                }
//            }
        }
    };

    public DownloadRecyclerViewAdapter(@NonNull Context context, ArrayList<DownloadGeneralModel> list, RecyclerViewListener listener) {
        this.list = list;
        this.mContext = context;
        this.listener = listener;
        random = new Random();

        LikesModel likesModel = new ViewModelProvider((ViewModelStoreOwner) mContext)
                .get(LikesModel.class);
        likesModel.getLastLikedModel().observe((LifecycleOwner) mContext, likedObserver);
        likesModel.getLastUnLikedModel().observe((LifecycleOwner) mContext, unLikedObserver);
    }

    @NonNull
    @Override
    public DownloadRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ViewDataBinding view = DataBindingUtil.inflate(layoutInflater, R.layout.download_item, parent, false);
        return new DownloadRecyclerViewAdapterViewHolder(view, listener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

//    @Override
//    public void onViewAttachedToWindow(@NonNull DownloadRecyclerViewAdapterViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//        if(lp instanceof StaggeredGridLayoutManager.LayoutParams
//                && holder.binding.getGeneralDownloadModel().getDescription() != null &&  holder.binding.getGeneralDownloadModel().getDescription().length() > 500) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//            p.setFullSpan(true);
//        }
//        holder.setIsRecyclable(false);
//    }

    @Override
    public void onBindViewHolder(@NonNull DownloadRecyclerViewAdapterViewHolder holder, int position) {
        holder.bind(BR.generalDownloadModel, list.get(position));
        holder.bind(BR.dataHolder, DownloadGeneralModel.getDownloadDataHolder(mContext, list.get(position)));
        RequestOptions requestOptions = new RequestOptions().dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(Uri.parse(list.get(position).getSavePath()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(colors[random.nextInt(colors.length)])
                .apply(requestOptions)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DownloadRecyclerViewAdapterViewHolder extends BaseViewHolder
            implements View.OnClickListener, OnLikeChangeListener {
        private final DownloadItemBinding binding;
        private final RecyclerViewListener listener;

        private final ImageView imageView;
        private final LikeView likeButton;

        public DownloadRecyclerViewAdapterViewHolder(@NonNull ViewDataBinding binding, RecyclerViewListener listener) {
            super(binding);

            this.binding = (DownloadItemBinding) binding;
            this.listener = listener;

            this.imageView = this.binding.img;
            this.likeButton = this.binding.likeBtn;

            likeButton.setOnLikeChangeListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == binding.getRoot()) {
                listener.onClick(getAdapterPosition(), view, view);
            }
        }

        @Override
        public void onLikeChange(boolean isLiked) {
            if (isLiked) {
                listener.onLike(list.get(getAdapterPosition()));
            } else {
                listener.onUnlike(list.get(getAdapterPosition()));
            }
        }
    }
}
