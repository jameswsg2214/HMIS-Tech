package com.oasys.digihealth.tech.api_service

import android.content.Context
import com.google.gson.GsonBuilder
import com.oasys.digihealth.tech.BuildConfig.BASE_DOMAIN
import com.oasys.digihealth.tech.BuildConfig.BASE_URL
import com.oasys.digihealth.tech.config.AppConstants
import com.oasys.digihealth.tech.ui.institution.common_departmant.model.DepartmentResponseModel
import com.oasys.digihealth.tech.ui.institution.lmis.model.LocationMasterResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.DirectApprovelReq
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.GetNoteTemplateReq
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.LabTestRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.RejectRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderProcessDetailsResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderReq
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderToProcessReqestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.assignToOtherResponse.LabAssignedToResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.labTestResponse.LabTestResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.noteTemplateResponse.GetNoteTemplateResp
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.rejectReferenceResponse.RejectReferenceResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.testMethodResponse.ResponseTestMethod
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.request.ApprovalRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.request.LabTestApprovalRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovalResultResponse
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovelResultReq
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse.LabApprovalSpinnerResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabTestApprovelResponse.LabTestApprovalResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel.LabTestSpinnerResponseModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.request.SampleTransportRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.request.SendApprovalRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.request.TestProcessRequestModel
import com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.response.userProfileResponse.UserProfileResponseModel
import com.oasys.digihealth.tech.ui.lmis.sampleDispatch.model.request.DispatchReq
import com.oasys.digihealth.tech.ui.lmis.sampleDispatch.model.request.SampleDispatchRequest
import com.oasys.digihealth.tech.ui.lmis.sampleDispatch.model.response.SampleDispatchResponseModel
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


    @POST(orderProcess)
    fun orderProcess(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: OrderToProcessReqestModel?
    ): Call<SimpleResponseModel>

    @POST(DirectApprovel)
    fun directApprovel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body directApprovelReq: DirectApprovelReq?
    ): Call<SimpleResponseModel>

    @POST(GetNoteTemplateByID)
    fun getNoteTemplate(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body getNoteTemplateReq: GetNoteTemplateReq
    ): Call<GetNoteTemplateResp>


    //reject

    @POST(rejectData)
    fun rejectTestLab(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RejectRequestModel?
    ): Call<SimpleResponseModel>

    @POST(getRejectReference)
    fun getRejectReference(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<RejectReferenceResponseModel>



    @POST(orderDetailsGet)
    fun orderDetailsGet(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: OrderReq?
    ): Call<OrderProcessDetailsResponseModel>

    //search list
    @POST(GetRefrenceTestMethod)
    fun getTestMethod(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseTestMethod>

    @POST(getAssignedSpinnerList)
    fun getLabAssignedToSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: RequestBody?
    ): Call<LabAssignedToResponseModel>


    @POST(lmisretest)
    fun lmisRetest(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SimpleResponseModel>


    //Sample Dispath

    @POST(getLabTestList)
    fun getSampleDispatchList(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: SampleDispatchRequest?
    ): Call<LabTestResponseModel>

    //dispatch
    @POST(sampledispatch)
    fun dispatch(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: DispatchReq?
    ): Call<SampleDispatchResponseModel>


    @POST(getLabTestList)
    fun getLabTestApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: LabTestApprovalRequestModel?
    ): Call<LabTestApprovalResponseModel>


    @POST(GetLabSearchResult)
    fun getLabTestSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: RequestBody?
    ): Call<LabTestSpinnerResponseModel>

    @POST(orderProcessApprovel)
    fun orderApproved(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body approvalRequestModel: ApprovalRequestModel?
    ): Call<SimpleResponseModel>


    @POST(orderDetailsGetLabApproval)
    fun orderDetailsGetLabApproval(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: LabApprovelResultReq
    ): Call<LabApprovalResultResponse>



    @POST(getRejectReference)
    fun getApprovalResultSpinner(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body body: RequestBody?
    ): Call<LabApprovalSpinnerResponseModel>


    // Test Process


    @POST(getLabTestList)
    fun getLabTestProcess(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body body: TestProcessRequestModel?
    ): Call<LabTestResponseModel>

    @POST(sampleRecived)
    fun sampleRecived(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SampleTransportRequestModel?
    ): Call<SimpleResponseModel>

    @POST(getUserProfile)
    fun getUserProfile(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<UserProfileResponseModel>

    @POST(sendApprovel)
    fun sendApprovel(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: SendApprovalRequestModel?
    ): Call<SimpleResponseModel>


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
        const val orderDetailsGet =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/getOrderProcessDetails"
        const val orderProcess =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/orderProcessSave"
        const val DirectApprovel =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/saveandapproval"
        const val GetNoteTemplateByID =
            BASE_DOMAIN + "HMIS-EMR/v1/api/notetemplate/getNoteTemplateById"
        const val rejectData =
            BASE_DOMAIN + "HMIS-LIS/v1/api/sampletransportdetails/sampleRejectForAll"


        const val orderDetailsGetLabApproval =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/getOrderProcessDetails"
        const val getRejectReference =
            BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getReference"


        const val lmisretest = BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/orderProcessRetest"
        //search feilds
        const val GetRefrenceTestMethod =
            BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getAllReference"
        const val getAssignedSpinnerList =
            BASE_DOMAIN + "Appmaster/v1/api/facility/getAllFacility"

        const val GetLabSearchResult =
            BASE_DOMAIN + "HMIS-LIS/v1/api/testmaster/gettestandprofileinfo"



        //dispatch
        const val sampledispatch =
            BASE_DOMAIN + "HMIS-LIS/v1/api/sampletransportbatch/dispatchsampletransport"

        //Approved
        const val orderProcessApprovel =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/orderProcessApproval"

        //Process
        const val sampleRecived =
            BASE_DOMAIN + "HMIS-LIS/v1/api/sampletransportdetails/sampletransportreceived"

        const val getUserProfile =
            BASE_DOMAIN + "Appmaster/v1/api/userProfile/getUserProfile"

        const val sendApprovel =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/sendApprovalTestWise"

    }
}