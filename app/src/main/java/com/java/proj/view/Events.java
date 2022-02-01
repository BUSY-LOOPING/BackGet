package com.java.proj.view;

import androidx.annotation.NonNull;


//global events we want to be handled by event-bus
public enum Events {
    SHARE_BUTTON_PRESSED {
        @NonNull
        @Override
        public String toString() {
            return "Events{SHARE_BUTTON_PRESSED}";
        }
    },
    SETTINGS_BUTTON_PRESSED{
        @NonNull
        @Override
        public String toString() {
            return "Events{SETTINGS_BUTTON_PRESSED}";
        }
    },
    IMAGE_CLICK {
        @NonNull
        @Override
        public String toString() {
            return "Events{IMAGE_CLICK}";
        }
    },
    GALLERY_BUTTON_PRESSED {
        @NonNull
        @Override
        public String toString() {
            return "Events{GALLERY_BTN_PRESSED}";
        }
    }


}
