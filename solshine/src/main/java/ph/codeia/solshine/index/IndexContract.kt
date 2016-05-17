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
    }

    interface Synchronization {
        fun bind(view: Display?)
        fun getForecast()
        fun gotForecast(newItems: List<WeatherData>)
    }

    interface WeatherData {
        val location: String
        val status: String
        val date: Date
        val minTemp: Double
        val maxTemp: Double
    }
}
