package ph.codeia.solshine.index

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import ph.codeia.solshine.R
import java.util.*
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class ForecastsFragment : Fragment() {
    @Inject
    internal lateinit var presenter: IndexContract.Synchronization

    @Inject
    internal lateinit var user: IndexContract.Interaction

    @Inject
    internal lateinit var view: IndexContract.Display

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_forecasts, container, false)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        presenter.bind(view)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.index, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.do_refresh -> user.didPressRefresh()
            R.id.do_add -> presenter.gotForecast(listOf(Weather("new!", "new!!!", Date(), 10.0, 100.0)))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.bind(null)
    }
}
