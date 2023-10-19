package com.hypernym.smartsurveillance.view.fragment.admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.listeners.OnOneOffClickListener
import com.hypernym.smartsurveillance.utilities.AppUtils
import com.hypernym.smartsurveillance.view.fragment.BaseFragment
import com.hypernym.smartsurveillance.view.fragment.persongroupmanagement.PersonGroupListFragment
import kotlinx.android.synthetic.main.fragment_admin.*


class AdminFragment : BaseFragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }


    override fun onResume() {
        super.onResume()
        AppUtils.fragmentName = AdminFragment::class.java.name
        context?.let { onAttach(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
           // toolbar_title.setText(it.resources.getString(R.string.admin_page_title))
            setToolbarTitle(it, it.resources.getString(R.string.admin_page_title))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // by default open group fragment
        context?.let {
            replaceFragment(R.id.dashboard_container, PersonGroupListFragment(),
                it, false)
        }

        // group details button click listener
        group?.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(view: View?) {
                setActiveButtonTheme(group)
                setInActiveButtonTheme(enrollment)
                setInActiveButtonTheme(attendance)

                context?.let {
                    replaceFragment(
                        R.id.dashboard_container, PersonGroupListFragment(),
                        it, false
                    )
                }
            }
        })

        // enrollment button click listener
        enrollment?.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(view: View?) {
                setActiveButtonTheme(enrollment)
                setInActiveButtonTheme(group)
                setInActiveButtonTheme(attendance)

                context?.let {
                    replaceFragment(R.id.dashboard_container, EnrollmentFragment(),
                        it, false)
                }
            }
        })

        // attendance button click listener
        attendance?.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(view: View?) {
                setActiveButtonTheme(attendance)
                setInActiveButtonTheme(group)
                setInActiveButtonTheme(enrollment)

                context?.let {
                    replaceFragment(R.id.dashboard_container, AttendanceFragment(),
                        it, false)
                }
            }
        })
    }
}