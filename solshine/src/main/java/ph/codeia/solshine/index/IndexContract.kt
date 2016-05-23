package ph.codeia.solshine.index

import java.util.*

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
object IndexContract {
    interface Display {
        fun refresh()
    }

    interface Interaction {
        fun didPressRefresh()
        fun didChooseItem(index: Int)
        fun didPressViewMap()
    }

    interface Synchronization {
        fun bind(view: Display?)
        fun fetchForecasts()
        fun forecastsFetched(newItems: List<WeatherData>, location: Location)
    }

    interface WeatherData {
        val status: String
        val date: Date
        val minTemp: Double
        val maxTemp: Double
    }

    interface Location {
        val city: String
        val country: String
        val coords: Pair<Double, Double>
    }
}
