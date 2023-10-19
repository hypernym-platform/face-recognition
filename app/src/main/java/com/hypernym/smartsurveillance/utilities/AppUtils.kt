package com.hypernym.smartsurveillance.utilities

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hypernym.smartsurveillance.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.hypernym.smartsurveillance.helper.StorageHelper
import com.kishandonga.csbx.CustomSnackbar
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.security.SecureRandom


object AppUtils {
    lateinit var applicationContext: Context
    lateinit var currentFragment: Fragment
    var bitmap:Bitmap? = null
    var fragmentName: String = ""
    var originalBitmap:Bitmap? = null
    var info : String? = null
    var isProcessing:Boolean? = false
    var isCameraPause:Boolean? = false
    var screenSaver:Boolean? = false
    var signalrAccessToken: String?=null
    var signalrURL: String?=null
    var personGroupId: String?=null
    var attendenceList: MutableList<String> = mutableListOf()
    var attendenceTime: MutableList<String> = mutableListOf()

    fun isDarkTheme(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun getLatestPersonGroupId(): String {
        val personGroupIds = StorageHelper.getAllPersonGroupIds(applicationContext)
        if(personGroupIds.size>0) {
            return personGroupIds.last().toString()

        }else
            return ""
    }

    fun displayCenteredToast(message: String?){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun displaySnackbar(context: Context,message: String?){
        CustomSnackbar(context).show {
            backgroundColor(Color.parseColor("#4F74EE"))
            cornerRadius(5f)
            padding(10)
            duration(Snackbar.LENGTH_SHORT)
            actionTextColor(0xFFFFFF)
            message(message.toString())
            withAction(android.R.string.ok) {
                it.dismiss()
            }
        }
    }
    fun isInternetConnected(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as? ConnectivityManager

            val netInfo: NetworkInfo? = connectivityManager?.activeNetworkInfo

            return (netInfo != null && netInfo.isConnected)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return false
    }

    fun getChartColors(): IntArray {
        return intArrayOf(

            // material colors.
            Color.rgb(46, 204, 113),
            Color.rgb(241, 196, 15),
            Color.rgb(231, 76, 60),
            Color.rgb(52, 152, 219),

            // pastel
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134),
            Color.rgb(179, 48, 80),

            // joyful
            Color.rgb(217, 80, 138),
            Color.rgb(254, 149, 7),
            Color.rgb(254, 247, 120),
            Color.rgb(106, 137, 134),
            Color.rgb(53, 194, 209),

            // liberty colors.
            Color.rgb(207, 248, 246),
            Color.rgb(148, 212, 212),
            Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175),
            Color.rgb(42, 109, 130),

            // colorful
            Color.rgb(193, 37, 82),
            Color.rgb(255, 102, 0),
            Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53),

            // vordiplom colors.
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 140),
            Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157)
        )
    }

    fun generateRandomColor(): Int {
        val rnd = SecureRandom()
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        return color
    }



    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

//    fun setGlideImageNoTansforamtion(context: Context?, imageView: ImageView, url: String?) {
//        Glide.with(context!!) //1
//            .load(url)
//            .apply(RequestOptions.noTransformation())
//            .placeholder(R.mipmap.ic_launcher)
//            .into(imageView)
//    }
//
//    fun setVehicleImage(context: Context?, imageView: ImageView, url: String?){
//        Glide.with(context!!)
//            .load(url)
//            .apply(RequestOptions.circleCropTransform())
//            .placeholder(R.mipmap.ic_launcher)
//            .into(imageView)
//    }
//
//    fun setGlideImage_user(context: Context?, imageView: ImageView, url: String?) {
//        Glide.with(context!!) //1
//            .load(url)
//            .apply(RequestOptions.circleCropTransform())
//            .placeholder(R.mipmap.ic_launcher)
//            .into(imageView)
//    }

    fun hideKeyboardFrom(context: Context, view: View?) {
        val imm: InputMethodManager? =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager

        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun convertImagetoBitmap(image: Image): Bitmap {
        val planes: Array<Image.Plane> = image.getPlanes()
        val yBuffer: ByteBuffer = planes.get(0).getBuffer()
        val uBuffer: ByteBuffer = planes.get(1).getBuffer()
        val vBuffer: ByteBuffer = planes.get(2).getBuffer()

        val ySize: Int = yBuffer.remaining()
        val uSize: Int = uBuffer.remaining()
        val vSize: Int = vBuffer.remaining()

        val nv21: ByteArray = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage: YuvImage =
            YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null)
        val out: ByteArrayOutputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out)

        val imageBytes: ByteArray = out.toByteArray()

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

}