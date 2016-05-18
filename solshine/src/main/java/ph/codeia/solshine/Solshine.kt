package ph.codeia.solshine

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ph.codeia.solshine.index.IndexWiring
import ph.codeia.solshine.openweathermap.FileFixture
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellWiring
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named
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
        class ActivityWiring(@get:Provides internal val activity: Activity) {
            @Provides
            fun linear(): LinearLayoutManager = LinearLayoutManager(activity)

            @[Provides PerActivity]
            fun inflater(): LayoutInflater = LayoutInflater.from(activity)
        }

        @Module
        class DataSources {
            @[Provides Named("fixture")]
            fun owm(s: FileFixture): OwmService = s
        }
    }

    @[Singleton Component(modules = arrayOf(Solshine::class, DataSources::class))]
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
        injector = DaggerSolshine_Injector.builder().solshine(this).build()
    }

    @Provides
    fun context(): Context = this

    @[Provides Singleton Named("worker")]
    fun workerExecutor(): Executor = Executors.newSingleThreadExecutor()

    @[Provides Singleton Named("ui")]
    fun uiExecutor(): Executor {
        val handler = Handler(Looper.getMainLooper())
        return Executor {
            handler.post(it)
        }
    }

    @[Provides Singleton]
    fun res(): Resources = resources
}
