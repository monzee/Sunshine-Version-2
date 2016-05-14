package com.example.android.sunshine.app.index;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.sunshine.app.R;

import java.util.ArrayList;
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
    @Subcomponent(modules = IndexWiring.class)
    public interface Injector {
        ForecastsFragment inject(ForecastsFragment f);
    }

    private final List<String> dummyData = new ArrayList<>();
    {
        dummyData.add("abc - sunny - 12/42");
        dummyData.add("def - rainy - 93/64");
        dummyData.add("ghi - snowy - 42/21");
        dummyData.add("jkl - foggy - 58/85");
        dummyData.add("mno - hairy - 53/32");
    }

    @Provides
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
    List<String> data() {
        return dummyData;
    }
}
