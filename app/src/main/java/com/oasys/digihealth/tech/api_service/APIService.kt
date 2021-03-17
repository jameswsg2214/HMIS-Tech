package com.oasys.digihealth.tech.api_service

import android.content.Context
import com.google.gson.GsonBuilder
import com.oasys.digihealth.tech.BuildConfig.BASE_DOMAIN
import com.oasys.digihealth.tech.BuildConfig.BASE_URL
import com.oasys.digihealth.tech.config.AppConstants
import com.oasys.digihealth.tech.ui.institution.common_departmant.model.DepartmentResponseModel
import com.oasys.digihealth.tech.ui.institution.lmis.model.LocationMasterResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.LabTestRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.labTestResponse.LabTestResponseModel
import com.oasys.digihealth.tech.ui.login.model.*
import com.oasys.digihealth.tech.ui.login.model.institution_response.InstitutionResponseModel
import com.oasys.digihealth.tech.ui.login.model.login_response_model.LoginResponseModel
import com.oasys.digihealth.tech.ui.login.model.office_response.OfficeResponseModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface APIService {
    object Factory {
        fun create(context: Context?): APIService {
            val b = OkHttpClient.Builder()
            b.readTimeout(
                AppConstants.TIMEOUT_VALUE.toLong(),
                TimeUnit.MILLISECONDS
            )
            b.writeTimeout(
                AppConstants.TIMEOUT_VALUE.toLong(),
                TimeUnit.MILLISECONDS
            )
            val gson = GsonBuilder()
                .serializeNulls()
                .setLenient()
                .create()
//            if


//            {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
//                val client = b.addNetworkInterceptor(StethoInterceptor()).build()
            val client = b.addInterceptor(ChuckInterceptor(context))
                .addInterceptor(logging)
                .build()
//            }
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }

    /*          Login                */
    @FormUrlEncoded
    @POST(Login)
    fun getLoginDetails(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<LoginResponseModel?>?

    @POST(setPassword)
    fun setPassword(
        @Body requestbody: RequestBody?
    ): Call<SimpleResponseModel?>?

    @POST(GetOtpForPasswordChange)
    fun getOtpForPasswordChange(@Body body: RequestBody?): Call<ChangePasswordOTPResponseModel>


    @POST(GetPasswordChanged)
    fun getPasswordChanged(@Body body: RequestBody?): Call<PasswordChangeResponseModel>


    @POST(LoginSession)
    fun LoginSession(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: String,
        @Header("session_id") session_id: String?,
        @Body req: LoginSessionRequest
    ): Call<SimpleResponseModel>

    // get institutiom
    @POST(Getnstitutions)
    fun getFaciltyList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Body body: RequestBody?
    ): Call<InstitutionResponseModel>

    // Get Department

    @POST(GetDepartmentList)
    fun getDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<DepartmentResponseModel>


    // office user Data
    @POST(getLocationMaster)
    fun getLocationMasterLogin(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModel>

    @POST(GetOfficeList)
    fun getOfficeList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<OfficeResponseModel>

    @POST(GetInstitutionList)
    fun getInstitutionList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<InstitutionResponseModel>


    @POST(getRmisLocationMaster)
    fun getRmisLocationMaster(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModel>


    /* --------------- Home Page --------------- */

    @POST(LogoutSession)
    fun LogoutSeasion(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("session_id") session_id: String?,
        @Body req: RequestBody
    ): Call<SimpleResponseModel>



    /* ---------- lmis ---------------*/

    // LMIS TEST List

    @POST(getLabTestList)
    fun getLabTestList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: LabTestRequestModel?
    ): Call<LabTestResponseModel>


/*

    */
    /**
     * Get Department List
     *
     * @param authorization
     * @param user_uuid
     * @param body
     * @return
     *//*

    @POST(GetDepartmentList)
    fun getDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<DepartmentResponseModel?>?

    @POST(GetStoreList)
    fun getStoreList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int, @Body body: RequestBody?
    ): Call<StoreListResponseModel?>?
*/


    companion object {

        const val Login =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/loginNew"
        const val setPassword =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/newUser_changePassword"
        const val GetOtpForPasswordChange =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/sendOtp"
        const val GetPasswordChanged =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/changePassword"
        const val LoginSession =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/login_session"
        const val LogoutSession =
            BASE_DOMAIN + "HMIS-Login/1.0.0/api/authentication/is_current_loginuser"




        // select Institution

        const val GetOfficeList =
            BASE_DOMAIN + "Appmaster/v1/api/facility/getUserOfficeByUserId"
        const val Getnstitutions =
            BASE_DOMAIN + "Appmaster/v1/api/userFacility/getUserFacilityByUId"
        const val GetDepartmentList =
            BASE_DOMAIN + "Appmaster/v1/api/manageInstitution/getManageInstitutionByUFId"
        const val getLocationMaster =
            BASE_DOMAIN + "HMIS-LIS/v1/api/tolocationmaster/gettolocationmasterbyfacilityid"
        const val GetInstitutionList =
            BASE_DOMAIN + "Appmaster/v1/api/facility/getFacilityByHealthOfficeId"
        const val getRmisLocationMaster =
            BASE_DOMAIN + "HMIS-RMIS/v1/api/tolocationmaster/gettolocationmasterbyfacilityid"


        /*LMIS*/

        const val getLabTestList =
            BASE_DOMAIN + "HMIS-LIS/v1/api/viewlabtest/getviewlabtest"



    }
}