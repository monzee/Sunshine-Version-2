package ph.codeia.solshine.openweathermap

import android.content.res.Resources
import android.util.Log
import ph.codeia.solshine.R
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class FileFixture @Inject constructor(
        val resources: Resources,
        @Named("worker") override val background: Executor,
        @Named("ui") override val foreground: Executor
) : OwmService {

    override fun fetchForecastSync(apiKey: String, location: String, days: Int): String {
        Log.d("mz", "reading file!")
        return resources.openRawResource(R.raw.forecast_fixture).use {
            it.reader().readText()
        }
    }
}
