package com.example.android.sunshine.app.index;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.shell.ShellWiring;
import com.example.android.sunshine.app.util.PerFeature;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import dagger.MembersInjector;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
public class IndexWiring {
    private final List<IndexContract.Forecast> dummyData = new ArrayList<>();

    @PerFeature
    @Subcomponent(modules = {ShellWiring.class, IndexWiring.class})
    public interface Injector {
        ForecastsFragment inject(ForecastsFragment f);
    }

    @AutoValue
    static abstract class Weather implements IndexContract.Forecast {
        public static IndexContract.Forecast create(
                Date date, String weather, int temperature, int humidity) {
            return new AutoValue_IndexWiring_Weather(date, weather, temperature,
                    humidity);
        }
    }

    {
        dummyData.add(Weather.create(new Date(1463370935030L), "sunny", 12, 42));
        dummyData.add(Weather.create(new Date(1463370955030L), "rainy", 93, 64));
        dummyData.add(Weather.create(new Date(1463370975030L), "sunny", 42, 21));
        dummyData.add(Weather.create(new Date(1463370995030L), "sunny", 58, 85));
        dummyData.add(Weather.create(new Date(1463371015030L), "sunny", 53, 32));
    }

    @Provides @PerFeature
    IndexView view(MembersInjector<IndexView> deps) {
        IndexView v = new IndexView();
        deps.injectMembers(v);
        return v;
    }

    @Provides @PerFeature
    IndexPresenter presenter(MembersInjector<IndexPresenter> deps) {
        IndexPresenter p = new IndexPresenter();
        deps.injectMembers(p);
        return p;
    }

    @Provides
    IndexContract.Interaction interaction(IndexPresenter p) {
        return p;
    }

    @Provides @PerFeature
    ForecastsAdapter adapter(MembersInjector<ForecastsAdapter> deps) {
        ForecastsAdapter a = new ForecastsAdapter();
        deps.injectMembers(a);
        return a;
    }

    @Provides @Named("forecasts")
    RecyclerView recyclerView(Activity activity, ForecastsAdapter a, LinearLayoutManager lm) {
        RecyclerView rv = (RecyclerView) activity.findViewById(R.id.list_forecasts);
        rv.setAdapter(a);
        rv.setLayoutManager(lm);
        return rv;
    }

    @Provides @Named("forecasts")
    List<IndexContract.Forecast> data() {
        return dummyData;
    }
}
