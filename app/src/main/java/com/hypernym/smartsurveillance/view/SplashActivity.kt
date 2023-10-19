package com.hypernym.smartsurveillance.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.utilities.AppUtils
import java.util.*

class SplashActivity : AppCompatActivity() {
    val permissionRequestCode = 1;
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        AppUtils.applicationContext = applicationContext


                // if permissions are not granted then requests the user for permissions.
        if (!check_permission()){
            requestPermissions()
        }
        else{
            init()
        }
    }

    private fun init(){
        //  throw RuntimeException("test crash for crashlytics")
        //delay for the splash screen.
        Handler(Looper.getMainLooper()).postDelayed({


         /*   if (PrefUtils.getBoolean(applicationContext, AppConstants.IS_USER_LOGGED_IN))
            {
                val intent = Intent(AppUtils.applicationContext, HomeActivity::class.java)

                startActivity(intent)
                finish()
            }
            else{*/
                val intent = Intent(this, FrameActivity::class.java)
                startActivity(intent)
                finish()
           // }
        }, 3000)
    }

    fun check_permission(): Boolean{
        val externalReadResult = ContextCompat.checkSelfPermission(
            this@SplashActivity, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val externalWriteResult = ContextCompat.checkSelfPermission(
            this@SplashActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraResult = ContextCompat.checkSelfPermission(
            this@SplashActivity, Manifest.permission.CAMERA
        )


        return externalReadResult == PackageManager.PERMISSION_GRANTED &&
                externalWriteResult == PackageManager.PERMISSION_GRANTED &&
                cameraResult == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(){
        ActivityCompat.requestPermissions(this@SplashActivity, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ), permissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        init()
    }

    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode!!)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())

    }
}