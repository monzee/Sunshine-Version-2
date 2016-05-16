package com.example.android.sunshine.app.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sunshine.app.R;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastsFragment extends Fragment {
    @Inject
    IndexView view;

    @Inject
    IndexPresenter presenter;

    @Inject @Named("forecasts")
    RecyclerView forecasts;

    public ForecastsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.do_refresh:
                presenter.didPressRefresh();
                return true;
            case R.id.action_settings:
                presenter.gotForecasts(new ArrayList<IndexContract.Forecast>());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.bind(null);
        forecasts = null;
    }
}
