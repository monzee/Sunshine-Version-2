package ph.codeia.solshine.index

import android.content.SharedPreferences
import android.os.Bundle
import ph.codeia.solshine.BuildConfig
import ph.codeia.solshine.IndexState
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration
import java.text.DateFormat
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class IndexPresenter @Inject constructor(
        private val state: IndexState,
        private val go: ShellContract.Navigation,
        private val msg: ShellContract.Feedback,
        private val repository: OwmService,
        private val prefs: SharedPreferences,
        @Named("medium") private val dateFormat: DateFormat
) : IndexContract.Interaction, IndexContract.Synchronization {

    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        if (!state.isStale) {
            state.isStale = true
            fetchForecasts()
        }
    }

    override fun didChooseItem(index: Int) {
        msg.toast("you clicked on $index", Duration.SHORT)
        go.launch(ShellContract.DETAIL, Bundle().apply {
            @Suppress("NEW_API")  // surely a bug, these are available since API 1.
            state.items[index].let {
                putString("date", dateFormat.format(it.date))
                putString("location", it.location)
                putString("status", it.status)
                putDouble("min", it.minTemp)
                putDouble("max", it.maxTemp)
            }
        })
    }

    override fun didPressViewMap() {
        @Suppress("fix this bundle api level bug pls.")
        go.launch(ShellContract.MAP, Bundle().apply {
            val (lat, long) = state.coords ?: Pair(0.0, 0.0)
            putDouble("lat", lat)
            putDouble("long", long)
        })
    }

    override fun bind(view: IndexContract.Display?) {
        this.view = view
    }

    override fun fetchForecasts() {
        if (state.isStale && !state.isPending) {
            state.isPending = true
            repository.fetchWeekForecast(
                    BuildConfig.OWM_API_KEY,
                    prefs.getString("location", "Manila"),
                    prefs.getString("units", "metric")
            ) {
                state.isPending = false
                it?.apply {
                    val location = "$city, $country"
                    state.coords = coords
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
        state.items.clear()
        state.items.addAll(newItems)
        state.isStale = false
        view?.refresh()
    }
}