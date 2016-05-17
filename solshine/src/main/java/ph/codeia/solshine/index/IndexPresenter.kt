package ph.codeia.solshine.index

import android.annotation.SuppressLint
import android.os.Bundle
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration
import ph.codeia.solshine.shell.ShellContract.Feature
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class IndexPresenter @Inject constructor(
    @Named("forecast") val items: MutableList<IndexContract.WeatherData>,
    val go: ShellContract.Navigation,
    val msg: ShellContract.Messaging
) : IndexContract.Interaction, IndexContract.Synchronization {
    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        msg.tell("refreshing...")
        view?.refresh()
    }

    @SuppressLint("NewApi") // band-aid; kotlin plugin bug in bundle methods
    override fun didChooseItem(index: Int) {
        msg.toast("you clicked on #%d".format(index), Duration.SHORT)
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
        msg.tell("fetching")
    }

    override fun gotForecast(newItems: List<IndexContract.WeatherData>) {
        msg.tell("got new stuff")
        items.addAll(newItems)
        view?.refresh()
    }
}