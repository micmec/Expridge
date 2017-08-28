package it.centotrenta.expridge.Utilities

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import android.content.Context
import it.centotrenta.expridge.MainActivity
import java.util.*

class GetItemCallback(private var rowItems: ArrayList<RowItem>, var context: Context) : Callback{


    override fun onResponse(call: Call?, response: Response?) {
        var items : List<Item> = Gson().fromJson(response!!.body()!!.string(), Array<Item>::class.java).toList()
        items.forEach {item -> rowItems.add(RowItem(context, item)) }
        MainActivity.updateTest()
    }

    override fun onFailure(call: Call?, response: IOException?) {
        println("Failed")
    }

}