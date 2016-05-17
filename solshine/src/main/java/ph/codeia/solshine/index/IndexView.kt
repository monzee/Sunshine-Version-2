package ph.codeia.solshine.index

import android.support.v7.widget.RecyclerView
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class IndexView @Inject constructor(
    @Named("forecast") val listView: RecyclerView
) : IndexContract.Display {

    override fun refresh() {
        listView.adapter.notifyDataSetChanged()
    }
}
