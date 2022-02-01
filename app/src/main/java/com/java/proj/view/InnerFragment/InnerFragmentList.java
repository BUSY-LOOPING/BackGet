package com.java.proj.view.InnerFragment;

import androidx.annotation.NonNull;

public enum InnerFragmentList implements Query {
//    LIKED_FRAGMENT(0) {
//        @NonNull
//        @Override
//        public String toString() {
//            return "";
//        }
//
//        @Override
//        public String getQuery() {
//            return null;
//        }
//    },
    WALLPAPERS_FRAGMENT(0) {
        @NonNull
        @Override
        public String toString() {
            return "Wallpapers";
        }

        @Override
        public String getQuery() {
            return "wallpapers";
        }

    @Override
    public String getTopicIDorSlug() {
        return "wallpapers";
    }
},
    _3D_RENDERS_FRAGMENT(1) {
        @NonNull
        @Override
        public String toString() {
            return "3D Renders";
        }

        @Override
        public String getQuery() {
            return "3d%20renders";
        }

        @Override
        public String getTopicIDorSlug() {
            return "3d-renders";
        }
    },
    TEXTURES_N_PATTERNS(2) {
        @NonNull
        @Override
        public String toString() {
            return "Textures & Patterns";
        }

        @Override
        public String getQuery() {
            return "textures%20and%20patterns";
        }

        @Override
        public String getTopicIDorSlug() {
            return "textures-patterns";
        }
    },
    ARCHITECTURE(3) {
        @NonNull
        @Override
        public String toString() {
            return "Architecture";
        }

        @Override
        public String getQuery() {
            return "architecture";
        }

        @Override
        public String getTopicIDorSlug() {
            return "architecture";
        }
    },
    EXPERIMENTAL(4) {
        @NonNull
        @Override
        public String toString() {
            return "Experimental";
        }

        @Override
        public String getQuery() {
            return "experimental";
        }

        @Override
        public String getTopicIDorSlug() {
            return "experimental";
        }
    },
    NATURE(5) {
        @NonNull
        @Override
        public String toString() {
            return "Nature";
        }

        @Override
        public String getQuery() {
            return "nature";
        }

        @Override
        public String getTopicIDorSlug() {
            return "nature";
        }
    },
    BUSINESS_N_WORK(6) {
        @NonNull
        @Override
        public String toString() {
            return "Business & Work";
        }

        @Override
        public String getQuery() {
            return "business%20and%20work";
        }

        @Override
        public String getTopicIDorSlug() {
            return "business-work";
        }
    },
    FASHION(7) {
        @NonNull
        @Override
        public String toString() {
            return "Fashion";
        }

        @Override
        public String getQuery() {
            return "fashion";
        }

        @Override
        public String getTopicIDorSlug() {
            return "fashion";
        }
    },
    FILM(8) {
        @NonNull
        @Override
        public String toString() {
            return "Film";
        }

        @Override
        public String getQuery() {
            return "film";
        }

        @Override
        public String getTopicIDorSlug() {
            return "film";
        }
    },
    FOOD_N_DRINK(9) {
        @NonNull
        @Override
        public String toString() {
            return "Food & Drink";
        }

        @Override
        public String getQuery() {
            return "food%20and%20drink";
        }

        @Override
        public String getTopicIDorSlug() {
            return "food-drink";
        }
    },
    HEALTH_N_WELLNESS(10) {
        @NonNull
        @Override
        public String toString() {
            return "Health & Wellness";
        }

        @Override
        public String getQuery() {
            return "health%20and%20wellness";
        }

        @Override
        public String getTopicIDorSlug() {
            return "health";
        }
    },
    CURRENT_EVENTS(11) {
        @NonNull
        @Override
        public String toString() {
            return "Current Events";
        }

        @Override
        public String getQuery() {
            return "current%20events";
        }

        @Override
        public String getTopicIDorSlug() {
            return "current-events";
        }
    },
    PEOPLE(12) {
        @NonNull
        @Override
        public String toString() {
            return "People";
        }

        @Override
        public String getQuery() {
            return "people";
        }

        @Override
        public String getTopicIDorSlug() {
            return "people";
        }
    },
    INTERIORS(13) {
        @NonNull
        @Override
        public String toString() {
            return "Interiors";
        }

        @Override
        public String getQuery() {
            return "interiors";
        }

        @Override
        public String getTopicIDorSlug() {
            return "interiors";
        }
    },
    STREET_PHOTOGRAPHY(14) {
        @NonNull
        @Override
        public String toString() {
            return "Street Photography";
        }

        @Override
        public String getQuery() {
            return "street%20photography";
        }

        @Override
        public String getTopicIDorSlug() {
            return "street-photography";
        }
    },
    TRAVEL(15) {
        @NonNull
        @Override
        public String toString() {
            return "Travel";
        }

        @Override
        public String getQuery() {
            return "travel";
        }

        @Override
        public String getTopicIDorSlug() {
            return "travel";
        }
    },
    ANIMALS(16) {
        @NonNull
        @Override
        public String toString() {
            return "Animals";
        }

        @Override
        public String getQuery() {
            return "animals";
        }

        @Override
        public String getTopicIDorSlug() {
            return "animals";
        }
    },
    SPIRITUALITY(17) {
        @NonNull
        @Override
        public String toString() {
            return "Spirituality";
        }

        @Override
        public String getQuery() {
            return "spirituality";
        }

        @Override
        public String getTopicIDorSlug() {
            return "spirituality";
        }
    },
    ARTS_N_CULTURE(18) {
        @NonNull
        @Override
        public String toString() {
            return "Arts & Culture";
        }

        @Override
        public String getQuery() {
            return "arts%20and%20culture";
        }

        @Override
        public String getTopicIDorSlug() {
            return "arts-culture";
        }
    },
    HISTORY(19) {
        @NonNull
        @Override
        public String toString() {
            return "History";
        }

        @Override
        public String getQuery() {
            return "history";
        }

        @Override
        public String getTopicIDorSlug() {
            return "history";
        }
    },
    ATHLETICS(20) {
        @NonNull
        @Override
        public String toString() {
            return "Athletics";
        }

        @Override
        public String getQuery() {
            return "athletics";
        }

        @Override
        public String getTopicIDorSlug() {
            return "athletics";
        }
    };

    private final int position;

    private InnerFragmentList(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public static InnerFragmentList getFragment(int position) {
        InnerFragmentList[] array = InnerFragmentList.values();
        return array[position];
    }

    public static int getSize() {
        InnerFragmentList[] array = InnerFragmentList.values();
        return array.length;
    }
}
