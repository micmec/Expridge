package it.centotrenta.expridge

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.CommonStatusCodes
import it.centotrenta.expridge.OcrUtils.OcrCaptureActivity
import it.centotrenta.expridge.Utilities.DBHandler
import it.centotrenta.expridge.Utilities.NotificationBroadcast

/**
 * Created by michelangelomecozzi on 04/08/2017.
 *
 * 130 si volaa!
 */

//RICCARDO
//TODO install notifications when an item is expiring in the next 2 days
//TODO Name is too big font and bold, inconsistent with typing
//TODO Name of app to be changes
//TODO Add item screen shows settings label
//TODO Filter no empty text on manual add item
//TODO General UI Improvements
//TODO Color scheme for float buttons
//TODO Good design = simplistic fridge with shelves?
//TODO Apache License for Vision Library
//TODO make the button for adding go back and the two buttons on items go back after 5 seconds

//MICHELANGELO
//TODO BUG, notifications cancel each other
//TODO BUG, moving icon for notifications
//TODO add more settings or fill in the space of the setting part
//TODO dialog if he puts an item with double keyword
//TODO solved the multiple click issues, but we have a similar one with notifications and animations
//TODO Add the various food images
//TODO consider creating more icons for all the food (Big waste of time or not? questo e' il dilemma)
//TODO BUG, if we click on "manually add" multiple times it opens two activities for adding
//TODO BUG, animation bug, first time we animate the objects it keeps animating them all together; it does not do it the rest of the times
//TODO check if the new way of handling the insertion of items (which fixes the bug of multiple clicks) does not do useless implementations

class MainActivity : AppCompatActivity(), ListItemsAdapter.ListItemsAdapterClickHandler, View.OnClickListener {

    override fun onClick(v: View?) {

        setOtherViewsOff()
        fabOff()

    }

    private val RC_OCR_CAPTURE = 9003

    companion object {
        lateinit var dataBaseHandler: DBHandler
        lateinit var mAdapter: ListItemsAdapter
        lateinit var font: Typeface

    }
    private var mRecyclerView: RecyclerView? = null
    private var mErrorMessageView: TextView? = null
    private var mFab: FloatingActionButton? = null
    private var nFab: FloatingActionButton? = null
    private var fFab: FloatingActionButton? = null
    private var mNoItems: TextView? = null
    private var FabOpen: Animation? = null
    private var FabClose: Animation? = null
    private var FabRotClock: Animation? = null
    private var FabRotAnti: Animation? = null
    internal var isOpen = false
    val MY_PREFS_NAME = "MyPrefsFile"
    private var arrowNoItems: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.list_activity_recyclerView) as RecyclerView
        mErrorMessageView = findViewById(R.id.list_activity_errorMessage) as TextView
        mNoItems = findViewById(R.id.no_items_view) as TextView
        dataBaseHandler = DBHandler(this)
        arrowNoItems = findViewById(R.id.arrow_no_items) as ImageView

        //TODO transfer this to the adapter for the animations
        FabOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        FabClose = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)
        FabRotClock = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate)
        FabRotAnti = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_rotate_back)

        mFab = findViewById(R.id.red_button) as FloatingActionButton
        nFab = findViewById(R.id.manually_button) as FloatingActionButton
        fFab = findViewById(R.id.photo_button) as FloatingActionButton
        mFab!!.setOnClickListener({
            if (isOpen) {
                fabOff()
            } else {
                fabOn()
            }
        })

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView!!.layoutManager = layoutManager
        mRecyclerView!!.setHasFixedSize(true)

        mAdapter = ListItemsAdapter(this, applicationContext)
        mRecyclerView!!.adapter = mAdapter

        font = Typeface.createFromAsset(assets,"Roboto-Regular.ttf")

        noItemsMethod()
    }

    override fun onResume() {

        super.onResume()

        mAdapter.loadDB()

        for (i in mAdapter.itemInformationClicked.indices) {
            mAdapter.itemInformationClicked[i] = false
            mAdapter.itemInformationSecondClicked[i] = false
        }
        mRecyclerView!!.adapter = mAdapter

        noItemsMethod()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.add_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.add_item_menu) {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View, id: Int, itemInfoName: String, time: Long,
                         deleteButton: ImageView, alarmButton: ImageView, dateFormatted: String, pos: Int) {

        if (mAdapter.itemInformationClicked[pos]) {

            closingAnimation(deleteButton,alarmButton,pos)

        } else {

            setOtherViewsOff()
            openingAnimation(deleteButton,alarmButton,pos)

        }

        if (mAdapter.itemInformationSecondClicked[pos]) {

            deleteButton.setOnClickListener { view ->
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> deletePartOfTheMethod(id, pos)

                    }
                }

                val builder = AlertDialog.Builder(view.context)
                builder.setMessage("Are you sure you want to delete $itemInfoName?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show()
            }

            alarmButton.setOnClickListener {
                if (!mAdapter.itemNotificationClicked[pos]) {

                    alarmButton.setImageResource(R.drawable.notifications_off)
                    setNotification(this, time, id, itemInfoName, dateFormatted)
                    mAdapter.itemNotificationClicked[pos] = true


                } else {

                    alarmButton.setImageResource(R.drawable.notifications)
                    deleteNotification(this, id, itemInfoName)
                    mAdapter.itemNotificationClicked[pos] = false

                }
            }
            mAdapter.itemInformationSecondClicked[pos] = false
        }
    }

    fun deletePartOfTheMethod(id: Int, position: Int) {
        dataBaseHandler.deleteItem(id)
        mAdapter.itemInformationClicked.removeAt(position)
        mAdapter.itemInformationSecondClicked.removeAt(position)
        mAdapter.loadDB()
        for (i in mAdapter.itemInformationClicked.indices) {
            mAdapter.itemInformationClicked[i] = false
            mAdapter.itemInformationSecondClicked[i] = false
        }
        mRecyclerView!!.adapter = mAdapter
        noItemsMethod()

    }

    fun redButtonAction(view: View) {
        val intent = Intent(this, AddItems::class.java)
        startActivity(intent)

    }

    private fun showItemsDataView() {
        mErrorMessageView!!.visibility = View.INVISIBLE
        mNoItems!!.visibility = View.INVISIBLE
        arrowNoItems!!.visibility = View.INVISIBLE
        mRecyclerView!!.visibility = View.VISIBLE
    }

    private fun noItemsMethod() {
        if (dataBaseHandler.isDBNil) {
            mRecyclerView!!.visibility = View.INVISIBLE
            mNoItems!!.visibility = View.VISIBLE
            arrowNoItems!!.visibility = View.VISIBLE
        } else {
            showItemsDataView()
        }
    }

    fun photoFloatClick(view: View) {
        val intent = Intent(this, OcrCaptureActivity::class.java)
        startActivityForResult(intent, RC_OCR_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_OCR_CAPTURE && resultCode == CommonStatusCodes.SUCCESS && data != null) {
            val text = data.getStringExtra(OcrCaptureActivity.TextBlockObject)
            Log.d("TEXT THAT I GOT WAS", text)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun setNotification(context: Context, time: Long, id: Int, itemName: String, dateFormatted: String) {

        val pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        val howBefore = pref.getInt("alarmValue", 8400000)

        val notifyIntent = Intent(context, NotificationBroadcast::class.java)
        notifyIntent.putExtra("id", id)
        notifyIntent.putExtra("itemName", itemName)
        notifyIntent.putExtra("itemDate", dateFormatted)
        val pendingIntent = PendingIntent.getBroadcast(context, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, time - howBefore, pendingIntent)

        Toast.makeText(this, "Notification on for " + itemName, Toast.LENGTH_SHORT).show()

        //TODO bug, create an if statement for avoiding notifications in cases of selecting the same day or the next one

    }

    fun deleteNotification(context: Context, id: Int, itemName: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyIntent = Intent(context, NotificationBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Notification off for " + itemName, Toast.LENGTH_SHORT).show()
    }

    fun openingAnimation(deleteButton: ImageView,alarmButton: ImageView,pos: Int){

        deleteButton.startAnimation(mAdapter.itemAnimationsOpen[pos])
        alarmButton.startAnimation(mAdapter.itemAnimationsOpen[pos])
        deleteButton.isClickable = true
        alarmButton.isClickable = true
        if (!mAdapter.itemNotificationClicked[pos]) {
            alarmButton.setImageResource(R.drawable.notifications)
        } else {
            alarmButton.setImageResource(R.drawable.notifications_off)
        }
        deleteButton.setImageResource(R.drawable.cancel)
        mAdapter.itemInformationClicked[pos] = true
        mAdapter.itemInformationSecondClicked[pos] = true
    }

    fun closingAnimation(deleteButton: ImageView,alarmButton: ImageView,pos: Int){

        deleteButton.startAnimation(mAdapter.itemAnimationsClose[pos])
        alarmButton.startAnimation(mAdapter.itemAnimationsClose[pos])
        if (!mAdapter.itemNotificationClicked[pos]) {
            alarmButton.setImageResource(R.drawable.notifications)
        } else {
            alarmButton.setImageResource(R.drawable.notifications_off)
        }
        deleteButton.setImageResource(R.drawable.cancel)
        deleteButton.isClickable = false
        alarmButton.isClickable = false
        mAdapter.itemInformationClicked[pos] = false
    }

    fun setOtherViewsOff(){

        for(i in mAdapter.itemInformationName!!.indices){

            val view = mRecyclerView!!.findViewHolderForAdapterPosition(i).itemView

            if(mAdapter.itemInformationClicked[i]) {
                val deleteButton: ImageView = view.findViewById(R.id.delete_button) as ImageView
                val alarmButton: ImageView = view.findViewById(R.id.alarm_button) as ImageView
                closingAnimation(deleteButton,alarmButton,i)
            }
        }
    }

    fun fabOn(){
        nFab!!.startAnimation(FabOpen)
        fFab!!.startAnimation(FabOpen)
        mFab!!.startAnimation(FabRotClock)
        nFab!!.isClickable = true
        fFab!!.isClickable = true
        isOpen = true
    }

    fun fabOff(){
        nFab!!.startAnimation(FabClose)
        fFab!!.startAnimation(FabClose)
        mFab!!.startAnimation(FabRotAnti)
        nFab!!.isClickable = false
        fFab!!.isClickable = false
        isOpen = false
    }

}
