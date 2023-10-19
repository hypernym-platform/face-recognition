package com.hypernym.smartsurveillance.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.utilities.ActivityUtils
import com.hypernym.smartsurveillance.utilities.AppUtils
import com.hypernym.smartsurveillance.view.MainActivity
import kotlinx.android.synthetic.main.activity_pause.*


class PauseFragment : BaseFragment(){


    override fun onResume() {
        super.onResume()
        pulsator.start()
        AppUtils.fragmentName = PauseFragment::class.java.name
        context?.let { onAttach(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            setToolbarTitle(it, it.resources.getString(R.string.pause_page_title))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_punch.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                context?.let {
                    replaceFragment(R.id.layout_container, HomeFragment(),
                        it, true)
                }
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pause, container, false)
    }

    override fun onPause() {
        pulsator.stop()
        super.onPause()
    }


}