package com.hypernym.smartsurveillance.view.fragment.persongroupmanagement

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.adapter.PersonGroupsListAdapter
import com.hypernym.smartsurveillance.callback.SwipeToDeleteCallback
import com.hypernym.smartsurveillance.dialogs.CreatePersonGroupDialog
import com.hypernym.smartsurveillance.helper.StorageHelper
import com.hypernym.smartsurveillance.listeners.OnOneOffClickListener
import com.hypernym.smartsurveillance.utilities.AppConstants
import com.hypernym.smartsurveillance.view.fragment.BaseFragment
import com.hypernym.smartsurveillance.view.persongroupmanagement.PersonGroupActivity
import com.microsoft.projectoxford.face.FaceServiceClient
import com.microsoft.projectoxford.face.FaceServiceRestClient
import kotlinx.android.synthetic.main.dialog_create_group.*
import kotlinx.android.synthetic.main.fragment_group.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class PersonGroupListFragment : BaseFragment(), PersonGroupsListAdapter.OnItemClickListener {

    private var deleteLargePersonGroupResult: String? = null
    private var personGroupsListAdapter: PersonGroupsListAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    // Progress dialog popped up when communicating with server.
    var progressDialog: ProgressDialog? = null
    var mSimpleDialog: CreatePersonGroupDialog? = null

    val faceServiceClient: FaceServiceClient = FaceServiceRestClient(
        AppConstants.AZURE_ENDPOINT,
        AppConstants.AZURE_SUBCRIPTION_KEY
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListView()

        create_group_button.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(view: View?) {
                showDialog(requireActivity())
            }
        })

        Handler().postDelayed({
            if (rv_groups.size > 0) {
                create_group_button.isEnabled = false
                create_group_button.isFocusable = false
                create_group_button.isClickable = false
                create_group_button.setBackgroundColor(Color.parseColor("#8E8E8E"))

            } else {
                create_group_button.isEnabled = true
                create_group_button.isFocusable = true
                create_group_button.isClickable = true
                create_group_button.setBackgroundColor(Color.parseColor("#4F74EE"))
            }
        }, 500)


    }

    private fun showDialog(context: Context) {
        mSimpleDialog = CreatePersonGroupDialog(
            context,
            View.OnClickListener {
                when (it.id) {
                    R.id.create_button -> {
                        val personGroupName = mSimpleDialog!!.ed_person_group_name.text.toString()
                        if (personGroupName.isNotEmpty()) {
                            addPersonGroup(personGroupName)
                            mSimpleDialog?.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Group Name Must Not Be Empty",
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

    private fun initializeListView() {
        setupRecyclerview()



/*       listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                if (!personGroupsListAdapter.longPressed) {
                    val personGroupId: String =
                        personGroupsListAdapter.personGroupIdList.get(position)
                    val personGroupName = StorageHelper.getPersonGroupName(
                        personGroupId, this@PersonGroupListActivity
                    )
                    val intent =
                        Intent(this@PersonGroupListActivity, PersonGroupActivity::class.java)
                    intent.putExtra("AddNewPersonGroup", false)
                    intent.putExtra("PersonGroupName", personGroupName)
                    intent.putExtra("PersonGroupId", personGroupId)
                    startActivity(intent)
                }
            }*/
    }


    fun addPersonGroup(personGroupName: String) {
        val personGroupId = UUID.randomUUID().toString()
        val intent = Intent(requireActivity(), PersonGroupActivity::class.java)
        intent.putExtra("AddNewPersonGroup", true)
        intent.putExtra("PersonGroupName", personGroupName)
        intent.putExtra("PersonGroupId", personGroupId)
        startActivity(intent)
        // showDialog();
    }

    fun doneAndSave(view: View?) {
        fragmentManager?.popBackStack()
    }


    private fun deleteSelectedItems(peronGroupId: String) {
        /*   val newPersonGroupIdList: MutableList<String> = ArrayList()
           val newPersonGroupChecked: MutableList<Boolean> = ArrayList()*/
        val personGroupIdsToDelete: MutableList<String> = ArrayList()


        personGroupIdsToDelete.add(peronGroupId)
        GlobalScope.launch {
            deleteLargePersonGroup(personGroupIdsToDelete, peronGroupId)
        }

    }

    override fun onResume() {
        setupRecyclerview()
        super.onResume()
    }

    private fun setupRecyclerview() {
        personGroupsListAdapter = PersonGroupsListAdapter(context, this)
        linearLayoutManager = LinearLayoutManager(context)
        rv_groups!!.setLayoutManager(linearLayoutManager)
        rv_groups!!.setAdapter(personGroupsListAdapter)


        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val persongroupid = personGroupsListAdapter!!.removeAt(viewHolder.adapterPosition)
                //Toast.makeText(requireContext(),""+ persongroupid,Toast.LENGTH_SHORT).show()
                if (persongroupid.isNotEmpty())
                    deleteSelectedItems(persongroupid)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_groups)



    }

    private suspend fun deleteLargePersonGroup(
        personGroupIdsToDelete: MutableList<String>,
        peronGroupId: String,
    ) {
        withContext(Dispatchers.IO) {
            // Get an instance of face service client.

            try {


                //   val personId = UUID.fromString(personId)
                deleteLargePersonGroupResult =
                    faceServiceClient.deleteLargePersonGroup(peronGroupId).toString()


            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

            }

            if (deleteLargePersonGroupResult != null) {
                StorageHelper.deletePersonGroups(personGroupIdsToDelete, requireContext())

                Log.e("", "Person group $deleteLargePersonGroupResult successfully deleted")

            }
        }
    }

    override fun onItemClick(view: View?, position: Int) {

        val personGroupId: String =
            personGroupsListAdapter!!.personGroupIdList.get(position)
        val personGroupName = StorageHelper.getPersonGroupName(
            personGroupId, requireActivity()
        )
/*      val bundle = Bundle()
        bundle.putBoolean("AddNewPersonGroup", false)
        bundle.putString("PersonGroupName", personGroupName)
        bundle.putString("PersonGroupId", personGroupId)

        ActivityUtils.startActivity(
            requireActivity(),
            FrameActivity::class.java,
            PersonGroupFragment::class.java.name,
            bundle
        )*/

        val intent =
            Intent(requireActivity(), PersonGroupActivity::class.java)
        intent.putExtra("AddNewPersonGroup", false)
        intent.putExtra("PersonGroupName", personGroupName)
        intent.putExtra("PersonGroupId", personGroupId)
        startActivity(intent)
    }


}