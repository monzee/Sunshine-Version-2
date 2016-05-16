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
    }

    @Inject
    LayoutInflater inflater;

    @Inject @Named("forecasts")
    List<String> items;

    @Override
    public Entry onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new Entry(v);
    }

    @Override
    public void onBindViewHolder(Entry holder, int position) {
        holder.view.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
