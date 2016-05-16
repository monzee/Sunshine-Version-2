package com.example.android.sunshine.app.index;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.R;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.Entry> {

    public static class Entry extends RecyclerView.ViewHolder {
        public final TextView view;

        public Entry(View itemView) {
            super(itemView);
            view = (TextView) itemView;
        }

        public void show(IndexContract.Forecast f) {
            view.setText(String.format("%s - %d - %d",
                    f.getWeather(), f.getTemperature(), f.getHumidity()));
        }
    }

    @Inject
    LayoutInflater inflater;

    @Inject @Named("forecasts")
    List<IndexContract.Forecast> items;

    @Inject
    IndexContract.Interaction user;

    @Override
    public Entry onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item, parent, false);
        final Entry e = new Entry(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.didChooseForecast(e.getAdapterPosition());
            }
        });
        return e;
    }

    @Override
    public void onBindViewHolder(Entry holder, int position) {
        holder.show(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
