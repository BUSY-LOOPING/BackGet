package com.java.proj.view.CallBacks;

import android.graphics.Bitmap;

public interface DownloadCallBack {
    void completed(int progress);
    void finalBitmap(Bitmap bitmap);
    void onFailed();
}
