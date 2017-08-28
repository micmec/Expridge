package it.centotrenta.expridge.Utilities

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import it.centotrenta.expridge.R
import java.text.SimpleDateFormat
import java.util.*


class RowItem(context: Context, var item: Item) {
    var dateFormatted: String = toDate(item.date, "dd/MM/yyyy")
    var firstClick: Boolean = false
    var secondClick: Boolean = false
    var imageResource: Int = imageIdToRes(item.imageId)
    var animationOpen: Animation = AnimationUtils.loadAnimation(context, R.anim.opening_animation)
    var animationClose: Animation = AnimationUtils.loadAnimation(context, R.anim.closing_animation)


    private fun toDate(milliSeconds: Long, format: String) : String {
        val formatter = SimpleDateFormat(format)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    private fun imageIdToRes(id: Int) : Int {
        return when(id){
            1 -> R.drawable.dairy
            2 -> R.drawable.meat
            3 -> R.drawable.shrimp
            4 -> R.drawable.vegetables
            5 -> R.drawable.fruit
            6 -> R.drawable.sweets
            else -> R.drawable.unknown
        }
    }


}