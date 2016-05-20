package ph.codeia.solshine.index

import android.annotation.SuppressLint
import android.os.Bundle
import ph.codeia.solshine.BuildConfig
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class IndexPresenter @Inject constructor(
        @Named("forecasts") private val items: MutableList<IndexContract.WeatherData>,
        @Named("forecasts_stale") private val stale: AtomicBoolean,
        @Named("forecasts_pending") private val pending: AtomicBoolean,
        private val go: ShellContract.Navigation,
        private val msg: ShellContract.Feedback,
        private val repository: OwmService
) : IndexContract.Interaction, IndexContract.Synchronization {
    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        if (!pending.get()) {
            stale.set(true)
            fetchForecasts()
        }
    }

    @SuppressLint("NewApi") // band-aid; kotlin plugin bug in bundle methods
    override fun didChooseItem(index: Int) {
        msg.toast("you clicked on $index", Duration.SHORT)
        go.launch(ShellContract.DETAIL, Bundle().apply {
            items[index].let {
                putString("location", it.location)
                putString("status", it.status)
            }
        })
    }

    override fun bind(view: IndexContract.Display?) {
        this.view = view
    }

    override fun fetchForecasts() {
        if (stale.get() && !pending.get()) {
            pending.set(true)
            repository.fetchWeekForecast(BuildConfig.OWM_API_KEY, "manila") {
                pending.set(false)
                it?.apply {
                    val location = "$city, $country"
                    forecastsFetched(forecasts.map {
                        Weather(location, it.weather.category, it.date,
                                it.temperature.min, it.temperature.max)
                    })
                } ?: msg.tell("got nothing")
            }
        }
    }

    override fun forecastsFetched(newItems: List<IndexContract.WeatherData>) {
        msg.log("got stuff: $newItems")
        items.clear()
        items.addAll(newItems)
        stale.set(false)
        view?.refresh()
    }
}