package com.example.android.sunshine.app.index;

import android.util.Log;

import com.example.android.sunshine.app.shell.ShellContract;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
public class IndexPresenter
        implements IndexContract.Interaction, IndexContract.Synchronization {
    public static final String TAG = "mzIndexPresenter";
    private IndexContract.Display view;

    @Inject @Named("forecasts")
    List<IndexContract.Forecast> forecasts;

    @Inject
    ShellContract.Navigation go;

    @Override
    public void didPressRefresh() {
        getForecasts();
    }

    @Override
    public void didChooseForecast(int index) {
        Log.d(TAG, String.format("showing forecast #%d", index));
        // go.launch(DETAILS, Bundle.withInt("id", forecasts.get(index).id)))
    }

    @Override
    public void bind(IndexContract.Display view) {
        this.view = view;
    }

    @Override
    public void getForecasts() {
        Log.d(TAG, "fetching");
        // service.fetch(5, this::gotForecasts)
    }

    @Override
    public void gotForecasts(List<IndexContract.Forecast> xs) {
        // forecasts.clear();
        // forecasts.addAll(xs);
        Log.d(TAG, String.format("got %d items", xs.size()));
        forecasts.add(IndexWiring.Weather.create(new Date(), "this is new!", 100, 10));
        if (view != null) {
            view.refresh();
        }
    }
}
