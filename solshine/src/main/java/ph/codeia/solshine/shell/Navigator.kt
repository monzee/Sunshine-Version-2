package ph.codeia.solshine.shell

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import ph.codeia.solshine.DetailActivity
import ph.codeia.solshine.SettingsFragment
import ph.codeia.solshine.index.ForecastsFragment
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class Navigator @Inject constructor(
        private val activity: Activity,
        private val fragments: FragmentManager,
        private val actionBar: ActionBar?,
        private val log: ShellContract.Feedback,
        @Named("contents") @IdRes private val containerId: Int
) : ShellContract.Navigation {

    private final val titles: Deque<String> = ArrayDeque()
    private var nextTitle: String? = null
    private var backStackSize = fragments.backStackEntryCount

    init {
        actionBar?.let {
            fragments.addOnBackStackChangedListener {
                val newSize = fragments.backStackEntryCount
                if (backStackSize > newSize) {
                    it.title = titles.pop()
                } else it.title.toString().let { t ->
                    titles.push(t)
                    it.title = nextTitle ?: t
                }
                backStackSize = newSize
            }
        }
    }

    override fun back() {
        // i need to rethink this
    }

    override fun launch(f: Long, args: Bundle?): Boolean = when (f) {
        ShellContract.INDEX -> when (containerId) {
            0 -> false
            else -> true.apply {
                // if this is the first run, do this
                fragments.beginTransaction()
                        .add(containerId, ForecastsFragment(), "home")
                        .commit()
                // otherwise pop the backstack until you see this fragment
            }
        }

        ShellContract.DETAIL -> with(activity) {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtras(args)
            })
            true
        }

        ShellContract.SETTINGS -> when (containerId) {
            0 -> false // launch index, send SETTINGS as arg
            else -> true.apply {
                nextTitle = "Settings"
                fragments.beginTransaction()
                        .hide(fragments.findFragmentById(containerId))
                        .add(containerId, SettingsFragment(), "settings")
                        .addToBackStack("settings")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
            }
        }

        else -> false
    }
}
