package com.example.android.sunshine.app.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sunshine.app.R;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastsFragment extends Fragment {

    // injected views need to be lazy because i need to wait until the fragment becomes a part of
    // the activity view hierarchy before i could call activity.findViewById. i think that happens
    // during onViewCreated, but the properties are injected very early. i have no choice
    // because there's only one hook on the activity that involves fragments: onAttachFragment.
    // i could also perform the injection inside the fragment, but it's clunky/redundant because of
    // the way i have setup the @Component hierarchy. and i'd also like to avoid using the di
    // framework as a service locator as much as possible.

    // or i could use butterknife instead.
    @Inject @Named("forecasts")
    Lazy<RecyclerView> forecasts;

    public ForecastsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forecasts.get();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        forecasts = null;
    }
}
