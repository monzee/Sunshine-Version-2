package ph.codeia.solshine.index

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import ph.codeia.solshine.R
import javax.inject.Inject
import javax.inject.Named

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class ForecastsFragment : Fragment() {
    @field:[Inject Named("forecast") JvmField]
    internal var rv: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater?.inflate(R.layout.fragment_forecasts, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.index, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rv = null
    }
}
