package ph.codeia.solshine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import ph.codeia.solshine.index.ForecastsFragment
import ph.codeia.solshine.index.IndexWiring
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellWiring
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class MainActivity : AppCompatActivity(), Injector<Fragment> {
    private lateinit var injector: Solshine.ActivityInjector

    @Inject
    internal lateinit var go: ShellContract.Navigation

    @Inject
    internal lateinit var msg: ShellContract.Feedback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injector = Solshine.injector(this)
        injector.shell(ShellWiring(R.id.container)).inject(this)
        savedInstanceState ?: go.home()  // should be parameterized
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = true.apply {
        menuInflater.inflate(R.menu.main, menu)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.do_launch_settings -> go.launch(ShellContract.SETTINGS)
        else -> super.onOptionsItemSelected(item)
    }

    override fun inject(injectable: Fragment): Boolean = when (injectable) {
        is ForecastsFragment -> true.apply {
            injector.index(IndexWiring(go, msg)).inject(injectable)
        }
        is SettingsFragment -> true.apply {
            injector.inject(injectable)
        }
        else -> false
    }
}
