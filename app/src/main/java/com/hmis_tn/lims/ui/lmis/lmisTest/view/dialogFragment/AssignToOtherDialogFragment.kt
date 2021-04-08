package com.hmis_tn.lims.ui.lmis.lmisTest.view.dialogFragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.lims.R
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.databinding.DialogAssignListBinding
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.ui.institution.lmis.model.LocationMaster
import com.hmis_tn.lims.ui.institution.lmis.model.LocationMasterResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.AssignToOtherRequest.AssignToOtherRequest
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.Assigntoother
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.LabNameSearchResponseModel.LabName
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.LabNameSearchResponseModel.LabNameSearchResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.SendIdList
import com.hmis_tn.lims.ui.lmis.lmisTest.view.adapter.AssignToAdapter
import com.hmis_tn.lims.ui.lmis.lmisTest.view.adapter.LabNameAdapter
import com.hmis_tn.lims.ui.lmis.lmisTest.viewModel.LabTestViewModel
import com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.response.userProfileResponse.UserProfileResponseModel
import com.hmis_tn.lims.ui.login.model.SimpleResponseModel
import com.hmis_tn.lims.utils.Utils
import retrofit2.Response


class AssignToOtherDialogFragment : DialogFragment() {

    private var department_uuid: Int? = null
    private var facilitylevelID: Int? = null
    private var content: String? = null
    private var viewModel: LabTestViewModel? = null
    var binding: DialogAssignListBinding? = null
    private var mAdapter: AssignToAdapter? = null
    var callbackOnAssignToOtherProcess: OnAssignToOtherListener? = null
    var StatusSingle: Boolean? = null
    var currentPosition: Int = 0
    var to_facility: Int = 0
    var to_lab: Int = 0
    private var instutionView: AppCompatAutoCompleteTextView? = null
    private var labView: AppCompatAutoCompleteTextView? = null
    private var utils: Utils? = null
    private var isTablet: Boolean = false
    var appPreferences: AppPreferences? = null
    private var favouriteData: ArrayList<SendIdList> = ArrayList()
    private var ref = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
          val dialog = dialog
          if (dialog != null) {
              dialog.window!!.setLayout(
                  ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.WRAP_CONTENT
              )
              dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (dialog.window != null)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
            isCancelable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_assign_list, container, false)

        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(LabTestViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        isTablet = Utils(requireContext()).isTablet(requireContext())

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilitylevelID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancelCardView?.setOnClickListener {
            dialog?.dismiss()
        }

        if (isTablet!!) {

            val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

            binding?.assignListrecycleview!!.layoutManager = layoutmanager

            mAdapter = AssignToAdapter(requireContext(), ArrayList())

            binding?.assignListrecycleview!!.adapter = mAdapter

            mAdapter!!.setOnSearchInitiatedListener(object :
                AssignToAdapter.OnSearchInitiatedListener {
                override fun onSearchInitiated(
                    query: String,
                    view: AppCompatAutoCompleteTextView,
                    position: Int,
                    lab: AppCompatAutoCompleteTextView
                ) {

                    instutionView = view

                    labView = lab

                    currentPosition = position

                    viewModel!!.getLabName(query, LabnameResponseCallback)


                }

            })

            mAdapter!!.setOnSearchInitiatedListener(object :
                AssignToAdapter.OnSearchInitiatedListener {
                override fun onSearchInitiated(
                    query: String,
                    view: AppCompatAutoCompleteTextView,
                    position: Int,
                    lab: AppCompatAutoCompleteTextView
                ) {

                    instutionView = view

                    labView = lab

                    currentPosition = position

                    viewModel!!.getLabName(query, LabnameResponseCallback)


                }

            })

            mAdapter!!.setOnSearch(object : AssignToAdapter.OnSearch {
                override fun onSearchFunction(
                    data: Int,
                    dropdownReferenceView: AppCompatAutoCompleteTextView,
                    searchposition: Int
                ) {

                    labView = dropdownReferenceView

                    currentPosition = searchposition

                    viewModel!!.getLocationMaster(data, LocationMasterResponseCallback)

                }
            })



        } else {

            ref.clear()
            ref.put(0, "select Lab")

            val adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item_nogap,
                ref.values.toMutableList()
            )

            adapter.setDropDownViewResource(R.layout.spinner_item_nogap)

            binding?.labName!!.adapter = adapter


            binding!!.institutionSpinner!!.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {

                    val datasize = s.trim().length

                    if (datasize > 0) {

                        viewModel!!.getLabName(s.toString(), LabnameResponseCallback)

                    }

                }
            })
        }

        binding!!.saveCardView.setOnClickListener {


            if (isTablet) {

                val data = mAdapter!!.getAll()

                val check = data.any { it!!.to_facility == 0 }

                val check2 = data.any { it!!.to_location_uuid == "" }

                if (check || check2) {

                    Toast.makeText(this.context, "Please Fill all field", Toast.LENGTH_SHORT).show()

                } else {

                    Log.e("Save", "save")

                    val request: AssignToOtherRequest = AssignToOtherRequest()

                    var details: ArrayList<Assigntoother> = ArrayList()


                    if (StatusSingle!!) {

                        for (i in favouriteData.indices) {

                            val dat: Assigntoother = Assigntoother()
                            dat.facility_uuid = data[0].facility_uuid
                            dat.id = data[0].id
                            dat.testname = data[0].testname
                            dat.to_facility = data[0].to_facility
                            dat.to_location_uuid = data[0].to_location_uuid

                            details.add(dat)

                        }

                    } else {
                        for (i in favouriteData.indices) {

                            val dat: Assigntoother = Assigntoother()
                            dat.facility_uuid = data[i].facility_uuid
                            dat.id = data[i].id
                            dat.testname = data[i].testname
                            dat.to_facility = data[i].to_facility
                            dat.to_location_uuid = data[i].to_location_uuid

                            details.add(dat)

                        }

                    }

                    request.details = details

                    viewModel!!.assigntoOther(request, saveRetrofitCallback)

                }

            }
            else{

                val request: AssignToOtherRequest = AssignToOtherRequest()

                var details: ArrayList<Assigntoother> = ArrayList()

                for (i in favouriteData.indices) {

                    val dat: Assigntoother = Assigntoother()
//                    dat.facility_uuid = favouriteData[i].facility_uuid
                    dat.id = favouriteData[i].Id
                    dat.testname = favouriteData[i].name
                    dat.to_facility =to_facility
                    dat.to_location_uuid = to_lab.toString()

                    details.add(dat)

                }

                request.details = details

                viewModel!!.assigntoOther(request, saveRetrofitCallback)


            }
        }



        val args = arguments

        if (args == null) {

        } else {
            // get value from bundle..
            favouriteData = args.getParcelableArrayList<SendIdList>(AppConstants.RESPONSECONTENT)!!

            StatusSingle = args.getBoolean(AppConstants.RESPONSENEXT)!!

            val list: ArrayList<Assigntoother> = ArrayList()


            if (isTablet) {

                if (StatusSingle!!) {

                    val data: Assigntoother = Assigntoother()

                    data.id = favouriteData[0].Id

                    data.testname = favouriteData[0].name

                    list.add(data)

                } else {


                    for (i in favouriteData.indices) {

                        val data: Assigntoother = Assigntoother()
                        data.id = favouriteData[i].Id
                        data.testname = favouriteData[i].name
                        list.add(data)

                    }

                }

                mAdapter!!.setData(list)

            } else {


                if (favouriteData.size == 1) {
                    binding?.testName?.setText(
                        favouriteData[0].name
                    )
                } else {

                    for (i in favouriteData!!.indices) {

                        if (binding?.testName?.text != "") {
                            binding?.testName?.text =
                                "" + binding?.testName?.text + " , " + favouriteData[i].name
                        } else {

                            binding?.testName?.text = favouriteData[i].name

                        }

                    }
                }


            }


        }


        return binding?.root
    }


    val saveRetrofitCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {


            Toast.makeText(context, responseBody?.body()?.message, Toast.LENGTH_SHORT).show()

            callbackOnAssignToOtherProcess!!.onRefreshAssignToOrderList()

            try {

                dialog!!.dismiss()

            } catch (e: Exception) {

            }

        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: UserProfileResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    UserProfileResponseModel::class.java
                )

            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel?>) {
            Log.i("", "locationdata" + responseBody!!.body()!!.responseContents)
            val data = responseBody!!.body()!!.responseContents

            if (data.isNotEmpty()) {

                if (isTablet)
                    mAdapter!!.setlabAdapter(
                        labView!!,
                        data as ArrayList<LocationMaster>,
                        currentPosition
                    )
                else
                    setLabList(data as ArrayList<LocationMaster>)
            }

            //    labnameAdapter(responseBody!!.body()!!.responseContents)
        }

        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }

        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {

            viewModel!!.progress.value = 8


        }
    }


    val LabnameResponseCallback = object : RetrofitCallback<LabNameSearchResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LabNameSearchResponseModel?>) {


            if (isTablet)
                mAdapter!!.setAdapter(
                    instutionView!!,
                    responseBody!!.body()!!.responseContents,
                    currentPosition,
                    labView!!
                )
            else
                setInstutionAdapter(responseBody!!.body()!!.responseContents)


        }

        override fun onBadRequest(errorBody: Response<LabNameSearchResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: LabNameSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LabNameSearchResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.status!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }

        }

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun setInstutionAdapter(responseContents: ArrayList<LabName>) {


        val responseContentAdapter = LabNameAdapter(
            requireContext(),
            R.layout.spinner_item,
            responseContents
        )
        binding!!.institutionSpinner!!.threshold = 1
        binding!!.institutionSpinner!!.setAdapter(responseContentAdapter)
        binding!!.institutionSpinner!!.showDropDown()

        binding!!.institutionSpinner!!.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as LabName?

            binding!!.institutionSpinner!!.setText(selectedPoi!!.name)

            to_facility = selectedPoi.uuid
            /*      lab.isEnabled=true

                  labTestList[searchposition].to_facility=selectedPoi.uuid*/

            viewModel!!.getLocationMaster(selectedPoi.uuid, LocationMasterResponseCallback)


        }

    }

    private fun setLabList(arrayList: ArrayList<LocationMaster>) {
        ref.clear()
        ref.put(0, "select Lab")
        ref.putAll(arrayList?.map { it?.uuid!! to it.location_name!! }!!.toMap().toMutableMap())

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item_nogap,
            ref.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item_nogap)

        binding?.labName!!.adapter = adapter

        binding?.labName?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    to_lab = ref.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    to_lab = ref.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


    }


    fun setOnAssignToOtherRefreshListener(callback: OnAssignToOtherListener) {
        callbackOnAssignToOtherProcess = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnAssignToOtherListener {
        fun onRefreshAssignToOrderList()
    }

}

