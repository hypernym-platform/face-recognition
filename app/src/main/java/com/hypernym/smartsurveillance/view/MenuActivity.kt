package com.hypernym.smartsurveillance.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.utilities.ActivityUtils
import com.hypernym.smartsurveillance.view.persongroupmanagement.PersonGroupListActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button_attendance.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
              ActivityUtils.startActivity(this@MenuActivity,
                  MainActivity::class.java,false)
            }
        })

        button_enrollment.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                ActivityUtils.startActivity(this@MenuActivity,
                    PersonGroupListActivity::class.java,false)

            }
        })
    }
}