package ph.codeia.solshine.index

import android.app.Activity
import android.support.v7.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ph.codeia.solshine.IndexState
import ph.codeia.solshine.PerFeature
import ph.codeia.solshine.R
import ph.codeia.solshine.shell.ShellContract
import javax.inject.Inject

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
    fun listView(a: Activity): RecyclerView =
            a.findViewById(R.id.list_forecasts) as RecyclerView

    @Provides
    fun data(s: IndexState): MutableList<IndexContract.WeatherData> = s.items

    @Provides
    fun view(v: IndexView): IndexContract.Display = v

    @Provides
    fun actions(s: Shared): IndexContract.Interaction = s.presenter

    @Provides
    fun dataSync(s: Shared): IndexContract.Synchronization = s.presenter
}
