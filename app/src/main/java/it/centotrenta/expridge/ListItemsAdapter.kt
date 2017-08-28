package it.centotrenta.expridge

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import it.centotrenta.expridge.Utilities.GetItemCallback
import it.centotrenta.expridge.Utilities.Item
import it.centotrenta.expridge.Utilities.RowItem
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.*
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.ArrayList

class ListItemsAdapter constructor(clickHandler: ListItemsAdapterClickHandler, context: Context) : RecyclerView.Adapter<ListItemsAdapter.ListAdapterViewHolder>() {

    var listClickHandler: ListItemsAdapterClickHandler = clickHandler

    val api: OkHttpClient = OkHttpClient()
    var rowItems: ArrayList<RowItem> = ArrayList()


    interface ListItemsAdapterClickHandler {
        fun onClick(view: View,
                    id: Int,
                    itemInfoName: String,
                    time: Long,
                    deleteButton: ImageView,
                    alarmButton: ImageView,
                    dateView: TextView,
                    dateFormatted: String,
                    pos: Int)
    }

    init {

        async(UI){ //
            var result = bg { OkHttpClient().newCall(Request.Builder().url("http://82.58.168.147:3997/fridge/4/fetchAllItems").get().build()).execute() }
            val res = result.await()
            updateUIafterInit(res)
        }


    }

    private fun updateUIafterInit(result: Response){
        val content = result.body()
        val str = content!!.string()
        val items = Gson().fromJson(str, Array<Item>::class.java).toList()
        //items.forEach {item -> rowItems.add(RowItem(, item)) }
        MainActivity.mAdapter.notifyDataSetChanged()
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
            val dateForNotification = rowItems[adapterPosition].item.date
            val itemName = rowItems[adapterPosition].item.name
            val dateFormatted = rowItems[adapterPosition].dateFormatted
            val id = rowItems[adapterPosition].item.id.toInt()
            listClickHandler.onClick(v, id, itemName, dateForNotification, deleteButton, alarmButton, listItemDate, dateFormatted, adapterPosition)

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
        holder.listItemName.text = rowItems[position].item.name
        holder.listItemDate.text = rowItems[position].dateFormatted
        holder.listItemImage.setImageResource(rowItems[position].imageResource)
    }

    override fun getItemCount(): Int = rowItems.size

}