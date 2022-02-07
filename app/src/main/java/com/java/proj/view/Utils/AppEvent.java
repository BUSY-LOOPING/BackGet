package com.java.proj.view.Utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AppEvent implements Parcelable {
    public int category;
    public int event;
    public int arg1;
    public int arg2;
    public Bundle extras;
    public WeakReference<ArrayList<Pair<String, Object>>> weakReferenceList;

    public AppEvent() {
        this.category = Integer.MIN_VALUE;
        this.event = Integer.MIN_VALUE;
        this.arg1 = 0;
        this.arg2 = 0;
        extras = new Bundle();
    }

    public enum AppEventCategory {
        BUTTON_CLICK(1){
            @NonNull
            @Override
            public String toString() {
                return "BUTTON_CLICK";
            }
        },
        BUTTON_LONG_CLICK(2){
            @NonNull
            @Override
            public String toString() {
                return "$BUTTON_LONG_CLICK";
            }
        };

        private final int value;
        private AppEventCategory (int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public AppEvent(int category, int event, float arg1, int arg2) {
        this.category = category;
        this.event = event;
        this.arg1 = Float.floatToRawIntBits(arg1);
        this.arg2 = arg2;
        extras = new Bundle();
    }

    private AppEvent(Parcel in) {
        this.category = in.readInt();
        this.event = in.readInt();
        this.arg1 = in.readInt();
        this.arg2 = in.readInt();
        this.extras = in.readBundle(getClass().getClassLoader());
    }


    public static final Creator<AppEvent> CREATOR = new Creator<AppEvent>() {
        @Override
        public AppEvent createFromParcel(Parcel in) {
            return new AppEvent(in);
        }

        @Override
        public AppEvent[] newArray(int size) {
            return new AppEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.category);
        parcel.writeInt(this.event);
        parcel.writeInt(this.arg1);
        parcel.writeInt(this.arg2);
        parcel.writeBundle(extras);
    }
}
