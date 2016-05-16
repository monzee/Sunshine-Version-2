package com.example.android.sunshine.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.example.android.sunshine.app.index.IndexWiring;
import com.example.android.sunshine.app.shell.ShellWiring;
import com.example.android.sunshine.app.util.PerActivity;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
public class Sunshine extends Application {
    private Injector injector;

    @Singleton
    @Component(modules = Sunshine.class)
    public interface Injector {
        ActivityInjector activity(ActivityScope scope);
    }

    @PerActivity
    @Subcomponent(modules = ActivityScope.class)
    public interface ActivityInjector {
        IndexWiring.Injector index();
        ShellWiring.Injector shell(ShellWiring w);
    }

    @Module
    public static class ActivityScope {
        private final Activity activity;

        public ActivityScope(Activity activity) {
            this.activity = activity;
        }

        @Provides
        Activity activity() {
            return activity;
        }

        @Provides @PerActivity
        LinearLayoutManager linearLayoutManager() {
            return new LinearLayoutManager(activity);
        }

        @Provides @PerActivity
        LayoutInflater inflater() {
            return LayoutInflater.from(activity);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injector = DaggerSunshine_Injector.builder()
                .sunshine(this)
                .build();
    }

    public Injector getInjector() {
        return injector;
    }

    public ActivityInjector getInjector(Activity a) {
        return injector.activity(new ActivityScope(a));
    }

    @Provides
    Context context() {
        return this;
    }
}
