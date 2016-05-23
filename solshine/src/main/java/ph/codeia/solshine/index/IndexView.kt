package ph.codeia.solshine.index

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration.SHORT
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class IndexView @Inject constructor(
        private val log: ShellContract.Feedback,
        private val listView: RecyclerView,
        private val adapter: ForecastsAdapter
) : IndexContract.Display {
    private var lastCount = adapter.itemCount

    @Inject
    fun init(linear: LinearLayoutManager) = listView.let {
        it.adapter = adapter
        it.layoutManager = linear
    }

    override fun refresh() {
        lastCount = with(adapter) {
            val newCount = itemCount
            if (newCount < lastCount) {
                notifyDataSetChanged()
            } else {
                notifyItemRangeChanged(0, newCount)
            }
            newCount
        }
        log.toast("refreshed.", SHORT)
    }
}
