package  com.hypernym.smartsurveillance.listeners

import android.view.View

interface ItemClickListener {

    fun onItemClick(view: View?, data: Any, position: Int)
}