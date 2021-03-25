package com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.tech.R
import com.oasys.digihealth.tech.application.HmisApplication
import com.oasys.digihealth.tech.config.AppConstants
import com.oasys.digihealth.tech.config.AppPreferences
import com.oasys.digihealth.tech.db.UserDetailsRoomRepository
import com.oasys.digihealth.tech.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.tech.retrofitCallbacks.RetrofitMainCallback
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.assignToOtherResponse.LabAssignedToResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.labTestResponse.LabTestResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.testMethodResponse.ResponseTestMethod
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.request.ApprovalRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.request.LabTestApprovalRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovalResultResponse
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovelResultReq
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse.LabApprovalSpinnerResponseContent
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse.LabApprovalSpinnerResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabTestApprovelResponse.LabTestApprovalResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel.LabTestSpinnerResponseModel
import com.oasys.digihealth.tech.ui.login.model.SimpleResponseModel
import com.oasys.digihealth.tech.utils.Utils

import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LabTestApprovalViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {



    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()
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



    fun getLabTestApprovalList(requestLabApprovalListRequest: LabTestApprovalRequestModel, GetLabTestApprovalListRetrofitCallback: RetrofitCallback<LabTestApprovalResponseModel>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabTestApproval(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,true,
            requestLabApprovalListRequest
        )?.enqueue(RetrofitMainCallback(GetLabTestApprovalListRetrofitCallback))

    }

    fun getLabTestApprovalListSecond(labTestApprovalRequestModel: LabTestApprovalRequestModel, labTestApprovalResponseSecondRetrofitCallback: RetrofitCallback<LabTestApprovalResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getLabTestApproval(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,true,
            labTestApprovalRequestModel
        )?.enqueue(RetrofitMainCallback(labTestApprovalResponseSecondRetrofitCallback))


    }
    fun orderDetailsGet(req: LabApprovelResultReq, GetLabTestSampleListRetrofitCallback: RetrofitCallback<LabApprovalResultResponse>
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderDetailsGetLabApproval(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            req
        )?.enqueue(RetrofitMainCallback(GetLabTestSampleListRetrofitCallback))

    }
    fun getapprovalSpinner(
        labApprovalSpinnerRetrofitCallback: RetrofitCallback<LabApprovalSpinnerResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()

        try {
            jsonBody.put("table_name", "auth_status")
            jsonBody.put("sortField", "display_order")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("status",1)

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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getApprovalResultSpinner(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,AppConstants.ACCEPT_LANGUAGE_EN,
            body
        )?.enqueue(RetrofitMainCallback(labApprovalSpinnerRetrofitCallback))
        return

    }



    fun getTextMethod1(facilityId: Int, ResponseTestMethodRetrofitCallback: RetrofitCallback<LabTestSpinnerResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        try {


            jsonBody.put("pageNo", 0)
            jsonBody.put("paginationSize", 100000)
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
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

        apiService?.getLabTestSpinner(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityId!!, true,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

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

    fun orderApproved(request: ApprovalRequestModel,ResponseTestAssignedToRetrofitCallback: RetrofitCallback<SimpleResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.orderApproved(AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            request
        )?.enqueue(RetrofitMainCallback(ResponseTestAssignedToRetrofitCallback))
    }

    fun retest(id: ArrayList<Int>, orderretestRetrofitCallback: RetrofitCallback<SimpleResponseModel>) {


        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        var jsonArray = JSONArray()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        for(i in id.indices){

            jsonArray.put(id[i])
        }

        try {




            jsonBody.put("Id", jsonArray)


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

        apiService?.lmisRetest(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, this.facility_id!!, body
        )?.enqueue(RetrofitMainCallback(orderretestRetrofitCallback))



    }

}