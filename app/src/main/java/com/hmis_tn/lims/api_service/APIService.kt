package com.hmis_tn.lims.api_service

import android.content.Context
import com.google.gson.GsonBuilder
import com.hmis_tn.lims.BuildConfig.BASE_DOMAIN
import com.hmis_tn.lims.BuildConfig.BASE_URL
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.ui.institution.common_departmant.model.DepartmentResponseModel
import com.hmis_tn.lims.ui.institution.lmis.model.LocationMasterResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.DoctorNameResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.LabModifiyRequest
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.LabModifiyResponse
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.PrevLabLmisResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.LabTechSearch
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.RequestLmisNewOrder
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.searcPatientListResponse.NewLmisOrderModule
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.SearchPatientRequestModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest.CreateEncounterRequestModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.requestLabFav.RequestLabFavModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.templateRequest.RequestTemplateAddDetails
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.updateRequest.UpdateRequestModule
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.FavAddListResponse.FavAddListResponse
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.GetToLocationTestResponse.GetToLocationTestResponse
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.GetWardIdResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.LabFavManageResponse.LabFavManageResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.ResponseLmisListview
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.createEncounterResponse.CreateEncounterResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.favAddTestNameResponse.FavAddTestNameResponse
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.favEditResponse.FavEditResponse
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.fetchEncountersResponse.FectchEncounterResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getDepaetmantList.FavAddAllDepatResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getFavouriteList.FavouritesResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getReferenceResponse.GetReferenceResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTemplate.ResponseLabGetTemplateDetails
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList.TempleResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.templateResponse.ReponseTemplateadd
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusRequestModel
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusResponseModel
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusSpinnerResponseModel
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.TestNameResponseModel
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model.ResponseResultDispatch
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.request.RequestDispatchSearch
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.AssignToOtherRequest.AssignToOtherRequest
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.DirectApprovelReq
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.GetNoteTemplateReq
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.LabTestRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.LabrapidSaveRequestModel.LabrapidSaveRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.RejectRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.SampleAcceptedRequest.SampleAcceptedRequest
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest.OrderProcessDetailsResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest.OrderReq
import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest.OrderToProcessReqestModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.LabNameSearchResponseModel.LabNameSearchResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.assignToOtherResponse.LabAssignedToResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.labTestResponse.LabTestResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.noteTemplateResponse.GetNoteTemplateResp
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.rejectReferenceResponse.RejectReferenceResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.testMethodResponse.ResponseTestMethod
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.request.ApprovalRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.request.LabTestApprovalRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovalResultResponse
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse.LabApprovelResultReq
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse.LabApprovalSpinnerResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestApprovelResponse.LabTestApprovalResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel.LabTestSpinnerResponseModel
import com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.request.SampleTransportRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.request.SendApprovalRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.request.TestProcessRequestModel
import com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.response.userProfileResponse.UserProfileResponseModel
import com.hmis_tn.lims.ui.lmis.sampleDispatch.model.request.DispatchReq
import com.hmis_tn.lims.ui.lmis.sampleDispatch.model.request.SampleDispatchRequest
import com.hmis_tn.lims.ui.lmis.sampleDispatch.model.response.SampleDispatchResponseModel
import com.hmis_tn.lims.ui.login.model.*
import com.hmis_tn.lims.ui.login.model.institution_response.InstitutionResponseModel
import com.hmis_tn.lims.ui.login.model.login_response_model.LoginResponseModel
import com.hmis_tn.lims.ui.login.model.office_response.OfficeResponseModel
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


    @POST(getSampleAcceptance)
    fun getSampleAccept(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body sampleAcceptedRequest: SampleAcceptedRequest?
    ): Call<SimpleResponseModel>

    @POST(rapidSave)
    fun rapidSave(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: LabrapidSaveRequestModel?
    ): Call<SimpleResponseModel>


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

    @POST(orderSendtonext)
    fun orderSendtonext(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: AssignToOtherRequest?
    ): Call<SimpleResponseModel>

    @POST(getLocationMaster)
    fun getLocationMaster(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModel>

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

    @POST(getLabName)
    fun getLabname(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LabNameSearchResponseModel>

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

// order status

    @POST(getOrderStatus)
    fun getOrderStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OrderStatusResponseModel>

    @POST(getOrderStatus)
    fun getSearchOrderStatus(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body orderStatusRequestModel: OrderStatusRequestModel?
    ): Call<OrderStatusResponseModel>

    @POST(GetRefrenceTestMethod)
    fun getSearchOrderStatus(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<OrderStatusSpinnerResponseModel>


    @POST(GetLabSearchResult)
    fun getOrderStatusTestName(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<TestNameResponseModel>


    // result dispathch


    @POST(getResultDispatch)
    fun getresultdispatchlist(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String?,
        @Body requestResultdiapatch: RequestDispatchSearch?
    ): Call<ResponseResultDispatch>

    //New order

    @POST(searchRegisterpatient)
    fun searchOutPatientLmisOrder(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body searchPatientRequestModel: SearchPatientRequestModel?
    ): Call<NewLmisOrderModule>

    @GET(GetFavorites)
    fun getLmisFavourites(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("lab_id") dept_id: Int,
        @Query("fav_type_id") fav_type_id: Int
    ): Call<FavouritesResponseModel>

    @GET(GetTemplete)
    fun getLmisTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("lab_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<TempleResponseModel>


    @POST(GetFavaddDepartmentList)
    fun getFavddAllADepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddAllDepatResponseModel>


    @POST(getUserDepartment)
    fun getUserDepartment(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddAllDepatResponseModel>

    @POST(GetToLocation)
    fun getLocation(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<LocationMasterResponseModel>

    @POST(GetLabSearchResult)
    fun getLabTestSpinner(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("isMobileApi") value: Boolean,
        @Body labTechSearch: LabTechSearch?
    ): Call<LabTestSpinnerResponseModel>


    @POST(GetReference)
    fun getLabType(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body labtype: RequestBody?
    ): Call<GetReferenceResponseModel>

    @POST(GetPrevLab)
    fun getPrevLmisLab(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<PrevLabLmisResponseModel>

    @PUT(DeleteTemplate)
    fun deleteTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SimpleResponseModel>

    @GET(LabGetTemplate)
    fun getLastTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("dept_id") dept_id: Int
    ): Call<ResponseLabGetTemplateDetails>

    @GET(LabGetTemplate)
    fun getLmisLastTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("temp_id") temp_id: Int,
        @Query("temp_type_id") temp_type_id: Int,
        @Query("lab_id") dept_id: Int
    ): Call<ResponseLabGetTemplateDetails>

    @POST(EmrPost)
    fun lmisEmrpost(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestLmisNewOrder: RequestLmisNewOrder?
    ): Call<SimpleResponseModel>

    @PUT(LmisLabUpdate)
    fun lmisLabUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body requestLmisNewOrder: LabModifiyRequest?
    ): Call<LabModifiyResponse>

    @POST(GetLabSearchResult)
    fun getLmisSearchResult(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<ResponseLmisListview>

    @POST(CreateEncounter)
    fun LmiscreateEncounter(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") faciltyid: Int,
        @Body createEncounterRequestModel: CreateEncounterRequestModel?
    ): Call<CreateEncounterResponseModel>

    @GET(GetEncounters)
    fun getLmisEncounters(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patientId") patientId: Int,
        @Query("doctorId") doctorId: Int,
        @Query("departmentId") departmentId: Int,
        @Query("encounterType") encounterType: Int
    ): Call<FectchEncounterResponseModel>

    @POST(getDoctorName)
    fun getDoctorName(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<DoctorNameResponseModel>


    @GET(getWardId)
    fun getWardIdByPatientId(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("patient_uuid") patient_uuid: Int
    ): Call<GetWardIdResponseModel>

    @POST(gettolocationmapid)
    fun getToLocationLabTest(
        @Header("Accept-Language") acceptLanguage: String?,
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<GetToLocationTestResponse>

/*    @POST(GetFavDepartmentList)
    fun getFavDepartmentList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddResponseModel>*/

    @POST(GetLabSearchResult)
    fun getAutocommitText(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavAddTestNameResponse>

    @POST(GetFavddAll)
    fun getFavddAll(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestLabFavModel?
    ): Call<LabFavManageResponseModel>

    @GET(GetFavddAllList)
    fun getFavddAllList(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Query("favourite_id") favourite_id: Int,
        @Query("favourite_type_id") favourite_type_id: Int
    ): Call<FavAddListResponse>

    @PUT(FavouriteUpdate)
    fun labEditFav(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<FavEditResponse>

    @PUT(DeleteRows)
    fun deleteRows(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestBody?
    ): Call<SimpleResponseModel>

    @POST(LabTemplateCreate)
    fun createTemplate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: RequestTemplateAddDetails?
    ): Call<ReponseTemplateadd>

    @PUT(LabUpdateTemplate)
    fun getTemplateUpdate(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body body: UpdateRequestModule?
    ): Call<SimpleResponseModel>

    @GET(GetTemplete)
    fun getTemplete(
        @Header("Authorization") authorization: String?,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_id: Int,
        @Query("dept_id") dept_id: Int,
        @Query("temp_type_id") temp_type_id: Int
    ): Call<TempleResponseModel>



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
        const val rapidSave =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorder/saveandapproval"
        const val orderSendtonext =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/assigntootherinstitute"

        const val orderDetailsGetLabApproval =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/getOrderProcessDetails"
        const val getRejectReference =
            BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getReference"
        const val getLabName =
            BASE_DOMAIN + "Appmaster/v1/api/facility/otherFaciltiySearchDropdown"

        const val getSampleAcceptance =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorderdetails/ordersampleacceptancetestwise"

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


        // order Status

        const val getOrderStatus =
            BASE_DOMAIN + "HMIS-LIS/v1/api/ordertat/getorderstatus"

        //reesult

        const val getResultDispatch =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientworkorderdetails/getresultdispatchlist"


        //New order

        const val searchRegisterpatient =
            BASE_DOMAIN + "registration/v1/api/patient/search"
        const val GetFavorites =
            BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getFavourite"
        const val GetTemplete =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettemplateByID"
        const val GetFavaddDepartmentList =
            BASE_DOMAIN + "Appmaster/v1/api/department/getAllDepartment"
        const val getUserDepartment =
            BASE_DOMAIN + "Appmaster/v1/api/userDepartment/getUserDepartment"
        const val GetToLocation =
            BASE_DOMAIN + "HMIS-LIS/v1/api/tolocationmaster/gettolocationmaster"
        const val GetReference =
                BASE_DOMAIN + "HMIS-LIS/v1/api/commonReference/getReference"
        const val GetPrevLab =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/getLatestRecords"
        const val DeleteTemplate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/deleteTemplateDetails"
        const val LabGetTemplate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/gettempdetails"
        const val EmrPost =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/postpatientorder"
        const val LmisLabUpdate =
            BASE_DOMAIN + "HMIS-LIS/v1/api/patientorders/updatePatientOrder"
        const val CreateEncounter =
                BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/create"
        const val GetEncounters =
            BASE_DOMAIN + "HMIS-EMR/v1/api/encounter/getEncounterByDocAndPatientId"
        const val getDoctorName =
            BASE_DOMAIN + "Appmaster/v1/api/userProfile/getAllDoctorsByFacilityId"
        const val getWardId =
            BASE_DOMAIN + "HMIS-IP-Management/v1/api/admission/getWardByPatientID"
        const val gettolocationmapid =
            BASE_DOMAIN + "HMIS-LIS/v1/api/departmentwisemapping/gettolocationmapid"
        const val GetFavDepartmentList =
                BASE_DOMAIN + "Appmaster/v1/api/department/getDepartmentOnlyById"
        const val GetFavddAll =
            BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/create?searchkey=lab"
        const val GetFavddAllList =
                BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/getFavouriteById"
        const val FavouriteUpdate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/updateFavouriteById"
        const val DeleteRows =
            BASE_DOMAIN + "HMIS-EMR/v1/api/favourite/delete"
        const val LabTemplateCreate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/create"
        const val LabUpdateTemplate =
            BASE_DOMAIN + "HMIS-EMR/v1/api/template/updatetemplateById"
    }
}