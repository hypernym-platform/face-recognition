package com.hypernym.smartsurveillance.view.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputLayout
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.utilities.ToolbarTitle

open class BaseFragment: Fragment() {


 //   private lateinit var progressHUD: KProgressHUD
    private val locationInterval = 4000
    var mGoogleApiClient: GoogleApiClient? = null


    fun isDarkTheme(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }



   /* fun showProgressHUD(context: Context){
        progressHUD = KProgressHUD(context)
        progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        progressHUD.setDetailsLabel(null)
        progressHUD.setLabel(context.resources.getString(R.string.loading_text1))
        progressHUD.setDetailsLabel(context.resources.getString(R.string.loading_text))
        progressHUD.setCancellable(false)
        progressHUD.setAutoDismiss(true)
        progressHUD.setAnimationSpeed(2)
        progressHUD.setDimAmount(0.5f)

        if (this::progressHUD.isInitialized)
            progressHUD.show()
    }

    fun showProgressWithText(context: Context, message: String){
        progressHUD = KProgressHUD(context)
        progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        progressHUD.setDetailsLabel(null)
        progressHUD.setLabel(context.resources.getString(R.string.loading_text1))
        progressHUD.setDetailsLabel(message)
        progressHUD.setCancellable(false)
        progressHUD.setAutoDismiss(true)
        progressHUD.setAnimationSpeed(2)
        progressHUD.setDimAmount(0.5f)

        if (this::progressHUD.isInitialized)
            progressHUD.show()
    }

    fun dismissProgressHUD(){
        if (this::progressHUD.isInitialized && progressHUD!=null && progressHUD.isShowing){
            progressHUD.dismiss()
        }
    }
*/
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
    fun setToolbarTitle(context: Context?, title: String)
    {
        // if the provided context is not null
        // then perform all the actions on toolbar
        // such as setting the title etc..
        context?.let {

            // set title
            if (it is ToolbarTitle){
                (context as? ToolbarTitle)?.setTitle(title)
            }
        }
    }





    fun setActiveButtonTheme(button: AppCompatButton){
        button.setBackgroundResource(R.drawable.active_button)
        button.setTextColor(Color.WHITE)
    }

    fun setInActiveButtonTheme(button: AppCompatButton){
        button.setBackgroundResource(R.drawable.inactive_button)
        button.setTextColor(Color.parseColor("#949393"))
    }

    fun setActiveTextViewTheme(button: TextView){
        if(resources.getBoolean(R.bool.portrait_only)) // for mobiles
        {
            button.setBackgroundResource(R.drawable.active_button)
            button.setTextColor(Color.parseColor("#ffffff"))
        }
        else{ // for tablets
            button.setBackgroundResource(R.drawable.active_button)
            button?.setBackgroundColor(Color.parseColor("#c32121"))
            button.setTextColor(Color.WHITE)
        }
    }

    fun setInActiveTextViewTheme(button: TextView){
        if(resources.getBoolean(R.bool.portrait_only)) // for mobiles
        {
            button.setBackgroundResource(R.drawable.inactive_button)
            button.setTextColor(Color.parseColor("#949393"))
        }
        else{ // for tablets
            button.setBackgroundResource(R.drawable.active_button)
            button?.setBackgroundColor(Color.parseColor("#E60000"))
            button.setTextColor(Color.WHITE)
        }

    }

    fun addFragment(id: Int, fragment: Fragment, context: Context){
        val transaction: FragmentTransaction? = (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
        transaction?.add(id, fragment)
        transaction?.commit()
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

    fun replaceMasterFragment(id: Int, fragment: Fragment, context: Context, addToBackStack: Boolean, bundle: Bundle?){
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

    fun replaceDetailFragment(id: Int, fragment: Fragment, context: Context, addToBackStack: Boolean, bundle: Bundle?){
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

    fun replaceFragment(id: Int, fragment: Fragment, context: Context, addToBackStack: Boolean){
        val transaction: FragmentTransaction? = (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
        transaction?.replace(id, fragment)

        if (addToBackStack){
            transaction?.addToBackStack(null)
        }

        transaction?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize the dashboard view model
       // dashboardViewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
    }

    /*    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())

    }*/
}