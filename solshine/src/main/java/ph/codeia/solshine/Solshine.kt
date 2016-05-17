package ph.codeia.solshine

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ph.codeia.solshine.index.IndexWiring
import ph.codeia.solshine.shell.ShellWiring
import javax.inject.Singleton

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class Solshine : Application() {

    companion object {
        lateinit var injector: Injector
            private set

        fun injector(a: Activity): ActivityInjector =
                injector.activity(ActivityWiring(a))

        @Module
        class ActivityWiring(val activity: Activity) {
            @Provides
            fun activity(): Activity = activity

            @Provides
            fun linear(): LinearLayoutManager = LinearLayoutManager(activity)

            @[Provides PerActivity]
            fun inflater(): LayoutInflater = LayoutInflater.from(activity)
        }
    }

    @[Singleton Component(modules = arrayOf(Solshine::class))]
    interface Injector {
        fun activity(a: ActivityWiring): ActivityInjector
    }

    @[PerActivity Subcomponent(modules = arrayOf(ActivityWiring::class))]
    interface ActivityInjector {
        fun shell(s: ShellWiring): ShellWiring.Injector
        fun index(i: IndexWiring): IndexWiring.Injector
    }

    override fun onCreate() {
        super.onCreate()
        injector = DaggerSolshine_Injector.create()
    }

    @Provides
    fun context(): Context = this
}

