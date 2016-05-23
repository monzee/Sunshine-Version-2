package ph.codeia.solshine.index

import android.content.SharedPreferences
import android.os.Bundle
import ph.codeia.solshine.BuildConfig
import ph.codeia.solshine.IndexState
import ph.codeia.solshine.Result
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration
import java.text.DateFormat
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class IndexPresenter @Inject constructor(
        private val state: IndexState,
        private val go: ShellContract.Navigation,
        private val msg: ShellContract.Feedback,
        private val repository: OwmService,
        private val prefs: SharedPreferences,
        private val dateFormat: DateFormat
) : IndexContract.Interaction, IndexContract.Synchronization {

    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        if (!state.isPending) {
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
                putString("status", it.status)
                putDouble("min", it.minTemp)
                putDouble("max", it.maxTemp)
                state.location?.let { loc ->
                    putString("location", "${loc.city}, ${loc.country}")
                }
            }
        })
    }

    override fun didPressViewMap() {
        state.location?.let {
            val (lat, long) = it.coords
            @Suppress("fix this bundle api level bug pls.")
            go.launch(ShellContract.MAP, Bundle().apply {
                putDouble("lat", lat)
                putDouble("long", long)
            })
        }
    }

    override fun bind(view: IndexContract.Display?) {
        this.view = view
    }

    override fun fetchForecasts() {
        if (state.isStale && !state.isPending) {
            state.isPending = true
            repository.fetchForecasts(
                    BuildConfig.OWM_API_KEY,
                    prefs.getString("location", "Manila"),
                    units = prefs.getString("units", "metric")
            ) {
                state.isPending = false
                when (this) {
                    is Result.Ok -> value.apply {
                        forecastsFetched(forecasts.map {
                            Weather(it.weather.category, it.date,
                                    it.temperature.min, it.temperature.max)
                        }, Location(city, country, coords))
                    }
                    is Result.Err -> msg.tell(error.toString())
                }
            }
        }
    }

    override fun forecastsFetched(
            newItems: List<IndexContract.WeatherData>,
            location: IndexContract.Location
    ) {
        msg.log("got stuff: $newItems")
        state.items.clear()
        state.items.addAll(newItems)
        state.location = location
        state.isStale = false
        go.title = location.city
        view?.refresh()
    }
}