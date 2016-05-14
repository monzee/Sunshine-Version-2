package com.example.android.sunshine.app.index;

import java.util.Date;
import java.util.List;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
public final class IndexContract {
    private IndexContract() {}

    public interface Display {
        void refresh();
    }

    public interface Interaction {
        void didPressRefresh();
        void didChooseForecast(int index);
    }

    public interface Synchronization {
        void bind(Display view);
        void getForecasts();
        void gotForecasts(List<Forecast> xs);
    }

    public interface Forecast {
        Date getDate();
        String getWeather();
        int getTemperature();
        int getHumidity();
    }
}
