package ph.codeia.solshine.index

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Feature
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
private const val TAG = "mzIndexPresenter"

class IndexPresenter @Inject constructor(
    @Named("forecast") val items: MutableList<IndexContract.WeatherData>,
    val go: ShellContract.Navigation
) : IndexContract.Interaction, IndexContract.Synchronization {
    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        view?.refresh()
    }

    @SuppressLint("NewApi") // band-aid; kotlin plugin bug in bundle methods
    override fun didChooseItem(index: Int) {
        Log.d(TAG, "showing item #%d".format(index))
        go.launch(Feature.DETAIL, Bundle().apply {
            val w = items[index]
            putString("location", w.location)
            putString("status", w.status)
        })
    }

    override fun bind(view: IndexContract.Display?) {
        this.view = view
    }

    override fun getForecast() {
        Log.d(TAG, "fetching data")
    }

    override fun gotForecast(newItems: List<IndexContract.WeatherData>) {
        Log.d(TAG, "got data: %d items".format(newItems.size))
        items.addAll(newItems)
        view?.refresh()
    }
}