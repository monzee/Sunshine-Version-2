package ph.codeia.solshine.openweathermap

import android.util.Log
import ph.codeia.solshine.Temp
import ph.codeia.solshine.TempUnits
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
interface OwmService {
    val background: Executor?
    val foreground: Executor?

    fun fetchForecastsSync(
            apiKey: String,
            location: String,
            days: Int,
            @TempUnits units: String = Temp.METRIC
    ): String

    fun fetchWeekForecast(apiKey: String, location: String, then: (Report?) -> Unit) {
        if (background == null || foreground == null) {
            then(null)
            return
        }

        val barrier = CountDownLatch(1)
        val result = AtomicReference<Report>()

        background?.let {
            it.execute {
                Log.d("mz", "(background) fetching | $apiKey | $location")
                fetchForecastsSync(apiKey, location, 7).let {
                    if (it.isNotEmpty()) {
                        result.set(Report.fromJson(it))
                    }
                }
                barrier.countDown()
            }

            it.execute {
                if (!barrier.await(30, TimeUnit.SECONDS)) {
                    Log.d("mz", "fetch timeout")
                }
                foreground?.execute {
                    then(result.get())
                }
            }
        }
    }
}

