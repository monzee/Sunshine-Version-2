package com.example.android.sunshine.app.index;

import javax.inject.Inject;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
public class IndexView implements IndexContract.Display {
    @Inject
    ForecastsAdapter adapter;

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }
}
