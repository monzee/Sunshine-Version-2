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
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class IndexWiring(
        @get:Provides internal val navigator: ShellContract.Navigation,
        @get:Provides internal val messenger: ShellContract.Messaging
) {

    @[PerFeature Subcomponent(modules = arrayOf(IndexWiring::class))]
    interface Injector {
        fun inject(f: ForecastsFragment): ForecastsFragment
    }

    @[Provides Named("forecasts")]
    fun listView(a: Activity, adapter: ForecastsAdapter, lm: LinearLayoutManager): RecyclerView {
        return (a.findViewById(R.id.list_forecasts) as RecyclerView).apply {
            layoutManager = lm
            this.adapter = adapter
        }
    }

    @[Provides Named("forecasts")]
    fun data(s: IndexState): MutableList<IndexContract.WeatherData> = s.items

    @[Provides Named("forecasts_stale")]
    fun freshness(s: IndexState): AtomicBoolean = s.isStale

    @[Provides Named("forecasts_pending")]
    fun pending(s: IndexState): AtomicBoolean = s.pending

    @Provides
    fun view(v: IndexView): IndexContract.Display = v

    @Provides
    fun actions(p: IndexPresenter): IndexContract.Interaction = p

    @Provides
    fun dataProvision(p: IndexPresenter): IndexContract.Synchronization = p
}
