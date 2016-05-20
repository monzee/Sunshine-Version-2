package ph.codeia.solshine.openweathermap

import org.json.JSONObject
import ph.codeia.solshine.JsonDeserializable
import ph.codeia.solshine.rescue
import java.util.*

data class Report (
        val city: String,
        val country: String,
        val coords: Pair<Double, Double>,
        val forecasts: List<Forecast>
) {
    companion object : JsonDeserializable<Report> {
        override fun fromJson(json: JSONObject): Report {
            val city = json.getJSONObject("city")
            val list = json.getJSONArray("list")
            val coord = city.getJSONObject("coord")
            val lat = coord.getDouble("lat")
            val lon = coord.getDouble("lon")

            return Report(
                    city = city.getString("name"),
                    country = city.getString("country"),
                    coords = Pair(lat, lon),
                    forecasts = (0..list.length() - 1).map {
                        Forecast.fromJson(list.getJSONObject(it))
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
        /** precipitation, in mm? not always present, only when raining i guess */
        val rain: Double?,
        /** wind speed, m/s */
        val speed: Double,
        val temperature: Temperature,
        val weather: Weather
) {
    companion object : JsonDeserializable<Forecast> {
        override fun fromJson(json: JSONObject): Forecast = with(json) {
            Forecast(
                    clouds = getInt("clouds"),
                    windDirection = getInt("deg"),
                    date = Date(getLong("dt") * 1000),
                    humidity = getInt("humidity"),
                    pressure = getDouble("pressure"),
                    rain = null rescue { getDouble("rain") },
                    speed = getDouble("speed"),
                    temperature = Temperature.fromJson(getJSONObject("temp")),
                    weather = Weather.fromJson(getJSONArray("weather").getJSONObject(0)))
        }
    }
}

data class Temperature(
        val day: Double,
        val evening: Double,
        val min: Double,
        val max: Double,
        val morning: Double,
        val night: Double
) {
    companion object : JsonDeserializable<Temperature> {
        override fun fromJson(json: JSONObject): Temperature = with(json) {
            Temperature(
                    day = getDouble("day"),
                    evening = getDouble("eve"),
                    min = getDouble("min"),
                    max = getDouble("max"),
                    morning = getDouble("morn"),
                    night = getDouble("night"))
        }
    }
}

data class Weather (
        /** single word weather description */
        val category: String,
        /** more specific weather description */
        val description: String
) {
    companion object : JsonDeserializable<Weather> {
        override fun fromJson(json: JSONObject): Weather = Weather(
                category = json.getString("main"),
                description = json.getString("description"))
    }
}
