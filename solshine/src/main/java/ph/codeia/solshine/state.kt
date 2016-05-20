package ph.codeia.solshine

import ph.codeia.solshine.index.IndexContract
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Singleton
class IndexState @Inject constructor() {
    var isStale: Boolean = true
    var isPending: Boolean = false
    var coords: Pair<Double, Double>? = null
    val items: MutableList<IndexContract.WeatherData> = mutableListOf()
}

@Singleton
class FragmentStackState @Inject constructor() {
    var nextTitle: String? = null
    val titles: Deque<String> = ArrayDeque()
}
