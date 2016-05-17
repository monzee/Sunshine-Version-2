package ph.codeia.solshine

import ph.codeia.solshine.index.IndexContract
import ph.codeia.solshine.index.Weather
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Singleton
class IndexState @Inject constructor() {
    val isStale: AtomicBoolean = AtomicBoolean(true)

    var items: MutableList<IndexContract.WeatherData> = mutableListOf(
            Weather("Abc", "sunny", Date(), 42, 82),
            Weather("dEf", "rainy", Date(), 61, 32),
            Weather("ghI", "cloudy", Date(), 34, 12),
            Weather("Jkl", "foggy", Date(), 26, 62),
            Weather("mNo", "snowy", Date(), 22, 42)
    )
}
