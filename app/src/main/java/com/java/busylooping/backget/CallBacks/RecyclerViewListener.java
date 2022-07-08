package com.java.busylooping.backget.CallBacks;

import android.view.View;

import com.java.busylooping.backget.models.GeneralModel;

public interface RecyclerViewListener {
    void onClick (int position, View container, View view);
    void onUtils(GeneralModel generalModel);

    /**after user clicks on like button ->
            -> control comes to onLike with position in @param
            -> global likes model gets updated with recent liked pic
            -> control goes to recycler view adapter where likes model observer sees the change and does 3 things ->
                -> finds the index if the model is present in the list, (if index > -1) then,
                -> updates the list (which is present as a reference) at the @index position with +1 and sets it as liked
                -> if the viewholder for that position is present, we find that viewholder from the recycler view reference and update the binding in it.
             -> after this simply database gets updated
        */
    void onLike (GeneralModel generalModel);

    /**after user clicks on unlike button ->
            -> control comes to onUnlike with position in @param
            -> global likes model gets updated with recent liked pic
            -> control goes to recycler view adapter where likes model observer sees the change and does 3 things ->
                -> finds the index if the model is present in the list, (if index > -1) then,
                -> updates the list (which is present as a reference) at the @index position with -1 and sets it as liked
                -> if the viewholder for that position is present, we find that viewholder from the recycler view reference and update the binding in it.
             -> after this simply database gets updated
        */
    void onUnlike (GeneralModel generalModel);
}
