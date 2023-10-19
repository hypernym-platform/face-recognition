package com.hypernym.smartsurveillance.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.hypernym.smartsurveillance.R


object ActivityUtils {

    fun startActivity(context: Activity, class_: Class<*>?, isFinish: Boolean) {
        val intent = Intent(context, class_)
        context.startActivity(intent)
      //  context.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        if (isFinish) context.finish()
    }

    fun startActivityForResult(
        context: Activity,
        class_: Class<*>?,
        fragmentName: String?,
        bundle: Bundle?,
        REQUEST_EXIT: Int
    ) {
        val intent = Intent(context, class_)
        intent.putExtra(AppConstants.FRAGMENT_NAME, fragmentName)
        intent.putExtra(AppConstants.DATA, bundle)
        context.startActivityForResult(intent, REQUEST_EXIT)
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun startActivity(
        context: Activity,
        class_: Class<*>?,
        fragmentName: String?,
        bundle: Bundle?
    ) {
        val intent = Intent(context, class_)
        intent.putExtra(AppConstants.FRAGMENT_NAME, fragmentName)
        intent.putExtra(AppConstants.DATA, bundle)
        context.startActivity(intent)
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun startActivity(context: Context, class_: Class<*>?, fragmentName: String?, bundle: Bundle?) {
        val intent = Intent(context, class_)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(AppConstants.FRAGMENT_NAME, fragmentName)
        intent.putExtra(AppConstants.DATA, bundle)
        context.startActivity(intent)
    }

    fun startActivity(
        context: Context,
        class_: Class<*>?,
        fragmentName: String?,
        bundle: Bundle?,
        isUpdateCurrent: Boolean
    ) {
        val intent = Intent(context, class_)
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.putExtra(AppConstants.FRAGMENT_NAME, fragmentName)
        intent.putExtra(AppConstants.DATA, bundle)
        context.startActivity(intent)
    }

    fun startActivityChat(
        context: Context,
        class_: Class<*>?,
        masterfragmentName: String?,
        detailfragmentName: String?,
        masterBundle: Bundle?,
        detailBundle: Bundle?,
        isUpdateCurrent: Boolean
    ) {
        val intent = Intent(context, class_)
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.putExtra(AppConstants.MASTER_FRAGMENT_NAME, masterfragmentName)
        intent.putExtra(AppConstants.DETAILS_FRAGMENT_NAME, detailfragmentName)

        intent.putExtra(AppConstants.MASTER_FRAGMENT_DATA, masterBundle)
        intent.putExtra(AppConstants.DETAIL_FRAGMENT_DATA, detailBundle)
        context.startActivity(intent)
    }

    fun startHomeActivity(context: Context, class_: Class<*>?, fragmentName: String?) {
        val intent = Intent(context, class_)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(AppConstants.FRAGMENT_NAME, fragmentName)
        context.startActivity(intent)
    }

    fun startWifiSettings(context: Context) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


}