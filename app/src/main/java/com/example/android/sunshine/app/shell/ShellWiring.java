package com.example.android.sunshine.app.shell;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import com.example.android.sunshine.app.MainActivity;

import javax.inject.Named;

import dagger.MembersInjector;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
public class ShellWiring {
    private final FragmentManager fm;
    private final @IdRes int contentId;

    @Subcomponent(modules = ShellWiring.class)
    public interface Injector {
        void inject(MainActivity activity);
    }

    public ShellWiring(FragmentManager fm, @IdRes int contentId) {
        this.fm = fm;
        this.contentId = contentId;
    }

    @Provides
    ShellContract.Navigation nav(MembersInjector<Navigator> deps) {
        Navigator n = new Navigator();
        deps.injectMembers(n);
        return n;
    }

    @Provides
    FragmentManager fragMan() {
        return fm;
    }

    @Provides @Named("content") @IdRes
    int content() {
        return contentId;
    }
}
