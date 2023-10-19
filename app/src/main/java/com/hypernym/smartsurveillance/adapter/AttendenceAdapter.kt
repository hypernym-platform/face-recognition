package com.hypernym.smartsurveillance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.helper.StorageHelper
import com.hypernym.smartsurveillance.listeners.OnOneOffClickListener
import com.hypernym.smartsurveillance.utilities.AppUtils
import kotlinx.android.synthetic.main.item_group.view.*
import java.util.ArrayList

class AttendenceAdapter (
    private var context: Context?,
    onItemClick: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClickListener: OnItemClickListener = onItemClick


    var personGroupIdList: MutableList<String>
    var personGroupChecked: MutableList<Boolean>
    var personGroupCheckedIdList: MutableList<String>
    var longPressed = false

    fun getCount(): Int {
        return personGroupIdList.size
    }

    fun getItem(position: Int): Any {
        return personGroupIdList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    init {
        personGroupIdList = ArrayList()
        personGroupChecked = ArrayList()
        personGroupCheckedIdList = ArrayList()
       // val personGroupIds = StorageHelper.getAllPersonGroupIds(context)
        val personGroupIds = AppUtils.attendenceList
        for (personGroupId in personGroupIds) {
            personGroupIdList.add(personGroupId)
            personGroupChecked.add(false)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bindView(personGroupIdList: String, context: Context?) {


            personGroupIdList.let {




                itemView.tv_group_name?.text = personGroupIdList ?: "--"
              /*  itemView.tv_number_of_group_persons?.text =
                    String.format("Person count: %d", personNumberInGroup) ?: "--"*/
            }

            itemView.tv_number_of_group_persons.text = AppUtils.attendenceTime[adapterPosition]
        }

        override fun onClick(v: View) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val ViewHolder = holder as? ViewHolder
        ViewHolder?.bindView(personGroupIdList[position], context)

        // click listener for list item click
        ViewHolder?.itemView?.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(view: View?) {
                onItemClickListener.onItemClick(view, position)
            }
        })
//
/*        ViewHolder?.itemView?.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                v!!.setBackgroundColor(getColor(context!!, R.color.grey))
                if (!personGroupCheckedIdList.contains(personGroupIdList[position])) {
                    personGroupCheckedIdList.add(personGroupIdList[position])
                    personGroupCheckedIdList.forEach {
                        Log.e(TAG, " $it")
                    }
                }
                return true
            }
        })*/
    }


    override fun getItemCount(): Int {
        return personGroupIdList.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun removeAt(position: Int): String {
        val personGroupId = personGroupIdList[position]
        personGroupIdList.removeAt(position)
        notifyItemRemoved(position)
        return personGroupId
    }

    fun getGroupId(position: Int): String {
        return personGroupIdList[position]
    }

    companion object {
        const val TAG = "groupList"
    }
}