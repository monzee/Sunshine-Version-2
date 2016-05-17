package ph.codeia.solshine.openweathermap

import org.json.JSONObject
import java.util.*

/**
 * This file is a part of the Sunshine-Version-2 project.
 */

data class Report (
        val city: String,
        val country: String,
        val forecast: List<Forecast>
) {
    companion object {
        fun fromJson(json: String): Report {
            val payload = JSONObject(json)
            val city = payload.getJSONObject("city")
            val list = payload.getJSONArray("list")

            return Report(
                    city = city.getString("name"),
                    country = city.getString("country"),
                    forecast = (0..list.length() - 1).map {
                        val row = list.getJSONObject(it)
                        val temp = row.getJSONObject("temp")
                        val weather = row.getJSONArray("weather").getJSONObject(0)
                        Forecast(
                                clouds = row.getInt("clouds"),
                                windDirection = row.getInt("deg"),
                                date = Date(row.getLong("dt")),
                                humidity = row.getInt("humidity"),
                                pressure = row.getDouble("pressure"),
                                rain = row.getDouble("rain"),
                                speed = row.getDouble("speed"),
                                temperature = Temperature(
                                        day = temp.getDouble("day"),
                                        evening = temp.getDouble("eve"),
                                        min = temp.getDouble("min"),
                                        max = temp.getDouble("max"),
                                        morning = temp.getDouble("morn"),
                                        night = temp.getDouble("night")),
                                weather = Weather(
                                        category = weather.getString("main"),
                                        description = weather.getString("description")))
                    })
        }
    }
}

data class Forecast (
        /** cloud coverage, percentage */
        val clouds: Int,
        /** wind direction, meteorological degrees */
        val windDirection: Int,
        val date: Date,
        /** humidity, percentage */
        val humidity: Int,
        /** sea-level atmospheric pressure, hPa */
        val pressure: Double,
        /** precipitation, mm? */
        val rain: Double,
        /** wind speed, m/s */
        val speed: Double,
        val temperature: Temperature,
        val weather: Weather
)

/** all temperatures in Kelvin */
data class Temperature(
        val day: Double,
        val evening: Double,
        val min: Double,
        val max: Double,
        val morning: Double,
        val night: Double
)

data class Weather (
        /** single word weather description */
        val category: String,
        /** more specific weather description */
        val description: String
)
