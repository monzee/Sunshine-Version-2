package ph.codeia.solshine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        (findViewById(R.id.some_label) as TextView).apply {
            text = intent.getStringExtra("location")
        }
    }
}
