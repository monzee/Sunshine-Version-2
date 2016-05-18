package ph.codeia.solshine.index

import android.annotation.SuppressLint
import android.os.Bundle
import ph.codeia.solshine.BuildConfig
import ph.codeia.solshine.PerFeature
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Duration
import ph.codeia.solshine.shell.ShellContract.Feature
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@PerFeature
class IndexPresenter @Inject constructor(
        @Named("forecasts") private val items: MutableList<IndexContract.WeatherData>,
        @Named("forecasts") private val stale: AtomicBoolean,
        private val go: ShellContract.Navigation,
        private val msg: ShellContract.Messaging,
        private val repository: OwmService
) : IndexContract.Interaction, IndexContract.Synchronization {
    private var view: IndexContract.Display? = null

    override fun didPressRefresh() {
        stale.set(true)
        getForecasts()
    }

    @SuppressLint("NewApi") // band-aid; kotlin plugin bug in bundle methods
    override fun didChooseItem(index: Int) {
        msg.toast("you clicked on $index", Duration.SHORT)
        go.launch(Feature.DETAIL, Bundle().apply {
            items[index].let {
                putString("location", it.location)
                putString("status", it.status)
            }
        })
    }

    override fun bind(view: IndexContract.Display?) {
        this.view = view
    }

    override fun getForecasts() {
        if (stale.get()) {
            repository.fetchWeekForecast(BuildConfig.OWM_API_KEY, "94043") {
                it?.apply {
                    val location = "$city, $country"
                    gotForecasts(forecasts.map {
                        Weather(location, it.weather.category, it.date,
                                it.temperature.min, it.temperature.max)
                    })
                }
                it ?: msg.tell("got nothing")
            }

        }
    }

    override fun gotForecasts(newItems: List<IndexContract.WeatherData>) {
        msg.log("got stuff: $newItems")
        items.clear()
        items.addAll(newItems)
        stale.set(false)
        view?.refresh()
    }
}