package ph.codeia.solshine.index

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ph.codeia.solshine.IndexState
import ph.codeia.solshine.PerFeature
import ph.codeia.solshine.R
import ph.codeia.solshine.shell.ShellContract
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class IndexWiring(
        @get:Provides internal val navigator: ShellContract.Navigation,
        @get:Provides internal val messenger: ShellContract.Feedback
) {

    @[PerFeature Subcomponent(modules = arrayOf(IndexWiring::class))]
    interface Injector {
        fun inject(f: ForecastsFragment): ForecastsFragment
    }

    @PerFeature
    class Shared @Inject constructor(internal val presenter: IndexPresenter)

    @Provides
    fun listView(
            a: Activity, fa: ForecastsAdapter, lm: LinearLayoutManager
    ): RecyclerView = (a.findViewById(R.id.list_forecasts) as RecyclerView).apply {
        layoutManager = lm
        adapter = fa
    }

    @[Provides Named("forecasts")]
    fun data(s: IndexState): MutableList<IndexContract.WeatherData> = s.items

    @[Provides Named("forecasts_stale")]
    fun freshness(s: IndexState): AtomicBoolean = s.isStale

    @[Provides Named("forecasts_pending")]
    fun pending(s: IndexState): AtomicBoolean = s.isPending

    @Provides
    fun view(v: IndexView): IndexContract.Display = v

    @Provides
    fun actions(s: Shared): IndexContract.Interaction = s.presenter

    @Provides
    fun dataSync(s: Shared): IndexContract.Synchronization = s.presenter
}
