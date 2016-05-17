package ph.codeia.solshine.shell

import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ph.codeia.solshine.MainActivity
import ph.codeia.solshine.PerFeature
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class ShellWiring(val fm: FragmentManager, @IdRes val container: Int) {

    @[PerFeature Subcomponent(modules = arrayOf(ShellWiring::class))]
    interface Injector {
        fun inject(a: MainActivity): MainActivity
    }

    @Provides
    fun nav(navigator: Navigator): ShellContract.Navigation = navigator

    @Provides
    fun fragMan(): FragmentManager = fm

    @Provides @Named("contents")
    fun contentPane(): Int = container
}
