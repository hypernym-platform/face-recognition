package com.hypernym.smartsurveillance.view.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.adapter.AttendenceAdapter
import com.hypernym.smartsurveillance.adapter.PersonGroupsListAdapter
import com.hypernym.smartsurveillance.callback.SwipeToDeleteCallback
import com.hypernym.smartsurveillance.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.fragment_group.*


class AttendanceFragment : BaseFragment(), AttendenceAdapter.OnItemClickListener {
    private var personGroupsListAdapter: AttendenceAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    private fun setupRecyclerview() {
        personGroupsListAdapter = AttendenceAdapter(context, this)
        linearLayoutManager = LinearLayoutManager(context)
        rv_attendence!!.setLayoutManager(linearLayoutManager)
        rv_attendence!!.setAdapter(personGroupsListAdapter)

        if (rv_attendence.size > 0) {
            tv_no_data.visibility = View.VISIBLE

        } else {
            tv_no_data.visibility = View.GONE
        }
    }

    override fun onItemClick(view: View?, position: Int) {

    }
}