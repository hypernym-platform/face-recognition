package com.hypernym.smartsurveillance.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.camerax.CameraManager
import com.hypernym.smartsurveillance.utilities.ActivityUtils
import com.hypernym.smartsurveillance.utilities.AppUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppUtils.applicationContext = applicationContext
        createCameraManager()
        checkForPermission()
        onClicks()


        val handler = Handler()
        val delay = 500 // 1000 milliseconds == 1 second


        handler.postDelayed(object : Runnable {
            override fun run() {

                Log.e(TAG, "Bitmap$AppUtils.bitmap")
                Glide.with(applicationContext).asBitmap().load(AppUtils.bitmap).into(resultImageView)

                //val bitmap = AppUtils.bitmap

                if (AppUtils.screenSaver == true){
                    AppUtils.screenSaver=false
                    ActivityUtils.startActivity(this@MainActivity,PauseActivity::class.java,true)
                }

                resultImageView.setColorFilter(R.color.black)
                info.text = AppUtils.info
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun onClicks() {
        btnSwitch.setOnClickListener {
            cameraManager.pauseResmueCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            previewView_finder,
            this,
            graphicOverlay_finder
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        val TAG = "MainActivity"
    }

}