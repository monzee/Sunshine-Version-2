package com.example.android.sunshine.app.shell;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
public final class ShellContract {
    private ShellContract() {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Feature.INDEX, Feature.DETAIL})
    public @interface Feature {
        int INDEX = 0;
        int DETAIL = 1;
    }

    public interface Navigation {
        void launch(@Feature int feature);
        void back();
        void home();
        void quit();
    }
}
