package com.oasys.digihealth.tech.ui.lmis.lmisTest.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.oasys.digihealth.tech.R
import com.oasys.digihealth.tech.application.HmisApplication
import com.oasys.digihealth.tech.config.AppConstants
import com.oasys.digihealth.tech.config.AppPreferences
import com.oasys.digihealth.tech.db.UserDetailsRoomRepository
import com.oasys.digihealth.tech.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.tech.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.DirectApprovelReq
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.GetNoteTemplateReq
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.LabTestRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.RejectRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderProcessDetailsResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderReq
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderToProcessReqestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.labTestResponse.LabTestResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.noteTemplateResponse.GetNoteTemplateResp
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.orderProcessResponse.OrderProcessResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.rejectReferenceResponse.RejectReferenceResponseModel
import com.oasys.digihealth.tech.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.tech.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class LabTestViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {



    var progress = MutableLiveData<Int>()
    var errorText = MutableLiveData<String>()


    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var facility_id:Int?=0
    var appPreferences: AppPreferences? = null



    init {

        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)

        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        //progress.value = 8
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
    }



    fun getLabList(requestLabRequest: LabTestRequestModel, GetLabTestListRetrofitCallback: RetrofitCallback<LabTestResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabTestList(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,true,
            requestLabRequest
        )?.enqueue(RetrofitMainCallback(GetLabTestListRetrofitCallback))

    }


    fun orderProcess(req: OrderToProcessReqestModel, GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderProcess(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun orderDetailsGet(req: OrderReq, GetLabTestSampleListRetrofitCallback: RetrofitCallback<OrderProcessDetailsResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderDetailsGet(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }


    fun orderDirectApprovel(req: DirectApprovelReq, GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.directApprovel(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }


    fun getNoteTemplate(
        facility_uuid: Int,
        getNoteTemplateReq: GetNoteTemplateReq,
        getNoteTemplateRespCallback: RetrofitCallback<GetNoteTemplateResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getNoteTemplate(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getNoteTemplateReq
        )?.enqueue(RetrofitMainCallback(getNoteTemplateRespCallback))
    }

    fun rejectLabTest(req: RejectRequestModel, GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.rejectTestLab(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }


    fun getRejectReference(labTestResponseSecondRetrofitCallback: RetrofitCallback<RejectReferenceResponseModel>)
    {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        val jsonBody = JSONObject()

        try {
            jsonBody.put("table_name", "reject_category")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getRejectReference(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(labTestResponseSecondRetrofitCallback))

    }


/*



    fun getLabSampleAcceptance(requestLabTestSampleRequest: SampleAcceptedRequest, GetLabTestSampleListRetrofitCallback: RetrofitCallback<SampleAcceptanceResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getSampleAccept(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,true,
            requestLabTestSampleRequest
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }







    fun orderProcess(GetLabTestSampleListRetrofitCallback: RetrofitCallback<UserProfileResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()

        try {

            jsonBody.put("search", "%%%")
            jsonBody.put("facility_uuid", facility_id)
            jsonBody.put("is_result_approve", true)
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("is_active",1)
            jsonBody.put("usertypeId",6)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getUserProfile(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }



    fun rapidSave(req: LabrapidSaveRequestModel, GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.rapidSave(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun assigntoOther(req: AssigntootherRequest, GetLabTestSampleListRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderSendtonext(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }

    fun getLocationMaster(facility:Int,stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facility)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

//        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLocationMaster(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getLabListSecond(labTestRequestModel: LabTestRequestModel, labTestResponseSecondRetrofitCallback: RetrofitCallback<LabTestResponseModel>)
    {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabTestList(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,true,
            labTestRequestModel
        )?.enqueue(RetrofitMainCallback(labTestResponseSecondRetrofitCallback))

    }


    fun getTextMethod1(facilityId: Int, ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {
            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 1000)
            jsonBody.put("status", 1)
            jsonBody.put("table_name", "type_of_method")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("sortField", "display_order")
            jsonBody.put("search", "")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getTestMethod(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }



    fun getLabName(search:String, GetPDFRetrofitCallback: RetrofitCallback<LabNameSearchResponseModel>) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val jsonBody = JSONObject()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        try {
            jsonBody.put("searchKey", "search")
            jsonBody.put("search", search)
            jsonBody.put("pageNo", 0)
            jsonBody.put("is_lab_center",true)
            jsonBody.put("paginationSize",10 )


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

       // progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabname(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,body
        )?.enqueue(RetrofitMainCallback(GetPDFRetrofitCallback))

    }


    fun getTextAssignedTo(facilityId: Int, ResponseTestAssignedToRetrofitCallback: RetrofitCallback<LabAssignedToResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {

            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("sortField", "uuid")
            jsonBody.put("status", "1")
            jsonBody.put("pageNo", "0")
            jsonBody.put("paginationSize", 100)
            jsonBody.put("search", "")



        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabAssignedToSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId, true,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))

    }

*/

}