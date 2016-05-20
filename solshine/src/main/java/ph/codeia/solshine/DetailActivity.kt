package ph.codeia.solshine

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import ph.codeia.solshine.shell.ShellContract
import ph.codeia.solshine.shell.ShellWiring
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {
    @Inject
    internal lateinit var go: ShellContract.Navigation

    @Inject
    internal lateinit var log: ShellContract.Feedback

    private var label: TextView? = null
    private var shareActionProvider: ShareActionProvider? = null
    private lateinit var shareText: String
    private val shareIntent: Intent
        get () = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_TEXT, Html.fromHtml("$shareText #Sunshine"))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        Solshine.injector(this).shell(ShellWiring(0)).inject(this)
        label = (findViewById(R.id.some_label) as TextView).apply {
            val loc = intent.getStringExtra("location")
            val date = intent.getStringExtra("date")
            val status = intent.getStringExtra("status")
            val minTemp = intent.getDoubleExtra("min", 0.0)
            val maxTemp = intent.getDoubleExtra("max", 0.0)
            val s = "$date &mdash; $loc &mdash; $status &mdash; $minTemp / $maxTemp"
            text = Html.fromHtml(s)
            shareText = "$s #Solshine"
        }
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = true.apply {
        menuInflater.inflate(R.menu.details, menu)
        menu?.findItem(R.id.do_share)?.let {
            shareActionProvider = MenuItemCompat.getActionProvider(it) as? ShareActionProvider
            shareActionProvider?.setShareIntent(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.do_share -> true.apply {
            shareActionProvider?.setShareIntent(shareIntent)
        }
        else -> super.onOptionsItemSelected(item)
    }

}
