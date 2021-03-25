package com.hmis_tn.lims.ui.homepage.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hmis_tn.lims.R
import com.hmis_tn.lims.application.HmisApplication
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.db.UserDetailsRoomRepository
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.retrofitCallbacks.RetrofitMainCallback
import com.hmis_tn.lims.ui.login.model.ChangePasswordOTPResponseModel
import com.hmis_tn.lims.ui.login.model.PasswordChangeResponseModel
import com.hmis_tn.lims.ui.login.model.SimpleResponseModel
import com.hmis_tn.lims.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class HomeScreenViewModel(
    application: Application?
) : AndroidViewModel(
    application!!
) {
    fun LogoutSession(
        FacilityId: Int,
        loginSeasionRetrofitCallBack: RetrofitCallback<SimpleResponseModel>
    ) {


        if (!Utils.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        val jsonBody = JSONObject()
        try {
            jsonBody.put("session_id", userDataStoreBean?.SessionId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.LogoutSeasion(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            FacilityId,
            userDataStoreBean?.SessionId,
            body
        )?.enqueue(RetrofitMainCallback(loginSeasionRetrofitCallBack))
        return


    }



    fun getOtp(userName : String, facility_uuid: Int, otpRetrofitCallBack: RetrofitCallback<ChangePasswordOTPResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", userName)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getOtpForPasswordChange(body)!!.enqueue(
            RetrofitMainCallback(otpRetrofitCallBack)
        )
    }


    fun onChangePassword(userName: String,otp: String,passwordEncryptValue: String, changePasswordCallback:RetrofitCallback<PasswordChangeResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        if(enterOTPEditText.value!!.trim().isEmpty())
        {
            errorText.value = "Please Enter OTP"
            return
        }
        if(enterNewPasswordEditText.value!!.trim().isEmpty())
        {
            errorText.value = "Please Enter New Password"
            return
        }

        if(enterConfirmPasswordEditText.value!!.trim().isEmpty())
        {
            errorText.value = "Please Enter Confirm Password"
            return
        }

        if(enterNewPasswordEditText.value != enterConfirmPasswordEditText.value)
        {
            errorText.value = "Password Mismatch"
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", userName)
            jsonBody.put("otp", otp)
            jsonBody.put("password", passwordEncryptValue)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        progressBar.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        //val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getPasswordChanged(body)?.enqueue(RetrofitMainCallback(changePasswordCallback))
        return
    }


    var errorText = MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var enterOTPEditText = MutableLiveData<String>()
    var enterNewPasswordEditText = MutableLiveData<String>()
    var enterConfirmPasswordEditText = MutableLiveData<String>()



    init {
        progressBar.value = 8
        userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
    }

}