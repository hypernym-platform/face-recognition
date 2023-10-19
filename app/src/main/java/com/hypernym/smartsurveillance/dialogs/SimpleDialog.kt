package com.hypernym.smartsurveillance.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.hypernym.smartsurveillance.R


class SimpleDialog(context: Context, title: String, message: String, nothing: String?, string: String, onClickListener: View.OnClickListener, context1: Context) : Dialog(context) {

    var mTitle: String? = title
    var mMessage: String? = message
    var mPositiveButtonText: String? = nothing
    var mNegativeButtonText: String? = string
    var mOnClickListener: View.OnClickListener? = onClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_simple)
        setCancelable(false)
        val titleText = findViewById<View>(R.id.text_title) as TextView
        val messageText = findViewById<View>(R.id.text_message) as TextView
        val negativeButton = findViewById<View>(R.id.button_negative) as Button
        val positiveButton = findViewById<View>(R.id.button_positive) as Button
        negativeButton.transformationMethod = null
        positiveButton.transformationMethod = null
        mTitle
        if (mTitle != null) {
            titleText.text = mTitle
            titleText.visibility = View.VISIBLE
        }
        if (mMessage != null) {
            messageText.text = mMessage
            messageText.visibility = View.VISIBLE
        }
        if (mNegativeButtonText != null) {
            negativeButton.text = mNegativeButtonText
            negativeButton.visibility = View.VISIBLE
            negativeButton.setOnClickListener(mOnClickListener)
        }
        if (mPositiveButtonText != null) {
            positiveButton.text = mPositiveButtonText
            positiveButton.visibility = View.VISIBLE
            positiveButton.setOnClickListener(mOnClickListener)
        }
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
