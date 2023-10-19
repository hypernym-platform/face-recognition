package com.hypernym.smartsurveillance.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.hypernym.smartsurveillance.R
import kotlinx.android.synthetic.main.dialog_create_group.*

class LockDiaglog (context: Context, onClickListener: View.OnClickListener) : Dialog(context) {


    var mOnClickListener: View.OnClickListener? = onClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_lock)
        setCancelable(false)

        if (create_button != null) {
            create_button.setOnClickListener(mOnClickListener)
        }

        if (close_button != null) {
            close_button.setOnClickListener(mOnClickListener)
        }


        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
}