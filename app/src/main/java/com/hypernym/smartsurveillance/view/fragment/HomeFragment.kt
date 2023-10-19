package com.hypernym.smartsurveillance.view.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.camerax.CameraManager
import com.hypernym.smartsurveillance.helper.StorageHelper
import com.hypernym.smartsurveillance.utilities.ActivityUtils
import com.hypernym.smartsurveillance.utilities.AppUtils
import com.hypernym.smartsurveillance.utilities.AppUtils.applicationContext
import com.hypernym.smartsurveillance.view.PauseActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    private lateinit var cameraManager: CameraManager
    val handler = Handler(Looper.getMainLooper())
    val runnable : Runnable? = null
    val delay = 500 // 1000 milliseconds == 1 second


    override fun onResume() {
        super.onResume()
        AppUtils.fragmentName = HomeFragment::class.java.name
        context?.let { onAttach(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            setToolbarTitle(it, it.resources.getString(R.string.home_page_title))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppUtils.applicationContext = applicationContext
        createCameraManager()
        checkForPermission()
        onClicks()
        getLatestPersonGroupId()


        handler.postDelayed(object : Runnable {
            override fun run() {

                Log.e(TAG, "Bitmap$AppUtils.bitmap")
                Glide.with(applicationContext).asBitmap().load(AppUtils.bitmap).into(resultImageView)

                resultImageView.setColorFilter(R.color.black)
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

    private fun getLatestPersonGroupId() {
        val personGroupIds = StorageHelper.getAllPersonGroupIds(requireActivity())
        if(personGroupIds.size>0){
            AppUtils.personGroupId = personGroupIds.last().toString()}
        /*  else
              AppUtils.personGroupId = ""*/
        /* for (personGroupId in personGroupIds) {
             personGroupIdList.add(personGroupId)
             personGroupChecked.add(false)
         }*/
    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun onClicks() {
        btnSwitch.setOnClickListener {

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
                Toast.makeText(requireActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                requireActivity().finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            requireActivity(),
            previewView_finder,
            this,
            graphicOverlay_finder
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        cameraManager.pauseResmueCamera()
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onPause() {
        cameraManager.pauseResmueCamera()
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }


    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        val TAG = "MainActivity"
    }
}