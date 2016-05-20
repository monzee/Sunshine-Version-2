package ph.codeia.solshine.shell

import android.support.annotation.IdRes
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.Subcomponent
import ph.codeia.solshine.DetailActivity
import ph.codeia.solshine.MainActivity
import ph.codeia.solshine.PerFeature
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class ShellWiring(
        @get:[Provides Named("contents")] @IdRes internal val container: Int
) {

    @[PerFeature Subcomponent(modules = arrayOf(ShellWiring::class))]
    interface Injector {
        fun inject(a: MainActivity): MainActivity
        fun inject(a: DetailActivity): DetailActivity
    }

    @Provides
    fun nav(navigator: Navigator): ShellContract.Navigation = navigator

    @[Provides Reusable]
    fun message(msg: Messenger): ShellContract.Feedback = msg
}
