package com.java.busylooping.backget.CustomRecyclerView.Util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.java.busylooping.backget.models.GeneralModel;

import java.util.ArrayList;

public class MyDiffUtil extends DiffUtil.Callback {
    private ArrayList<GeneralModel> oldList;
    private ArrayList<GeneralModel> newList;

    public MyDiffUtil(@NonNull ArrayList<GeneralModel> oldList,@NonNull ArrayList<GeneralModel> newList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getImageId().equals(newList.get(newItemPosition).getImageId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        GeneralModel oldModel = oldList.get(oldItemPosition);
        GeneralModel newModel = newList.get(newItemPosition);

        return oldModel.isLiked() == newModel.isLiked() &&
                oldModel.getUriModel().equals(newModel.getUriModel()) &&
                oldModel.getLikes().equals(newModel.getLikes());
    }
}
