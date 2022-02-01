package com.java.proj.view.CallBacks;

import android.view.View;
import android.widget.ImageView;

public interface RecyclerViewListener {
    void onClick (int position, View container, View view);
    void onDownload (int position);
    void onLike (int position);
    void onUnlike (int position);
}
