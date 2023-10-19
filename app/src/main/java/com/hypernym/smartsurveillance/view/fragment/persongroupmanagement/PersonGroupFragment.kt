package com.hypernym.smartsurveillance.view.fragment.persongroupmanagement


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hypernym.smartsurveillance.R
import com.hypernym.smartsurveillance.adapter.PersonGridViewAdapter
import com.hypernym.smartsurveillance.dialogs.CreatePersonDialog
import com.hypernym.smartsurveillance.helper.StorageHelper
import com.hypernym.smartsurveillance.listeners.OnOneOffClickListener
import com.hypernym.smartsurveillance.utilities.AppConstants
import com.hypernym.smartsurveillance.utilities.AppUtils
import com.hypernym.smartsurveillance.view.fragment.BaseFragment
import com.hypernym.smartsurveillance.view.persongroupmanagement.PersonActivity
import com.microsoft.projectoxford.face.FaceServiceClient
import com.microsoft.projectoxford.face.FaceServiceRestClient
import kotlinx.android.synthetic.main.fragment_person_group.*
import kotlinx.android.synthetic.main.fragment_person_group.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import qa.vodafone.fleetdispatcherapp.utilities.GridItemDecoration
import java.util.*


class PersonGroupFragment : BaseFragment(),PersonGridViewAdapter.OnItemClickListener {


    private var createGroupResult: String? = null
    private var deletePersonResult: String? = null
    private var trainResult: String? = null
    private var personGridViewAdapter: PersonGridViewAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var personGroupId:String? = null
    private var personGroupName:String? = null
    var personGroupExists = false
    var mSimpleDialog: CreatePersonDialog? = null

    // Progress dialog popped up when communicating with server.
    var progressDialog: ProgressDialog? = null

    val faceServiceClient: FaceServiceClient = FaceServiceRestClient(
        AppConstants.AZURE_ENDPOINT,
        AppConstants.AZURE_SUBCRIPTION_KEY
    )
    override fun onResume() {
        super.onResume()
        AppUtils.fragmentName = PersonGroupListFragment::class.java.name
        context?.let { onAttach(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            // toolbar_title.setText(it.resources.getString(R.string.admin_page_title))
            setToolbarTitle(it, it.resources.getString(R.string.group_page_title))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // fetch bundle passed to this fragment.
        // JobsFragment -> onItemClick
        arguments?.let {
            personGroupId = it.getString("PersonGroupId")
            personGroupName = it.getString("PersonGroupName")
        }
        initRecyclerView()
        add_person.setOnClickListener(object : OnOneOffClickListener(){
            override fun onSingleClick(view: View?) {
                GlobalScope.launch {  addPerson() }

            }
        })
        done_and_save.setOnClickListener(object : OnOneOffClickListener(){
            override  fun onSingleClick(view: View?) {
               GlobalScope.launch {  doneAndSave() }
            }
        })

        back?.setOnClickListener(object:OnOneOffClickListener(){
            override fun onSingleClick(view: View?) {
               fragmentManager?.popBackStack()
            }
        })
    }

    private fun initRecyclerView() {
        personGridViewAdapter = PersonGridViewAdapter(personGroupId,context, this)
        rv_persons.layoutManager = GridLayoutManager(context, 3)
        rv_persons.addItemDecoration(GridItemDecoration(2, 3))
        rv_persons!!.setAdapter(personGridViewAdapter)
    }

    private suspend fun addPerson() {
       /* if (!personGroupExists) {
            addPeronGroupProcess()

        } else {*/
            gotoAddPerson()
      //  }
    }

    private fun gotoAddPerson() {
        setInfo("")
        val intent = Intent(requireContext(), PersonActivity::class.java)
        intent.putExtra("AddNewPerson", true)
        intent.putExtra("PersonName", "")
        intent.putExtra("PersonGroupId", personGroupId)
        startActivity(intent)
    }


    suspend fun doneAndSave() {
        if (!personGroupExists) {
          //  AddPersonGroupTask(false).execute(personGroupId)
        } else {
            doneAndSave(true)
        }
    }

    private suspend fun doneAndSave(trainPersonGroup: Boolean) {
      /*  val newPersonGroupName = edit_person_group_name.text.toString()
        if (newPersonGroupName == "") {
            setInfo("Person group name could not be empty")
            return
        }
        StorageHelper.setPersonGroupName(
            personGroupId,
            newPersonGroupName,
            requireContext()
        )*/
        if (trainPersonGroup) {
            GlobalScope.launch {
                trainPersonGroupProcess()
            }

        } else {
            fragmentManager?.popBackStack()
        }
    }

    private suspend fun deleteSelectedItems() {
        val newPersonIdList: MutableList<String> = ArrayList()
        val newPersonChecked: MutableList<Boolean> = ArrayList()
        val personIdsToDelete: MutableList<String> = ArrayList()
        for (i in personGridViewAdapter!!.personChecked.indices) {
            if (personGridViewAdapter!!.personChecked[i]) {
                val personId = personGridViewAdapter!!.personIdList[i]
                personIdsToDelete.add(personId)
                deletePersonGroupProcess(personId)
            } else {
                newPersonIdList.add(personGridViewAdapter!!.personIdList[i])
                newPersonChecked.add(false)
            }
        }
        StorageHelper.deletePersons(personIdsToDelete, personGroupId, requireContext())
        personGridViewAdapter!!.personIdList = newPersonIdList
        personGridViewAdapter!!.personChecked = newPersonChecked
        personGridViewAdapter!!.notifyDataSetChanged()
    }



    private suspend fun deletePersonGroupProcess(personId: String) {
        withContext(Dispatchers.IO){
            // Get an instance of face service client.

            try {
                Toast.makeText(requireContext(),"Deleting selected persons...",Toast.LENGTH_SHORT).show()
                deletePersonResult = faceServiceClient.trainLargePersonGroup(personGroupId).toString()

                val personId = UUID.fromString(personId)
                faceServiceClient.deletePersonInLargePersonGroup(personGroupId, personId)


            } catch (e: Exception) {
                Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()

            }

            if (deletePersonResult != null) {
                Toast.makeText(requireContext(),"Person $deletePersonResult successfully deleted",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun trainPersonGroupProcess() {
        withContext(Dispatchers.IO){
            // Get an instance of face service client.

             try {
               Toast.makeText(requireContext(),"Training person group...",Toast.LENGTH_SHORT).show()
               trainResult = faceServiceClient.trainLargePersonGroup(personGroupId).toString()

            } catch (e: Exception) {
                 Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()

            }

            if (trainResult != null) {
                Toast.makeText(requireContext(),"Response: Success. Group $trainResult training completed",Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Background task of adding a person group.
    private suspend fun addPeronGroupProcess() {
        withContext(Dispatchers.Main){
            // Get an instance of face service client.

            try {
                Toast.makeText(requireContext(),"Request: Creating person group",Toast.LENGTH_SHORT).show()
                val nameStr: String
                val descriptionStr: String


                nameStr = ""
                descriptionStr = ""


                createGroupResult = faceServiceClient.createLargePersonGroup(
                    personGroupId,
                    nameStr,
                    descriptionStr).toString()

            } catch (e: Exception) {
//                Toast.makeText(requireActivity(),e.message,Toast.LENGTH_SHORT).show()
                Log.e(TAG, e.message.toString())
            }

            if (createGroupResult != null) {

                Toast.makeText(requireActivity(),"Response: Success. Group $createGroupResult training completed",Toast.LENGTH_SHORT).show()
            }
        }
    }



    // Set the information panel on screen.
    private fun setInfo(info: String) {
/*        val textView = findViewById<View>(R.id.info) as TextView
        textView.text = info*/
    }

    private suspend fun showDialog(context: Context) {
        mSimpleDialog = CreatePersonDialog(
            context,
            View.OnClickListener {
                when (it.id) {
                    R.id.create_button -> {

                        mSimpleDialog?.dismiss()
                    }
                    R.id.button_negative -> {
                        mSimpleDialog?.dismiss()

                    }
                }

            }
        )
        mSimpleDialog?.show()
        mSimpleDialog?.setCanceledOnTouchOutside(true)
        mSimpleDialog?.setCancelable(true)
    }

    override fun onItemClick(view: View?, position: Int) {
        val personId = personGridViewAdapter!!.personIdList[position]
        val personName = StorageHelper.getPersonName(
            personId, personGroupId, requireContext()
        )

        val intent = Intent(requireContext(), PersonActivity::class.java)
        intent.putExtra("AddNewPerson", false)
        intent.putExtra("PersonName", personName)
        intent.putExtra("PersonId", personId)
        intent.putExtra("PersonGroupId", personGroupId)
        startActivity(intent)
    }
    companion object{
        val TAG = "PersonGroupFragment"
    }

}