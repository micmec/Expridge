package it.centotrenta.expridge

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

/**
 * Created by michelangelomecozzi on 05/08/2017.
 *
 * 130 si volaa!
 */
class AlarmActivity : AppCompatActivity(){

    lateinit var spinner: Spinner
    companion object {
        val MY_PREFS_NAME = "MyPrefsFile"
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        spinner = findViewById(R.id.spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.minutes_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        val id = pref.getInt("idAlarm", 0)
        spinner.setSelection(id)

        spinner.selectedItem

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                val pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
                val editor = pref.edit()
                if (parent.selectedItem.toString() != null) {
                    try {
                        val selection = parent.selectedItem.toString()
                        val hours = Integer.parseInt(selection.replace("[\\D]".toRegex(), ""))
                        editor.putInt("alarmValue", hours)
                        editor.putInt("idAlarm", position)
                        editor.apply()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    editor.putInt("alarmValue", 86400000)
                    editor.apply()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                val pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.putInt("alarmValue", 86400000)
                editor.apply()
            }
        }
    }

}