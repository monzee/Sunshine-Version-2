package ph.codeia.solshine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellContract.Feature
import ph.codeia.solshine.shell.ShellWiring
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {
    @Inject
    internal lateinit var go: ShellContract.Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        Solshine.injector(this).shell(ShellWiring(0)).inject(this)
        (findViewById(R.id.some_label) as TextView).apply {
            text = intent.getStringExtra("location")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.do_launch_settings -> go.launch(ShellContract.SETTINGS)
        else -> super.onOptionsItemSelected(item)
    }

}
