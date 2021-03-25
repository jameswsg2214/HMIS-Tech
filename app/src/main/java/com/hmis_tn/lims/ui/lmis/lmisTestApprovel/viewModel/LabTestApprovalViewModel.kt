package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.lims.R
import com.hmis_tn.lims.application.HmisApplication
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.db.UserDetailsRoomRepository
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.assignToOtherResponse.LabAssignedToResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.labTestResponse.LabTestResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.testMethodResponse.ResponseTestMethod
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.request.ApprovalRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.request.LabTestApprovalRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovalResultResponse
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovelResultReq
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse.LabApprovalSpinnerResponseContent
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse.LabApprovalSpinnerResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestApprovelResponse.LabTestApprovalResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel.LabTestSpinnerResponseModel
import com.hmis_tn.lims.ui.login.model.SimpleResponseModel
import com.hmis_tn.lims.utils.Utils

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