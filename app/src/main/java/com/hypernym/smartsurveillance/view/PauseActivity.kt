package com.hypernym.smartsurveillance.view

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.utilities.ActivityUtils
import com.hypernym.smartsurveillance.utilities.AppUtils

import kotlinx.android.synthetic.main.activity_pause.*


class PauseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause)
        AppUtils.applicationContext = applicationContext
        pulsator.start()

        iv_punch.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                ActivityUtils.startActivity(this@PauseActivity,MainActivity::class.java,true)
            }

        })

    }
}