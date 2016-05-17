package ph.codeia.solshine.index

import java.util.*

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
data class Weather(
        override val location: String,
        override val status: String,
        override val date: Date,
        override val temperature: Int,
        override val humidity: Int
) : IndexContract.WeatherData
