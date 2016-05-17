package ph.codeia.solshine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ph.codeia.solshine.index.ForecastsFragment
import ph.codeia.solshine.index.IndexWiring
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellWiring
import java.util.*
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var injector: Solshine.ActivityInjector
    private val toInject: Queue<Fragment> = ArrayDeque()

    @Inject
    internal lateinit var go: ShellContract.Navigation

    @Inject
    internal lateinit var msg: ShellContract.Messaging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injector = Solshine.injector(this)
        injector.shell(ShellWiring(supportFragmentManager, R.id.container)).inject(this)
        if (savedInstanceState == null) {
            go.home()
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        toInject.add(fragment)
    }

    override fun onResumeFragments() {
        while (!toInject.isEmpty()) {
            inject(toInject.remove())
        }
        super.onResumeFragments()
    }

    private fun inject(f: Fragment) {
        when (f) {
            is ForecastsFragment -> injector.index(IndexWiring(go, msg)).inject(f)
            else -> {}
        }
    }
}
