package ph.codeia.solshine.shell

import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.Subcomponent
import ph.codeia.solshine.MainActivity
import ph.codeia.solshine.PerFeature
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class ShellWiring(
        @get:Provides internal val fm: FragmentManager,
        @get:[Provides Named("contents")] @IdRes internal val container: Int
) {

    @[PerFeature Subcomponent(modules = arrayOf(ShellWiring::class))]
    interface Injector {
        fun inject(a: MainActivity): MainActivity
    }

    @Provides
    fun nav(navigator: Navigator): ShellContract.Navigation = navigator

    @[Provides Reusable]
    fun message(msg: Messenger): ShellContract.Messaging = msg
}
