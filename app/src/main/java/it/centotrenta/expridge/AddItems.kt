package it.centotrenta.expridge

import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.CalendarView
import android.widget.EditText
import it.centotrenta.expridge.Utilities.DBHandler

/**
 * Created by michelangelomecozzi on 05/08/2017.
 *
 * 130 si volaa!
 */
class AddItems : AppCompatActivity() ,View.OnClickListener{

    private var nameInput: EditText? = null
    private var dateInput: CalendarView? = null
    private var dbHandler: DBHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_add_items)

        dbHandler = MainActivity.dataBaseHandler
        nameInput = findViewById(R.id.name_input) as EditText
        dateInput = findViewById(R.id.calendarView) as CalendarView
        dateInput!!.setOnDateChangeListener { view, year, month, dayOfMonth ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val c = Calendar.getInstance()
                c.set(year, month, dayOfMonth)
                view.date = c.timeInMillis

            }
        }

    }

    override fun onClick(v: View) {
        val name = nameInput!!.text.toString()
        val dateMill = dateInput!!.date
        dbHandler!!.addItem(name, dateMill)
        MainActivity.mAdapter.addToClickList()
        MainActivity.mAdapter.addAnimationsToItem(getApplicationContext())
        super.onBackPressed()
    }
}