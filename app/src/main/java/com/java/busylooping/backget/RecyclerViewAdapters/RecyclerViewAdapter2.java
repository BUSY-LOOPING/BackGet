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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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
import com.java.busylooping.backget.CustomRecyclerView.Util.MyDiffUtil;
import com.java.busylooping.backget.R;
import com.java.busylooping.backget.databinding.RecyclerViewItemLikesFragStaggeredBinding;
import com.java.busylooping.backget.models.GeneralModel;
import com.java.busylooping.backget.models.LikesModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.RecyclerViewHolder2>{

    private Context mContext;
    private RecyclerView recyclerView;
    private ArrayList<GeneralModel> list;
    private final RecyclerViewListener listener;

    private final int[] colors = new int[]{R.color.pink, R.color.yellow, R.color.orange, R.color.brown, R.color.purple};
    private final Random random;

    private final Observer<Map<String, GeneralModel>> observer = new Observer<Map<String, GeneralModel>>() {
        @Override
        public void onChanged(Map<String, GeneralModel> generalModels) {
            ArrayList<GeneralModel> newList;
            if (generalModels instanceof ArrayList)
                newList = (ArrayList<GeneralModel>) generalModels.values();
            else
                newList = new ArrayList<>(generalModels.values());
            RecyclerViewAdapter2.this.setData(newList);
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
                        if (index > -1 && index < list.size()) {
                            if (list.get(index).isLiked()) {
                                list.get(index).setLiked(false);
                                list.get(index).setLikes((Integer.parseInt(list.get(index).getLikes()) - 1) + "");
                                if (recyclerView.findViewHolderForAdapterPosition(index) != null) {
                                    RecyclerViewAdapter2.RecyclerViewHolder2 viewHolder =
                                            (RecyclerViewHolder2) recyclerView.findViewHolderForAdapterPosition(index);
                                    if (viewHolder != null) {
                                        viewHolder.binding.setVariable(BR.generalModel, list.get(index));
                                    }
                                }
                            }
                        }
                    }
                }
            }, mContext, list);
            searchInList.execute(model);
        }
    };

    public RecyclerViewAdapter2(Context mContext, ArrayList<GeneralModel> list, RecyclerViewListener listener) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listener;
        random = new Random();


//        new ViewModelProvider((ViewModelStoreOwner) mContext).get(LikesModel.class).
//                getCurrentLikedList().observe((LifecycleOwner) mContext, observer);

        LikesModel likesModel = new ViewModelProvider((ViewModelStoreOwner) mContext)
                .get(LikesModel.class);
        likesModel.getCurrentLikedList().observe((LifecycleOwner) mContext, observer);
//        likesModel.getLastUnLikedModel().observe((LifecycleOwner) mContext, unLikedObserver);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

//    @Override
//    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder2 holder) {
//        super.onViewAttachedToWindow(holder);
//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//        if(lp instanceof StaggeredGridLayoutManager.LayoutParams
//                && holder.binding.getGeneralModel().getDescription() != null &&  holder.binding.getGeneralModel().getDescription().length() > 500) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//            p.setFullSpan(true);
//        }
//        holder.setIsRecyclable(true);
//    }

    @NonNull
    @Override
    public RecyclerViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ViewDataBinding view = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_item_likes_frag_staggered, parent, false);
        return new RecyclerViewHolder2(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder2 holder, int position) {
        holder.bind(BR.generalModel, list.get(position));
        if (list.get(position) != null) {
            RequestOptions requestOptions = new RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(Uri.parse(list.get(position).getUriModel().getRegular()))
                    .placeholder(colors[random.nextInt(colors.length)])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView);

            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(Uri.parse(list.get(position).getProfileModel().getUserProfileImageModel().getMedium()))
                    .placeholder(colors[random.nextInt(colors.length)])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.artistImg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder2 extends BaseViewHolder
            implements View.OnClickListener, OnLikeChangeListener {
        private final ImageView imageView, artistImg;
        private final RecyclerViewListener listener;
        private final LikeView likeButton;
//        private final RecyclerViewItemLikesFragBinding binding;
        private final RecyclerViewItemLikesFragStaggeredBinding binding;
        private final LottieAnimationView downloadBtn;


        public RecyclerViewHolder2(@NonNull ViewDataBinding binding, RecyclerViewListener listener) {
            super(binding);
            this.listener = listener;
//            this.binding = (RecyclerViewItemLikesFragBinding) binding;
            this.binding = (RecyclerViewItemLikesFragStaggeredBinding) binding;
            imageView = this.binding.img;
            artistImg = this.binding.artistImg;
            likeButton = this.binding.likeBtn;
            downloadBtn = this.binding.downloadBtn;

//            this.binding.descriptionTxt.setMovementMethod(LinkMovementMethod.getInstance());
//            this.binding.artistBioTxt.setMovementMethod(LinkMovementMethod.getInstance());

            downloadBtn.setOnClickListener(this);
            likeButton.setOnLikeChangeListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                listener.onClick(getAdapterPosition(), view, view);
            } else if (view == downloadBtn) {
                listener.onUtils(list.get(getAdapterPosition()));
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

    public void setData(ArrayList<GeneralModel> newList) {
        MyDiffUtil diffUtil = new MyDiffUtil(list, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        list.clear();
        list.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}
