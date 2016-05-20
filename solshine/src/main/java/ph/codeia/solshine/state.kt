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

    val isPending: AtomicBoolean = AtomicBoolean(false)

    val items: MutableList<IndexContract.WeatherData> = mutableListOf(
            Weather("Abc", "sunny", Date(), 42.0, 82.0),
            Weather("dEf", "rainy", Date(), 61.0, 32.0),
            Weather("ghI", "cloudy", Date(), 34.0, 12.0),
            Weather("Jkl", "foggy", Date(), 26.0, 62.0),
            Weather("mNo", "snowy", Date(), 22.0, 42.0)
    )
}

@Singleton
class FragmentStackState @Inject constructor() {
    var nextTitle: String? = null
    val titles: Deque<String> = ArrayDeque()
}