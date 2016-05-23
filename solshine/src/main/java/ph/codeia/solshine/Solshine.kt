package ph.codeia.solshine

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ph.codeia.solshine.index.IndexWiring
import ph.codeia.solshine.openweathermap.OwmFixture
import ph.codeia.solshine.openweathermap.OwmHttp
import ph.codeia.solshine.openweathermap.OwmService
import ph.codeia.solshine.shell.ShellWiring
import java.text.DateFormat
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton
import android.text.format.DateFormat as AndroidDateFormat

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
@Module
class Solshine : Application() {

    @[Singleton Component(modules = arrayOf(Solshine::class, DataSources::class))]
    interface Injector {
        fun activity(a: ActivityWiring): ActivityInjector
    }

    @[PerActivity Subcomponent(modules = arrayOf(ActivityWiring::class))]
    interface ActivityInjector {
        fun inject(f: SettingsFragment)
        fun shell(s: ShellWiring): ShellWiring.Injector
        fun index(i: IndexWiring): IndexWiring.Injector
    }

    companion object {
        lateinit var injector: Injector
            private set

        fun injector(a: AppCompatActivity): ActivityInjector =
                injector.activity(ActivityWiring(a))

        @Module
        class ActivityWiring(private val activity: AppCompatActivity) {
            @Provides
            fun context(): Activity = activity

            @Provides
            fun linear(): LinearLayoutManager = LinearLayoutManager(activity)

            @[Provides PerActivity]
            fun inflater(): LayoutInflater = LayoutInflater.from(activity)

            @Provides
            fun fragMan(): FragmentManager = activity.supportFragmentManager

            @Provides
            fun actionBar(): ActionBar? = activity.supportActionBar
        }

        @Module
        class DataSources {
            @[Provides Named("fixture")]
            fun owmFixture(s: OwmFixture): OwmService = s

            @Provides
            fun owm(s: OwmHttp): OwmService = s
        }
    }

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        injector = DaggerSolshine_Injector.builder().solshine(this).build()
    }

    @Provides
    fun context(): Context = this

    @[Provides Singleton Named("worker")]
    fun workerExecutor(): Executor = Executors.newCachedThreadPool()

    @[Provides Singleton Named("ui")]
    fun uiExecutor(): Executor {
        val mainLooper = Looper.getMainLooper()
        val uiThread = mainLooper.thread
        val handler = Handler(mainLooper)
        return Executor {
            if (uiThread == Thread.currentThread()) {
                it.run()
            } else {
                handler.post(it)
            }
        }
    }

    @[Provides Singleton]
    fun res(): Resources = resources

    @Provides
    fun prefs(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    @[Provides Named("medium")]
    fun dateFormatMedium(): DateFormat = AndroidDateFormat.getMediumDateFormat(this)
}
