package ph.codeia.solshine.index

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.Subcomponent
import ph.codeia.solshine.IndexState
import ph.codeia.solshine.R
import ph.codeia.solshine.shell.ShellContract
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class IndexWiring(val navigator: ShellContract.Navigation) {

    @Subcomponent(modules = arrayOf(IndexWiring::class))
    interface Injector {
        fun inject(f: ForecastsFragment): ForecastsFragment
    }

    @[Provides Named("forecast")]
    fun listView(a: Activity, adapter: ForecastsAdapter, lm: LinearLayoutManager): RecyclerView {
        return (a.findViewById(R.id.list_forecasts) as RecyclerView).apply {
            layoutManager = lm
            this.adapter = adapter
        }
    }

    @[Provides Named("forecast")]
    fun data(s: IndexState): MutableList<IndexContract.WeatherData> = s.items

    @[Provides Reusable]
    fun view(v: IndexView): IndexContract.Display = v

    @[Provides Reusable]
    fun actions(p: IndexPresenter): IndexContract.Interaction = p

    @[Provides Reusable]
    fun dataProvision(p: IndexPresenter): IndexContract.Synchronization = p

    @Provides
    fun nav(): ShellContract.Navigation = navigator
}
