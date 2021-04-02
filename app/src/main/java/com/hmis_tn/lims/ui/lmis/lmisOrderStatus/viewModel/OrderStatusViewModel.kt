package com.hmis_tn.lims.ui.lmis.lmisOrderStatus.viewModel

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
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusRequestModel
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusResponseModel
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusSpinnerResponseModel
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.TestNameResponseModel
import com.hmis_tn.lims.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class OrderStatusViewModel(
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

    fun getOrderStatus(
        facility_uuid: Int,
        currentPage: Int,
        pageSize: Int,
        orderstatus: Boolean,
        quisearch:String,
        pin:String,
        Patientname:String,
        fromDate:String,
        toDate:String,
        testUUID : String,
        ordStatusUUID : String,
        orderStatusRetrofitCallBack: RetrofitCallback<OrderStatusResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

            try {
                jsonBody.put("sortField", "modified_date")
                jsonBody.put("sortOrder", "DESC")
                jsonBody.put("search", quisearch)
                jsonBody.put("pageNo", currentPage)
                jsonBody.put("paginationSize", pageSize)
                if(testUUID.equals("0")){
                    jsonBody.put("test_uuid", "")
                }else{
                    jsonBody.put("test_uuid", testUUID)
                }

                if(ordStatusUUID.equals("0")){
                    jsonBody.put("order_status_uuid", "")
                }else{
                    jsonBody.put("order_status_uuid", ordStatusUUID)
                }
                jsonBody.put("uhid", pin)
                jsonBody.put("fromDate", fromDate)
                jsonBody.put("toDate", toDate)


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
        apiService?.getOrderStatus("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facility_uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(orderStatusRetrofitCallBack))
        return
    }
    fun getOrderStatusNextPage( facility_uuid:Int, currentPage: Int,
                         pageSize: Int,  orderstatus: Boolean,
                                quisearch:String,
                                pin:String,
                                Patientname:String,

                                orderStatusRetrofitCallBack: RetrofitCallback<OrderStatusResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

            try {
                jsonBody.put("sortField", "modified_date")
                jsonBody.put("sortOrder", "DESC")
                jsonBody.put("search", quisearch)
                jsonBody.put("pageNo", currentPage)
                jsonBody.put("paginationSize", pageSize)
                jsonBody.put("test_uuid", "")
                jsonBody.put("uhid", pin)


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

        apiService?.getOrderStatus("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,facility_uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(orderStatusRetrofitCallBack))
        return
    }

    fun getSelectedOrders(facility_uuid:Int, orderStatusRequestModel: OrderStatusRequestModel, ResponseTestMethodRetrofitCallback: RetrofitCallback<OrderStatusResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getSearchOrderStatus("en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_uuid!!,
            orderStatusRequestModel
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getOrderStatusTestName(facility_uuid:Int, ResponseTestMethodRetrofitCallback: RetrofitCallback<TestNameResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "test_master")
            jsonBody.put("sortField", "name")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("paginationSize", 100)
            jsonBody.put("status", 1)
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

        apiService?.getOrderStatusTestName(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getOrderStatusSpinner(facility_uuid:Int, OrderStatusRetrofitCallback: RetrofitCallback<OrderStatusSpinnerResponseModel>) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("table_name", "order_status")
            jsonBody.put("sortField", "name")
            jsonBody.put("sortOrder", "ASC")
            jsonBody.put("paginationSize", 100)
            jsonBody.put("status", 1)
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

        apiService?.getSearchOrderStatus(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_uuid!!,
            body
        )?.enqueue(RetrofitMainCallback(OrderStatusRetrofitCallback))

    }


}