package it.centotrenta.expridge

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import it.centotrenta.expridge.Utilities.DBHandler
import java.util.ArrayList

/**
 * Created by michelangelomecozzi on 05/08/2017.
 *
 * 130 si volaa!
 */
class ListItemsAdapter constructor(clickHandler: ListItemsAdapterClickHandler, context: Context) : RecyclerView.Adapter<ListItemsAdapter.ListAdapterViewHolder>() {

    var listClickHandler: ListItemsAdapterClickHandler = clickHandler
    var itemInformationName: ArrayList<String>? = null
    var itemInformationDate: ArrayList<String>? = null
    var itemInformationDateForNotification: ArrayList<Long>? = null
    private var itemInformationImage: ArrayList<Int>? = null
    var itemInformationId: ArrayList<Int>? = null
    var itemInformationClicked = ArrayList<Boolean>()
    var itemInformationSecondClicked = ArrayList<Boolean>()
    internal var itemNotificationClicked: ArrayList<Boolean>
    var itemAnimationsOpen = ArrayList<Animation>()
    var itemAnimationsClose = ArrayList<Animation>()
    var databaseHandler: DBHandler = MainActivity.dataBaseHandler

    interface ListItemsAdapterClickHandler {
        fun onClick(view: View, id: Int, itemInfoName: String, time: Long, deleteButton: ImageView, alarmButton: ImageView, dateView: TextView,
                    dateFormatted: String, pos: Int)
    }

    init {
        itemInformationName = databaseHandler.namesFromColumn
        itemInformationDate = databaseHandler.datesFromColumn
        itemInformationDateForNotification = databaseHandler.datesLongFromColumn
        itemInformationImage = getRelativeImages(itemInformationName)
        itemInformationId = databaseHandler.idFromColumn
        itemNotificationClicked = databaseHandler.notificationFromColumn
        itemInformationClicked = ArrayList<Boolean>()
        itemInformationSecondClicked = ArrayList<Boolean>()
        itemAnimationsOpen = ArrayList<Animation>()
        itemAnimationsClose = ArrayList<Animation>()

        // We recreate an array of booleans for the click handling every time we construct the adapter
        for (i in itemInformationName!!.indices) {
            itemInformationClicked.add(false)
            itemInformationSecondClicked.add(false)
            itemAnimationsOpen.add(AnimationUtils.loadAnimation(context, R.anim.opening_animation))
            itemAnimationsClose.add(AnimationUtils.loadAnimation(context, R.anim.closing_animation))
        }
    }

    inner class ListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val listItemName: TextView = view.findViewById(R.id.name_of_food) as TextView
        val listItemDate: TextView = view.findViewById(R.id.date_of_food) as TextView
        val listItemImage: ImageView = view.findViewById(R.id.food_image) as ImageView
        val deleteButton: ImageView = view.findViewById(R.id.delete_button) as ImageView
        val alarmButton: ImageView = view.findViewById(R.id.alarm_button) as ImageView
        val roboto = MainActivity.font


        init {

            view.setOnClickListener(this)
            listItemDate.typeface = roboto
            listItemName.typeface = roboto

        }

        override fun onClick(v: View) {

            val adapterPosition = adapterPosition
            val dateForNotification = itemInformationDateForNotification!![adapterPosition]
            val itemName = itemInformationName!![adapterPosition]
            val dateFormatted = itemInformationDate!![adapterPosition]
            val id = itemInformationId!![adapterPosition]
            val pos = itemInformationId!!.indexOf(id)
            listClickHandler.onClick(v, id, itemName, dateForNotification, deleteButton, alarmButton, listItemDate,dateFormatted, pos)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterViewHolder {
        val context = parent.context
        val layoutId = R.layout.custom_row
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutId, parent, false)
        return ListAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListAdapterViewHolder, position: Int) {

        try {
            val itemName = itemInformationName!![position]
            val itemDate = itemInformationDate!![position]
            holder.listItemName.text = itemName
            holder.listItemDate.text = itemDate
            try {
                val itemImage = itemInformationImage!![position]
                holder.listItemImage.setImageResource(itemImage)
            } catch (e: Exception) {
                holder.listItemImage.setImageResource(R.drawable.unknown)
            }

        } catch (e: Exception) {

            e.printStackTrace()

        }

    }

    override fun getItemCount(): Int {
        if (itemInformationName == null) {
            return 0
        }
        return itemInformationName!!.size
    }

    // Create a method for assigning new data to the item, without creating a new ViewHolder TODO: we might need it in future
    fun setItemData(informationName: ArrayList<String>, informationDate: ArrayList<String>, informationImage: ArrayList<Int>) {
        itemInformationName = informationName
        itemInformationDate = informationDate
        itemInformationImage = informationImage
        notifyDataSetChanged()
    }

    private fun getRelativeImages(_itemNames: ArrayList<String>?): ArrayList<Int>? {
        val list = ArrayList<Int>()
        for (i in _itemNames!!.indices) {
            val _itemName = _itemNames[i]
            if (_itemName.contains("Cheese") || _itemName.contains("CHEESE") || _itemName.contains("cheese") ||
                    _itemName.contains("Milk") || _itemName.contains("MILK") || _itemName.contains("milk") ||
                    _itemName.contains("Yogurt") || _itemName.contains("YOGURT") || _itemName.contains("yogurt") ||
                    _itemName.contains("Yogurt") || _itemName.contains("YOGURT") || _itemName.contains("yogurt") ||
                    _itemName.contains("Cream") || _itemName.contains("CREAM") || _itemName.contains("cream")) {
                list.add(R.drawable.dairy)
            } else if (_itemName.contains("Meat") || _itemName.contains("MEAT") || _itemName.contains("meat") ||
                    _itemName.contains("Steak") || _itemName.contains("STEAK") || _itemName.contains("steak") ||
                    _itemName.contains("Ham") || _itemName.contains("HAM") || _itemName.contains("ham") ||
                    _itemName.contains("Bacon") || _itemName.contains("BACON") || _itemName.contains("bacon") ||
                    _itemName.contains("Chicken") || _itemName.contains("CHICKEN") || _itemName.contains("chicken") ||
                    _itemName.contains("Sausage") || _itemName.contains("SAUSAGE") || _itemName.contains("sausage") ||
                    _itemName.contains("Beef") || _itemName.contains("BEEF") || _itemName.contains("beef") ||
                    _itemName.contains("Chop") || _itemName.contains("CHOP") || _itemName.contains("chop") ||
                    _itemName.contains("Lamb") || _itemName.contains("LAMB") || _itemName.contains("lamb") ||
                    _itemName.contains("Burger") || _itemName.contains("BURGER") || _itemName.contains("burger") ||
                    _itemName.contains("Mutton") || _itemName.contains("MUTTON") || _itemName.contains("mutton") ||
                    _itemName.contains("Pork") || _itemName.contains("PORK") || _itemName.contains("pork") ||
                    _itemName.contains("Turkey") || _itemName.contains("TURKEY") || _itemName.contains("turkey") ||
                    _itemName.contains("Hot dog") || _itemName.contains("HOT DOG") || _itemName.contains("hot dog")) {
                list.add(R.drawable.meat)
            } else if (_itemName.contains("Pepper") || _itemName.contains("PEPPER") || _itemName.contains("pepper") ||
                    _itemName.contains("Salad") || _itemName.contains("SALAD") || _itemName.contains("salad") ||
                    _itemName.contains("Broccoli") || _itemName.contains("BROCCOLI") || _itemName.contains("broccoli") ||
                    _itemName.contains("Cabbage") || _itemName.contains("CABBAGE") || _itemName.contains("cabbage") ||
                    _itemName.contains("Carrot") || _itemName.contains("CARROT") || _itemName.contains("carrot") ||
                    _itemName.contains("Lettuce") || _itemName.contains("LETTUCE") || _itemName.contains("lettuce") ||
                    _itemName.contains("Onion") || _itemName.contains("ONION") || _itemName.contains("onion") ||
                    _itemName.contains("Spinach") || _itemName.contains("SPINACH") || _itemName.contains("spinach") ||
                    _itemName.contains("Tomato") || _itemName.contains("TOMATO") || _itemName.contains("tomato") ||
                    _itemName.contains("Zucchini") || _itemName.contains("ZUCCHINI") || _itemName.contains("zucchini")) {
                list.add(R.drawable.vedgetables)
            } else
            //Todo fish and if it is unknown might add it to the categories
            {
                list.add(R.drawable.unknown)
            }
        }
        return list
    }

    internal fun loadDB() {
        itemInformationName = databaseHandler.namesFromColumn
        itemInformationDate = databaseHandler.datesFromColumn
        itemInformationDateForNotification = databaseHandler.datesLongFromColumn
        itemInformationImage = getRelativeImages(itemInformationName)
        itemInformationId = databaseHandler.idFromColumn
        itemNotificationClicked = databaseHandler.notificationFromColumn
    }

    internal fun addToClickList() {

        itemInformationClicked.add(false)
        itemInformationSecondClicked.add(false)

    }

    internal fun addAnimationsToItem(context: Context) {

        itemAnimationsOpen.add(AnimationUtils.loadAnimation(context, R.anim.fab_open))
        itemAnimationsClose.add(AnimationUtils.loadAnimation(context, R.anim.fab_close))

    }
}