package com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.lims.R
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.databinding.FragmentLmisLabTemplateBinding
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.view_model.LmisNewOrderViewModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList.LabDetail
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList.TempDetails
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList.TemplatesLab
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList.TempleResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui.refInterface.ClearTemplateParticularPositionListener
import com.hmis_tn.lims.ui.login.model.SimpleResponseModel
import com.hmis_tn.lims.utils.Utils
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTemplate.ResponseLabGetTemplateDetails

import retrofit2.Response


class LmisLabTechnicianTemplateFragment : Fragment(),ManageLmisLabTemplateFragment.OnTemplateRefreshListener,
    ClearTemplateParticularPositionListener {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0
    lateinit var templeteAdapter: LmisLabTechnicianTempleteAdapter
    var mCallback: TempleteClickedListener? =null
    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentLmisLabTemplateBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: LmisNewOrderViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var Lab_uuid : Int?=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lmis_lab_template,
                container,
                false
            )


        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(LmisNewOrderViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences = AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        Lab_uuid = appPreferences?.getInt(AppConstants.LAB_UUID)

        utils = Utils(requireContext())

        binding?.manageFavouritesCardView?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val managedialog = ManageLmisLabTemplateFragment()
            managedialog.setOnTemplateRefreshListener(this)
            managedialog.show(ft, "Tag")

        }
        initTempleteAdapter()
        viewModel!!.getTemplete(getTempleteRetrofitCallBack,Lab_uuid)


        return binding!!.root
    }

    private fun initTempleteAdapter() {
        templeteAdapter =
            LmisLabTechnicianTempleteAdapter(requireContext())
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager = gridLayoutManager
        binding?.recyclerView?.adapter = templeteAdapter
        templeteAdapter.setOnItemClickListener(object :
            LmisLabTechnicianTempleteAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: List<LabDetail?>?,
                position: Int,
                selected: Boolean,
                id:Int
            ) {

                templeteAdapter.updateSelectStatus(position, selected)
                mCallback?.sendTemplete(responseContent!!,position,selected,id)
            }
        })

        templeteAdapter.setOnItemViewClickListener(object :
            LmisLabTechnicianTempleteAdapter.OnItemViewClickListner {
            override fun onItemClick(responseContent: TempDetails?) {

             //   viewModel?.getTemplateDetails(responseContent?.template_id,facility_UUID,Lab_uuid!!,getTemplateRetrofitResponseCallback)
            }
        })

        templeteAdapter.setOnItemDeleteClickListener(object :
            LmisLabTechnicianTempleteAdapter.OnItemDeleteClickListner {
            override fun onItemClick(
                responseContent: TemplatesLab?
            ) {
                customdialog = Dialog(requireContext())
                customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
                customdialog!! .setCancelable(false)
                customdialog!! .setContentView(R.layout.delete_cutsom_layout)
                val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView
                closeImageView.setOnClickListener {
                    customdialog!!.dismiss()
                }
                val drugNmae = customdialog!! .findViewById(R.id.addDeleteName) as TextView

                drugNmae.text ="${drugNmae.text} '"+responseContent?.temp_details?.template_name+"' Record ?"

                val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView
                val noBtn = customdialog!! .findViewById(R.id.no) as CardView
                yesBtn.setOnClickListener {
                    viewModel!!.deleteTemplate(
                        facility_UUID,
                        responseContent?.temp_details!!.template_id!!,
                        deleteRetrofitResponseCallback
                    )
                }
                noBtn.setOnClickListener {
                    customdialog!! .dismiss()
                }
                customdialog!! .show()
            }
        })
        templeteAdapter.setOnItemViewClickListener(object : LmisLabTechnicianTempleteAdapter.OnItemViewClickListner{
            override fun onItemClick(responseContent: TempDetails?) {
                viewModel?.getTemplateDetails(responseContent?.template_id,facility_UUID,Lab_uuid,getTemplateRetrofitResponseCallback)
            }
        })
    }
    val getTempleteRetrofitCallBack =
        object : RetrofitCallback<TempleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<TempleResponseModel?>) {
                templeteAdapter.refreshList(responseBody.body()?.responseContents?.templates_lab_list)
            }

            override fun onBadRequest(errorBody: Response<TempleResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: TempleResponseModel
                try {

                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.bad)
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

    interface TempleteClickedListener {
        fun sendTemplete(
            templeteDetails: List<LabDetail?>?,
            position: Int,
            selected: Boolean,
            id:Int
        )
    }
    fun setOnTextClickedListener(callback: TempleteClickedListener) {
        this.mCallback = callback
    }
    override fun onRefreshList() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack,Lab_uuid)
    }
    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ManageLmisLabTemplateFragment) {
            childFragment.setOnTemplateRefreshListener(this)
        }}
    var deleteRetrofitResponseCallback = object : RetrofitCallback<SimpleResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {
            viewModel!!.getTemplete(getTempleteRetrofitCallBack,Lab_uuid)
            customdialog!! .dismiss()

            Log.e("DeleteResponse", responseBody?.body().toString())
        }
        override fun onBadRequest(errorBody: Response<SimpleResponseModel?>) {

        }
        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
        }}
    /*
      Get Template
     */
    var getTemplateRetrofitResponseCallback = object : RetrofitCallback<ResponseLabGetTemplateDetails> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseLabGetTemplateDetails?>) {

            val ft = childFragmentManager.beginTransaction()
            val labtemplatedialog = ManageLmisLabTemplateFragment()
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.RESPONSECONTENT, responseBody?.body()?.responseContent)
            labtemplatedialog.arguments = bundle
            labtemplatedialog.show(ft, "Tag")

        }
        override fun onBadRequest(errorBody: Response<ResponseLabGetTemplateDetails?>) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {

        }

    }

    override fun ClearTemplateParticularPosition(position: Int) {
        templeteAdapter.refreshParticularData(position)
    }

    override fun ClearAllData() {
        templeteAdapter.refreshAllData()
    }

    override fun GetTemplateDetails() {
        viewModel!!.getTemplete(getTempleteRetrofitCallBack,Lab_uuid)
    }

    override fun updatestaus(favitem: Int, position: Int, select: Boolean) {

    }

    override fun checkanduncheck(dataList: ArrayList<Int>) {

    }


}


