package ph.codeia.solshine.openweathermap

import android.content.res.Resources
import android.util.Log
import ph.codeia.solshine.R
import ph.codeia.solshine.TempUnits
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class OwmFixture @Inject constructor(
        private val resources: Resources,
        @Named("worker") override val background: Executor,
        @Named("ui") override val foreground: Executor
) : OwmService {

    override fun fetchForecastsSync(
            apiKey: String,
            location: String,
            days: Int,
            @TempUnits units: String
    ): String {
        Log.d("mz", "reading file!")
        return resources.openRawResource(R.raw.forecast_fixture).use {
            it.reader().readText()
        }
    }
}
