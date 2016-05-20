package ph.codeia.solshine

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.Menu
import android.view.MenuInflater
import javax.inject.Inject

/**
 * This file is a part of the Sunshine-Version-2 project.
 */
class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    @Inject
    internal lateinit var preferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onResume() {
        super.onResume()
        (activity as? SuperclassInjector<Fragment>)?.inject(this)
        setHasOptionsMenu(true)
        preferences.registerOnSharedPreferenceChangeListener(this)
        listOf("location", "units").forEach { onSharedPreferenceChanged(preferences, it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        findPreference(key).let {
            it.summary = prefs?.getString(key, when (key) {
                "location" -> "Manila"
                "units" -> "metric"
                else -> ""
            })
        }
    }
}