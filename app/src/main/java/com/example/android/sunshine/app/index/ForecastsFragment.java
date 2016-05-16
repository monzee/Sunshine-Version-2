package com.example.android.sunshine.app.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sunshine.app.R;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastsFragment extends Fragment {
    @Inject @Named("forecasts")
    RecyclerView forecasts;

    public ForecastsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        forecasts = null;
    }
}
