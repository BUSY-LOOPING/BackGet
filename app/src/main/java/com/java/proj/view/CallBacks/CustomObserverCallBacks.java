package com.java.proj.view.CallBacks;

import androidx.lifecycle.Observer;

public interface CustomObserverCallBacks<T, M> extends Observer<T> {
    @Override
    void onChanged(T t);

    void onNewAdded(M m, int pos);

    void onRemoved(M m, int pos);
}
