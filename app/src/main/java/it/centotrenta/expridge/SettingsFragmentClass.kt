package it.centotrenta.expridge

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import android.widget.Toast


/**
 * Created by michelangelomecozzi on 06/08/2017.
 *
 * 130 si volaa!
 */

class SettingsFragmentClass: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.preferences)
        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen
        val count = prefScreen.preferenceCount
        for (i in 0 until count) {
            val p = prefScreen.getPreference(i)
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                p.summary = value
            }
        }

        val preference = findPreference(getString(R.string.pref_hours_input_key))
        preference!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val error = Toast.makeText(context, "Please select a number for the hours", Toast.LENGTH_SHORT)

            // Double check that the preference is the size preference
            val sizeKey = getString(R.string.pref_hours_input_key)
            if (preference!!.key == sizeKey) {
                try {
                    val stringSize = Integer.parseInt(newValue as String)
                    // If the number is outside of the acceptable range, show an error.
                    if (stringSize <= 0 || stringSize>100) {
                        error.show()
                        return@OnPreferenceChangeListener false
                    }
                } catch (nfe: NumberFormatException) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    error.show()
                    return@OnPreferenceChangeListener false
                }

            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        /* Unregister the preference change listener */
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        /* Register the preference change listener */
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference(key)
        Log.d("PREFERENCE WAS CHANGED","PREFERENCE WAS CHANGED 2")
        if (null != preference) {
            if (preference !is CheckBoxPreference) {
                preference.summary = sharedPreferences.getString(key, "")
            }
        }
    }

//    override fun onPreferenceChange(preference: android.preference.Preference?, newValue: Any?): Boolean {
//        val error = Toast.makeText(context, "Please select a number for the hours", Toast.LENGTH_SHORT)
//
//        Log.d("PREFERENCE WAS CHANGED","PREFERENCE WAS CHANGED")
//        // Double check that the preference is the size preference
//        val sizeKey = getString(R.string.pref_hours_input_key)
//        if (preference!!.key == sizeKey) {
//            try {
//                val stringSize = Integer.parseInt(newValue as String)
//                // If the number is outside of the acceptable range, show an error.
//                if (stringSize <= 0 || stringSize>100) {
//                    error.show()
//                    return false
//                }
//            } catch (nfe: NumberFormatException) {
//                // If whatever the user entered can't be parsed to a number, show an error
//                error.show()
//                return false
//            }
//
//        }
//        return true
//    }

}