package ph.codeia.solshine.index

import android.support.v7.widget.RecyclerView
import ph.codeia.solshine.PerFeature
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration.SHORT
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@PerFeature
class IndexView @Inject constructor(
        val log: ShellContract.Messaging,
        @Named("forecasts") val listView: RecyclerView
) : IndexContract.Display {
    private val adapter = listView.adapter
    private var lastCount = -1

    override fun refresh() {
        lastCount = adapter.run {
            val newCount = itemCount
            when {
                newCount < lastCount -> notifyDataSetChanged()
                else -> notifyItemRangeChanged(0, newCount)
            }
            newCount
        }
        log.toast("refreshed.", SHORT)
    }
}
