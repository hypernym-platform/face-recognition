package com.hypernym.smartsurveillance.view

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.dialogs.CreatePersonGroupDialog
import com.hypernym.smartsurveillance.dialogs.LockDiaglog
import com.hypernym.smartsurveillance.listeners.OnOneOffClickListener
import com.hypernym.smartsurveillance.utilities.AppConstants
import com.hypernym.smartsurveillance.utilities.AppUtils
import com.hypernym.smartsurveillance.utilities.ToolbarTitle
import com.hypernym.smartsurveillance.view.fragment.admin.AdminFragment
import com.hypernym.smartsurveillance.view.fragment.HomeFragment
import com.hypernym.smartsurveillance.view.fragment.PauseFragment
import kotlinx.android.synthetic.main.dialog_create_group.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class FrameActivity : BaseActivity(), Toolbar.OnMenuItemClickListener, ToolbarTitle {

    var mSimpleDialog: LockDiaglog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame)



        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // hide the built-in toolbar provided by the system
        supportActionBar?.hide()

        // init base container fragment
        initBaseFragment()

        // toolbar notification icon click listener
        app_toolbar?.setOnMenuItemClickListener(this)
    }

    private fun initBaseFragment() {
        if (intent.extras != null) {
            val fragmentName = intent.getStringExtra(AppConstants.FRAGMENT_NAME)
            val bundle: Bundle? = intent.getBundleExtra(AppConstants.DATA)
            if (fragmentName != null && !TextUtils.isEmpty(fragmentName)) {
                val fragment = Fragment.instantiate(this, fragmentName)
                if (bundle != null) {
                    fragment.arguments = bundle
                }



                AppUtils.currentFragment = fragment
                supportFragmentManager.beginTransaction().replace(R.id.layout_container, fragment)
                    .commit()
            }
            else{
                // as per the design load dashboard screen by default
                addFragment(PauseFragment())

            }
        } else {
            // as per the design load dashboard screen by default
            addFragment(PauseFragment())

        }
    }

    fun addFragment(fragment: Fragment) {
        AppUtils.currentFragment = fragment

        if (fragment.javaClass.name == HomeFragment::class.java.name) {
            supportFragmentManager.beginTransaction().replace(R.id.layout_container, fragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.layout_container, fragment)
                .addToBackStack(
                    fragment.javaClass.name
                ).commit()
        }
    }

    override fun setTitle(title: String) {
        try {
            toolbar_title?.setText(title)
            setMenuItems()
          //  dismissProgressHUD()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setMenuItems() {
        guest?.setOnClickListener(object:OnOneOffClickListener(){
            override fun onSingleClick(view: View?) {
                Toast.makeText(this@FrameActivity,R.string.in_progress,Toast.LENGTH_SHORT).show()
            }
        })

        back?.setOnClickListener(object:OnOneOffClickListener(){
            override fun onSingleClick(view: View?) {
                supportFragmentManager.popBackStack()
            }
        })

        when {
            AppUtils.fragmentName.equals(HomeFragment::class.java.name)||
                    AppUtils.fragmentName.equals(PauseFragment::class.java.name) -> {
                app_toolbar?.menu?.getItem(0)?.isVisible = true
                app_toolbar?.menu?.getItem(1)?.isVisible = false

                back?.visibility = View.GONE
                hypernym_icon?.visibility = View.VISIBLE

                toolbar_title?.visibility = View.GONE
                logout?.visibility = View.GONE
              //  guest?.visibility = View.VISIBLE

            }
            else -> {
                app_toolbar?.menu?.getItem(0)?.isVisible = false
                app_toolbar?.menu?.getItem(1)?.isVisible = false
                logout?.visibility = View.GONE
              //  guest?.visibility = View.GONE
                back?.visibility = View.VISIBLE
                toolbar_title?.visibility = View.VISIBLE
                hypernym_icon.visibility = View.GONE
            }
        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.guest -> {
              Toast.makeText(this,R.string.in_progress,Toast.LENGTH_SHORT).show()
            }
            R.id.lock -> {
                showDialog(this@FrameActivity)

            }

        }

        return false
    }

    private fun showDialog(context: Context) {
        mSimpleDialog = LockDiaglog(
            context,
            View.OnClickListener {
                when (it.id) {
                    R.id.create_button -> {
                        val personGroupName = mSimpleDialog!!.ed_person_group_name.text.toString()
                        if (personGroupName.isNotEmpty() && personGroupName.equals("1234")) {

                            getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            )
                            replaceFragment( R.id.layout_container,
                                AdminFragment(),
                                this,
                                true,
                                null)
                            mSimpleDialog?.dismiss()
                        } else {
                            Toast.makeText(
                              this@FrameActivity,
                                "Please enter a valid passcode",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                    R.id.close_button -> {
                        mSimpleDialog?.dismiss()

                    }
                }

            }
        )
        mSimpleDialog?.show()
        mSimpleDialog?.setCanceledOnTouchOutside(true)
        mSimpleDialog?.setCancelable(true)
    }
}