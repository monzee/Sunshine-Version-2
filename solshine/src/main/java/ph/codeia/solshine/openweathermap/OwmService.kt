package ph.codeia.solshine.openweathermap

import android.util.Log
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

    fun fetchForecastSync(apiKey: String, location: String, days: Int): String

    fun fetchWeekForecast(apiKey: String, location: String, then: (Report?) -> Unit) {
        if (background == null || foreground == null) {
            then(null)
            return
        }

        val barrier = CountDownLatch(1)
        val result = AtomicReference<Report>()

        background?.execute {
            Log.d("mz", "(background) fetching | $apiKey | $location")
            result.set(Report.fromJson(fetchForecastSync(apiKey, location, 7)))
            barrier.countDown()
        }

        Thread {
            barrier.await(30, TimeUnit.SECONDS)
            foreground?.execute {
                then(result.get())
            }
        }.start()
    }
}

