package ph.codeia.solshine.index

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import ph.codeia.solshine.Injector
import ph.codeia.solshine.R
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
            savedInstanceState: Bundle?
    ): View? = inflater?.inflate(R.layout.fragment_forecasts, container, false)

    override fun onResume() {
        super.onResume()
        @Suppress("UNCHECKED_CAST")
        (activity as? Injector<Fragment>)?.inject(this)
        setHasOptionsMenu(true)
        presenter.bind(view)
        presenter.fetchForecasts()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.index, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.do_refresh -> true.apply { user.didPressRefresh() }
        R.id.do_view_map -> true.apply { user.didPressViewMap() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.bind(null)
    }
}
