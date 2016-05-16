package com.example.android.sunshine.app.shell;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import com.example.android.sunshine.app.index.ForecastsFragment;
import com.example.android.sunshine.app.shell.ShellContract.Feature;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
public class Navigator implements ShellContract.Navigation {
    @Inject
    Activity activity;

    @Inject
    FragmentManager fm;

    @Inject @Named("content") @IdRes
    int container;

    @Override
    public void launch(@Feature int feature) {
        switch (feature) {
            case Feature.DETAIL:
                break;
            case Feature.INDEX:
                fm.beginTransaction()
                        .add(container, new ForecastsFragment(), "index")
                        .commit();
                break;
        }
    }

    @Override
    public void back() {
        fm.popBackStack();
    }

    @Override
    public void home() {
        launch(Feature.INDEX);
    }

    @Override
    public void quit() {
        activity.finish();
    }
}
