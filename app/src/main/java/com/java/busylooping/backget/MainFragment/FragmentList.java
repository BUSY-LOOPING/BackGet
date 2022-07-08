package com.java.busylooping.backget.MainFragment;

import androidx.annotation.NonNull;

public enum FragmentList {
    RECENT_FRAGMENT(0) {
        @NonNull
        @Override
        public String toString() {
            return "Recent";
        }
    },
    POPULAR_FRAGMENT(1) {
        @NonNull
        @Override
        public String toString() {
            return "Popular";
        }
    },
    SEARCH_FRAGMENT(2) {
        @NonNull
        @Override
        public String toString() {
            return "Search";
        }
    },
    UPLOAD_FRAGMENT(2) {
        @NonNull
        @Override
        public String toString() {
            return "Upload";
        }
    };

    private final int position;
    private FragmentList(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }


    public static FragmentList getFragment(int position) {
        FragmentList[] array = FragmentList.values();
        return array[position];
    }

    public static int getSize() {
        FragmentList[] array = FragmentList.values();
        return array.length;
    }
}
