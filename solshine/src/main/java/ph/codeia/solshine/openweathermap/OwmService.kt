package ph.codeia.solshine.openweathermap

import android.util.Log
import org.json.JSONException
import ph.codeia.solshine.Result
import ph.codeia.solshine.Units
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
            @TempUnits units: String = Units.METRIC
    ): String

    fun fetchForecasts(
            apiKey: String,
            location: String,
            days: Int = 7,
            @TempUnits units: String = Units.METRIC,
            then: Result<Report>.() -> Unit
    ) {
        val barrier = CountDownLatch(1)
        val result = AtomicReference<Result<Report>>().apply {
            when {
                background == null -> set(Result.fail("need background executor"))
                foreground == null -> set(Result.fail("need foreground executor"))
            }
        }
        background?.let {
            it.execute {
                val json = fetchForecastsSync(apiKey, location, 7, units)
                result.set(when {
                    json.isNotEmpty() -> try {
                        Result.ok(Report.fromJson(json))
                    } catch (e: JSONException) {
                        Log.d("mz", json)
                        // TODO: should try to parse the json and get the error message
                        Result.fail<Report>(e)
                    }
                    else -> Result.fail("got a blank response. network error?")
                })
                barrier.countDown()
            }

            it.execute {
                if (!barrier.await(30, TimeUnit.SECONDS)) {
                    result.set(Result.fail("timeout"))
                }
                foreground?.execute {
                    result.get().then()
                }
            }
        }
    }
}

