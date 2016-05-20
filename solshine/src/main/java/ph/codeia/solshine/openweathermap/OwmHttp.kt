package ph.codeia.solshine.openweathermap

import android.util.Log
import ph.codeia.solshine.TempUnits
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class OwmHttp @Inject constructor(
        @Named("worker") override val background: Executor?,
        @Named("ui") override val foreground: Executor?
) : OwmService {

    override fun fetchForecastsSync(
            apiKey: String,
            location: String,
            days: Int,
            @TempUnits units: String
    ): String {
        val query = "mode=json&cnt=$days&q=$location&appid=$apiKey".let {
            when (units) {
                "standard" -> it
                else -> "$it&units=$units"
            }
        }
        Log.d("mz", query)
        val url = URL("http://api.openweathermap.org/data/2.5/forecast/daily?$query")
        return (url.openConnection() as HttpURLConnection).run {
            Log.i("mz", "(online) fetching via http...")
            try {
                requestMethod = "GET"
                connect()
                inputStream.bufferedReader().use {
                    it.readText()
                }
            } catch (e: IOException) {
                Log.e("mz/HUC/InputStream", "io error", e)
                ""
            } finally {
                disconnect()
            }
        }
    }

}
