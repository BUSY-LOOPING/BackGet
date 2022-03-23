package com.java.proj.view.Utils;

import androidx.lifecycle.LiveData;

import com.java.proj.view.CallBacks.CustomObserverCallBacks;

import org.checkerframework.checker.units.qual.C;

public class CustomLiveData<T, M> extends LiveData<T> implements CustomObserverCallBacks<T, M> {
    @Override
    protected void postValue(T value) {
        super.postValue(value);
    }

    @Override
    protected void setValue(T value) {
        super.setValue(value);
    }


    public void addValueToList(M data) {

    }

    @Override
    public void onChanged(T t) {

    }

    @Override
    public void onNewAdded(M m, int pos) {

    }

    @Override
    public void onRemoved(M m, int pos) {

    }
}
