package com.hypernym.smartsurveillance.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.helper.StorageHelper
import com.hypernym.smartsurveillance.listeners.OnOneOffClickListener
import kotlinx.android.synthetic.main.item_person.view.*

import java.util.*

class PersonGridViewAdapter( private val personGroupId:String?, private var context: Context?,
                             onItemClick: OnItemClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var personIdList: MutableList<String>
    var personChecked: MutableList<Boolean>
    var longPressed = false
    var onItemClickListener: OnItemClickListener = onItemClick




    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    init {
        personIdList = ArrayList()
        personChecked = ArrayList()
        val personIdSet = StorageHelper.getAllPersonIds(personGroupId, context)
        for (personId in personIdSet) {
            personIdList.add(personId)
            personChecked.add(false)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bindView(personIdList: String, context: Context?,personGroupId:String)
        {


            personIdList.let {

                // set the text of the item
                val personGroupName = StorageHelper.getPersonGroupName(
                    it, context
                )
                val personNumberInGroup = StorageHelper.getAllPersonIds(
                    it, context
                ).size

                val personId = it
                val faceIdSet = StorageHelper.getAllFaceIds(personId.toString(), context)
                if (!faceIdSet.isEmpty()) {
                    val it: Iterator<String> = faceIdSet.iterator()
                    val uri = Uri.parse(StorageHelper.getFaceUri(it.next(), context))
                    itemView.image_person.setImageURI(uri)
                } else {
                    val drawable: Drawable = context!!.resources.getDrawable(R.drawable.select_image)
                    itemView.image_person.setImageDrawable(
                        drawable
                    )
                }

                // set the text of the item
                val personName =
                    StorageHelper.getPersonName(personId.toString(), personGroupId, context)
                itemView.text_person.text = personName ?: "--"

/*                // set the checked status of the item
                val checkBox =  itemView.checkbox_person
                if (longPressed) {
                    checkBox.visibility = View.VISIBLE
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        personChecked[position] = isChecked
                    }
                    checkBox.isChecked = personChecked[position]
                } else {
                    checkBox.visibility = View.INVISIBLE
                }*/

            }
        }
        override fun onClick(v: View) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val ViewHolder = holder as? ViewHolder
        ViewHolder?.bindView(personIdList[position],context,personGroupId!!)

        // click listener for list item click
        ViewHolder?.itemView?.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(view: View?) {
                onItemClickListener.onItemClick(view, position)
            }
        })
    }

    override fun getItemCount(): Int {
        return personIdList.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}