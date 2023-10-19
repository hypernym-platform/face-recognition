package com.hypernym.smartsurveillance.camerax

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.Image
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class BaseImageAnalyzer<T> : ImageAnalysis.Analyzer {

    abstract val graphicOverlay: GraphicOverlay

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let {
            detectInImage(InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees),it)
                .addOnSuccessListener { results ->
                    onSuccess(
                        results,
                        graphicOverlay,
                        it.cropRect
                    )
                 //   detect(convertImagetoBitmap(it))
                    imageProxy.close()
                }
                .addOnFailureListener {
                    onFailure(it)
                    imageProxy.close()
                }
        }
    }

    protected abstract fun detectInImage(inputimage: InputImage, image: Image): Task<T>

    protected abstract fun detect(mBitmap: Bitmap?)


    abstract fun stop()


    protected abstract fun onSuccess(
        results: T,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    )

    protected abstract fun onFailure(e: Exception)

    fun replaceFragment(id: Int, fragment: Fragment, context: Context, addToBackStack: Boolean, bundle: Bundle?){
        val transaction: FragmentTransaction? = (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
        transaction?.replace(id, fragment)

        if (bundle != null){
            fragment?.arguments = bundle
        }

        if (addToBackStack){
            transaction?.addToBackStack(null)
        }

        transaction?.commit()
    }

}