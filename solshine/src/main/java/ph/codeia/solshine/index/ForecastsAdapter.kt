package ph.codeia.solshine.index

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ph.codeia.solshine.R
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class ForecastsAdapter @Inject constructor(
    @param:Named("forecast") val items: MutableList<IndexContract.WeatherData>,
    // val user: IndexContract.Interaction,
    val inflater: LayoutInflater
) : RecyclerView.Adapter<ForecastsAdapter.WeatherView>() {

    class WeatherView(view: View) : RecyclerView.ViewHolder(view) {
        private val label: TextView = view as TextView

        var text: String
            get() = label.text.toString()
            set(value) { label.text = Html.fromHtml(value) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WeatherView? {
        val view = inflater.inflate(R.layout.item_forecast, parent, false)
        val holder = WeatherView(view)
        view.setOnClickListener {
            // user.didChooseItem(holder.getAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: WeatherView?, position: Int) {
        val w = items[position]
        holder?.text = "%s &mdash; %s &mdash; %d/%d".format(w.location, w.status, w.temperature, w.humidity)
    }

    override fun getItemCount(): Int = items.size
}
