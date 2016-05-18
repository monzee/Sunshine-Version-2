package ph.codeia.solshine.shell

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import ph.codeia.solshine.index.ForecastsFragment
import ph.codeia.solshine.shell.ShellContract.Feature
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class Navigator @Inject constructor(
        private val activity: Activity,
        private val fragments: FragmentManager,
        @Named("contents") @IdRes val containerId: Int
) : ShellContract.Navigation {

    override fun back() {
        fragments.popBackStack()
    }

    override fun quit() {
        activity.finish()
    }

    override fun launch(f: Feature, args: Bundle?) {
        when (f) {
            Feature.INDEX -> {
                fragments.beginTransaction()
                        .add(containerId, ForecastsFragment())
                        .commit()
            }
            Feature.DETAIL -> {}
        }
    }

}
