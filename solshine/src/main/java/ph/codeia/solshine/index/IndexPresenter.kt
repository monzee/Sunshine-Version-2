package ph.codeia.solshine.index

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import ph.codeia.solshine.BuildConfig
import ph.codeia.solshine.PerFeature
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration
import ph.codeia.solshine.shell.ShellContract.Feature
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@PerFeature
class IndexPresenter @Inject constructor(
        @Named("forecasts") val items: MutableList<IndexContract.WeatherData>,
        val go: ShellContract.Navigation,
        val msg: ShellContract.Messaging,
        @Named("fixture") val repository: OwmService
) : IndexContract.Interaction, IndexContract.Synchronization {
    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        getForecast()
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
        repository.fetchWeekForecast(BuildConfig.OWM_API_KEY, "manila") {
            val city = "${it?.city}, ${it?.country}"
            gotForecast(it?.forecast!!.map {
                Weather(city, it.weather.category, it.date,
                        it.temperature.min, it.temperature.max)
            })
        }
    }

    override fun gotForecast(newItems: List<IndexContract.WeatherData>) {
        Log.d("mz", "got stuff: " + newItems)
        items.clear()
        items.addAll(newItems)
        view?.refresh()
    }
}