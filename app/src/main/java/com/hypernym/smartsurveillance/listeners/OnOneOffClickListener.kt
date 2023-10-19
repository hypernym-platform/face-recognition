package com.hypernym.smartsurveillance.listeners

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View

/*
* Class created to avoid double clicks on layout views
* thus leading to screens/actions being opened/performed twice.
* */
abstract class OnOneOffClickListener: View.OnClickListener{
    val MIN_CLICK_INTERVAL: Long = 800;
    var mLastClickTime: Long = 0
    var isViewClicked: Boolean = false

    abstract fun onSingleClick(view: View?): Unit

    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime

        mLastClickTime = currentClickTime

        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return

        if (!isViewClicked){
            isViewClicked = true
            startTimer()
        }
        else{
            return
        }

        onSingleClick(v)
    }

    /**
     * This method delays simultaneous touch events of multiple views.
     */
    private fun startTimer() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            isViewClicked = false
        }, MIN_CLICK_INTERVAL)
    }
}
