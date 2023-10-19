package com.hypernym.smartsurveillance.view

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import com.hypernym.smartsurveillance.R


import java.util.*

open class BaseActivity: AppCompatActivity() {

/*    private lateinit var progressHUD: KProgressHUD

    fun showProgressHUD(context: Context){
        progressHUD = KProgressHUD(context)
        progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        progressHUD.setLabel(context.resources.getString(R.string.loading_text1))
        progressHUD.setDetailsLabel(context.resources.getString(R.string.loading_text))
        progressHUD.setAnimationSpeed(2)
        progressHUD.setDimAmount(0.5f)
        progressHUD.setCancellable(false)

        if (this::progressHUD.isInitialized)
            progressHUD.show()
    }

    fun dismissProgressHUD(){
        if (this::progressHUD.isInitialized && progressHUD!=null && progressHUD.isShowing){
            progressHUD.dismiss()
        }
    }*/
    fun isDarkTheme(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    /*fun addFragment(id: Int, fragment: Fragment, context: Context){
        val transaction: FragmentTransaction? = (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
        transaction?.add(id, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }*/

    /*fun replaceFragment(id: Int, fragment: Fragment, context: Context, addToBackStack: Boolean){
        val transaction: FragmentTransaction? = (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
        transaction?.replace(id, fragment)

        if (addToBackStack){
            transaction?.addToBackStack(null)
        }

        transaction?.commit()
    }*/

    fun setLayoutError(inputLayout: TextInputLayout, text: String){
        inputLayout?.error = text
        inputLayout?.setErrorTextColor(ColorStateList.valueOf(Color.RED))
        inputLayout?.hintTextColor = ColorStateList.valueOf(Color.RED)
        inputLayout?.errorIconDrawable = null
    }

    fun setLayoutSuccess(inputLayout: TextInputLayout, text: String){
        inputLayout?.error = text
        inputLayout?.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#5BB32F")))
        inputLayout?.hintTextColor = ColorStateList.valueOf(Color.parseColor("#5BB32F"))
        inputLayout?.errorIconDrawable = null
    }

    fun setLayoutToDefault(inputLayout: TextInputLayout, text: String){
        inputLayout?.error = text
        inputLayout?.setErrorTextColor(ColorStateList.valueOf(Color.BLACK))
        inputLayout?.hintTextColor = ColorStateList.valueOf(Color.BLACK)
        inputLayout?.errorIconDrawable = null
    }

    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())

    }

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