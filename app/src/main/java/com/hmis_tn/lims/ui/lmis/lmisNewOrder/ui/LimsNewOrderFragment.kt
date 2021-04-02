package com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.lims.R
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.databinding.FragmentNewOrderBinding
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.searcPatientListResponse.NewLmisOrderModule
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.ResponseContentslmisorder
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.view_model.LmisNewOrderViewModel
import com.hmis_tn.lims.utils.CustomProgressDialog
import com.hmis_tn.lims.utils.Utils
import retrofit2.Response


class LimsNewOrderFragment : Fragment() {

    private var InputPutFieldInput: String? = ""

    var binding: FragmentNewOrderBinding? = null
    var utils: Utils? = null
    private var viewModel: LmisNewOrderViewModel? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private var mAdapter: LmisNewOrderAdapter? = null
    private var facility_id: Int = 0
    private var currentPage = 0
    private var pageSize = 10
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var appPreferences: AppPreferences? = null
    private var queryvalue: String? = ""
    private var patientType: String? = null
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var linearLayoutManager: LinearLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_order,
                container,
                false
            )

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(LmisNewOrderViewModel::class.java)

        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel

        utils = Utils(requireContext())
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        binding?.listviewlayout?.visibility = View.GONE
        customProgressDialog = CustomProgressDialog(requireContext())
        viewModel!!.progress.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog!!.show()
            } else if (progress == View.GONE) {
                customProgressDialog!!.dismiss()
            }

        })
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.searchRecyclerView!!.layoutManager = linearLayoutManager
        mAdapter = LmisNewOrderAdapter(requireContext())
        binding?.searchRecyclerView!!.adapter = mAdapter
        binding?.progressbar!!.setVisibility(View.GONE);

        binding!!.InputTypeField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize >= 10) {
                    binding!!.InputTypeField!!.error = null
                } else {
                    binding!!.InputTypeField!!.error = "Please enter minimum\nof 10 nos "
                }}
        })
        binding?.clearCardView?.setOnClickListener {
            binding?.InputTypeField?.setText("")
        }

        binding?.saveCardView?.setOnClickListener {
            binding?.progressbar!!.setVisibility(View.VISIBLE);
            InputPutFieldInput = binding?.InputTypeField?.text?.toString()
            mAdapter?.clearAll()
            currentPage = 0;
            if (InputPutFieldInput?.isEmpty()!!) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Please Enter the number"
                )
                return@setOnClickListener
            }
            viewModel?.searchPatient(
                InputPutFieldInput!!,
                currentPage,
                pageSize,
                "modified_date",
                "DESC",
                patientSearchRetrofitCallBack
            )
        }
        mAdapter!!.setOnItemClickListener(object :
            LmisNewOrderAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: ResponseContentslmisorder?, position: Int) {
                if(responseContent?.patient_visits?.size!! > 0)
                {
                    appPreferences?.saveInt(
                        AppConstants.PATIENT_UUID,
                        responseContent?.patient_visits?.get(0)?.patient_uuid!!
                    )


                    appPreferences?.saveInt(AppConstants.ENCOUNTER_TYPE,
                        responseContent?.patient_visits?.get(0)?.patient_type_uuid!!
                    )
                    val bundle = Bundle()
                    bundle.putParcelable("patiendata", responseContent)
                    bundle.putInt("salution", responseContent?.title_uuid!!)
                    val labTechnicianFragment = LmisLabTechnicianFragment()
                    labTechnicianFragment.setArguments(bundle);//passing data to fragment
                    val fragmentManager: FragmentManager? = fragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.landfragment, labTechnicianFragment)
                    fragmentTransaction.commit()
                }
                else
                {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Patient visit details Empty"
                    )
                }

            }})

        binding?.searchRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            binding?.progressbar!!.setVisibility(View.VISIBLE);
                            viewModel?.getPatientListNextPage(
                                InputPutFieldInput!!, currentPage,pageSize!!,
                                patientSearchNextRetrofitCallBack
                            )
                        }

                    }
                }
            }
        })
        return binding!!.root
    }

    val patientSearchRetrofitCallBack = object : RetrofitCallback<NewLmisOrderModule> {
        override fun onSuccessfulResponse(responseBody: Response<NewLmisOrderModule?>) {

            if(responseBody.body()?.responseContents?.isEmpty()!!){
                binding?.listviewlayout?.visibility = View.GONE
                Toast.makeText(activity,"No records found",Toast.LENGTH_LONG).show()
            }

            if (responseBody.body()?.responseContents?.isNotEmpty()!!) {
//                viewModel?.errorTextVisibility?.value = 8
                binding?.listviewlayout?.visibility = View.VISIBLE

                TOTAL_PAGES = Math.ceil(responseBody.body()!!.totalRecords!!.toDouble() / 10).toInt()
                Log.i("", "" + responseBody.body()?.responseContents)

                if (responseBody.body()!!.responseContents!!.isNotEmpty()!!) {

                    isLoadingPaginationAdapterCallback = false
                    mAdapter!!.addAll(responseBody!!.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES!!) {
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }
                } else {
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
            }
            if(responseBody!!.body()!!.totalRecords!!<11)
            {
                binding?.progressbar!!.setVisibility(View.GONE);
            }

        }

        override fun onBadRequest(errorBody: Response<NewLmisOrderModule?>) {
            isLoadingPaginationAdapterCallback = false
            binding?.progressbar!!.setVisibility(View.GONE);
            val gson = GsonBuilder().create()
            val responseModel: NewLmisOrderModule
            try {
                responseModel = gson.fromJson(
                    errorBody.errorBody()!!.string(),
                    NewLmisOrderModule::class.java
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

            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
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
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            binding?.progressbar!!.setVisibility(View.GONE);
            viewModel!!.progress.value = 8
        }
    }

    val patientSearchNextRetrofitCallBack = object : RetrofitCallback<NewLmisOrderModule> {
        override fun onSuccessfulResponse(responseBody: Response<NewLmisOrderModule?>) {
            if (responseBody.body()?.responseContents!!.isNotEmpty()!!) {
                binding?.progressbar!!.setVisibility(View.GONE);
                mAdapter!!.removeLoadingFooter()
                isLoadingPaginationAdapterCallback = false

                mAdapter?.addAll(responseBody.body()!!.responseContents)

                println("testing for two  = $currentPage--$TOTAL_PAGES")

                if (currentPage < TOTAL_PAGES!!) {
                    mAdapter?.addLoadingFooter()
                    isLoading = true
                    isLastPage = false
                    println("testing for four  = $currentPage--$TOTAL_PAGES")
                } else {
                    isLastPage = true
//                    visitHistoryAdapter.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                    println("testing for five  = $currentPage--$TOTAL_PAGES")
                }
            } else {
                println("testing for six  = $currentPage--$TOTAL_PAGES")
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true
            }
        }

        override fun onBadRequest(errorBody: Response<NewLmisOrderModule?>) {
            binding?.progressbar!!.setVisibility(View.GONE);
            mAdapter?.removeLoadingFooter()
            isLoadingPaginationAdapterCallback = false
            isLoading = false
            isLastPage = true

        }

        override fun onServerError(response: Response<*>?) {
            viewModel!!.progress.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            viewModel!!.progress.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onForbidden() {
            viewModel!!.progress.value = View.GONE
            utils!!.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            binding?.progressbar!!.setVisibility(View.GONE);
        }
    }
}