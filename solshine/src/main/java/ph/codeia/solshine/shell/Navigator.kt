package ph.codeia.solshine.shell

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import ph.codeia.solshine.DetailActivity
import ph.codeia.solshine.FragmentStackState
import ph.codeia.solshine.R
import ph.codeia.solshine.SettingsFragment
import ph.codeia.solshine.index.ForecastsFragment
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
        private val stack: FragmentStackState,
        @Named("contents") @IdRes private val containerId: Int
) : ShellContract.Navigation {

    override var title = stack.nextTitle ?: activity.title
        set(value) {
            stack.nextTitle = value.toString()
            actionBar?.title = value
        }

    private var backStackSize = fragments.backStackEntryCount

    init {
        actionBar?.let {
            it.title = title
            fragments.addOnBackStackChangedListener {
                val newSize = fragments.backStackEntryCount
                if (backStackSize > newSize) {
                    title = stack.titles.pop()
                } else {
                    stack.titles.push(it.title.toString())
                    it.title = stack.nextTitle
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
            actionBar?.title = title
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtras(args)
            })
            true
        }

        ShellContract.MAP -> with(activity) {
            @Suppress("wrong lint due to ide plugin bug")
            args?.run {
                val lat = getDouble("lat")
                val long = getDouble("long")
                Intent(Intent.ACTION_VIEW).let { i ->
                    i.data = Uri.parse("geo:$lat,$long")
                    i.resolveActivity(activity.packageManager)?.let {
                        activity.startActivity(i)
                        true
                    } ?: false
                }
            } ?: false
        }

        ShellContract.SETTINGS -> when (containerId) {
            0 -> false // launch index, send SETTINGS as arg
            else -> true.apply {
                stack.nextTitle = activity.getString(R.string.settings)
                fragments.beginTransaction()
                        .replace(containerId, SettingsFragment(), "settings")
                        .addToBackStack("settings")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
            }
        }

        else -> false
    }
}
