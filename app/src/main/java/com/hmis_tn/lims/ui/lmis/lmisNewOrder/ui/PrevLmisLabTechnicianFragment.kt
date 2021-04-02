package com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.lims.R
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.databinding.PrevLmisLabFragmentBinding
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.PodArrResult
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.PrevLabLmisResponseModel

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.view_model.LmisNewOrderViewModel
import com.hmis_tn.lims.utils.Utils


import retrofit2.Response


class PrevLmisLabTechnicianFragment : Fragment() {
    private var departmentID: Int?=0
    private var binding: PrevLmisLabFragmentBinding? = null
    private var viewModel: LmisNewOrderViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
//    private val favList: MutableList<RecyclerDto> = java.util.ArrayList()
    private var mAdapter: PrevLmisParentAdapter? = null
    private var mainCallback: LabPrevClickedListener?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_lmis_lab_fragment,
                container,
                false
            )
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(LmisNewOrderViewModel::class.java)

        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getPrevLabCallback(patientID,facilityid,prevlabrecordsRetrofitCallback)

        mAdapter = PrevLmisParentAdapter(requireContext(), ArrayList())
        binding?.previewRecyclerView!!.adapter = mAdapter

        mAdapter!!.setOnItemClickListener(object: PrevLmisParentAdapter.OnItemClickListener{
            override fun onItemClick(responseContent: List<PodArrResult>,isMofify:Boolean) {
                mainCallback!!.sendPrevtoChild(responseContent,departmentID,isMofify)
            }
        })
        return binding!!.root
    }
    val prevlabrecordsRetrofitCallback = object : RetrofitCallback<PrevLabLmisResponseModel> {


        override fun onSuccessfulResponse(response: Response<PrevLabLmisResponseModel?>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                viewModel?.errorText?.value = 8.toString()
                departmentID = response?.body()?.responseContents?.get(0)?.department_uuid
                val responsejson = Gson().toJson(response.body()?.responseContents)
                Log.i("",""+responsejson)
                mAdapter?.refreshList(response.body()?.responseContents!!)
            } else {
                viewModel?.errorText?.value = 0.toString()
            }}

        override fun onBadRequest(errorBody: Response<PrevLabLmisResponseModel?>) {
            val gson = GsonBuilder().create()
            val responseModel: PrevLabLmisResponseModel
            try {

                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
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
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure?:"")
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }
    interface LabPrevClickedListener {
        fun sendPrevtoChild(
            responseContent: List<PodArrResult>?,
            departmentID: Int?,isModify:Boolean
        )
    }

    fun setOnTextClickedListener(callback: LabPrevClickedListener) {
        mainCallback = callback
    }

    override fun onResume() {
        super.onResume()
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getPrevLabCallback(patientID,facilityid,prevlabrecordsRetrofitCallback)

    }

    fun refreshList(patientID: Int?,facilityid: Int?){
        viewModel?.getPrevLabCallback(patientID,facilityid,prevlabrecordsRetrofitCallback)
    }



}


