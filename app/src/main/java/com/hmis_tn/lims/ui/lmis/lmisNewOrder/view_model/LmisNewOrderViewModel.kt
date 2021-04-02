package com.hmis_tn.lims.ui.lmis.lmisNewOrder.view_model

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
import com.hmis_tn.lims.ui.institution.lmis.model.LocationMasterResponseModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.*
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.LabTechSearch
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.RequestLmisNewOrder
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.SearchPatientRequestModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest.CreateEncounterRequestModel
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest.Encounter
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest.EncounterDoctor
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
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.searcPatientListResponse.NewLmisOrderModule
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.templateResponse.ReponseTemplateadd
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.testMethodResponse.ResponseTestMethod
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel.LabTestSpinnerResponseModel
import com.hmis_tn.lims.ui.login.model.SimpleResponseModel
import com.hmis_tn.lims.utils.Utils

import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class LmisNewOrderViewModel(
    application: Application
) : AndroidViewModel(
    application
) {
    var errorText = MutableLiveData<String>()

    var progress = MutableLiveData<Int>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var department_UUID: Int? = 0
    private var facility_id: Int? = 0
    private var labUuid: Int? = null
    private var encountertype: Int? = null
    private var otherDepartment: String = ""
    var appPreferences: AppPreferences? = null

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        labUuid = appPreferences?.getInt(AppConstants.LAB_UUID)

        encountertype = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        otherDepartment = appPreferences?.getString(AppConstants.OTHER_DEPARTMENT_UUID)!!
    }

    fun searchPatient(
        InputPutFieldInput: String,
        currentPage: Int,
        pageSize: Int,
        sortField: String,
        sortOrder: String,
        patientSearchRetrofitCallBack: RetrofitCallback<NewLmisOrderModule>
    ) {
        val searchPatientRequestModel = SearchPatientRequestModel()
        if (InputPutFieldInput.length > 10) {
            searchPatientRequestModel.mobile = ""
            searchPatientRequestModel.pin = InputPutFieldInput
        } else {
            searchPatientRequestModel.mobile = InputPutFieldInput
            searchPatientRequestModel.pin = ""
        }

        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = pageSize
        searchPatientRequestModel.sortField = sortField
        searchPatientRequestModel.sortOrder = sortOrder

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientLmisOrder(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModel
        )!!.enqueue(
            RetrofitMainCallback(patientSearchRetrofitCallBack)
        )
    }

    fun getPatientListNextPage(
        InputPutFieldInput: String,
        currentPage: Int,
        pageSize: Int,
        patientSearchNextRetrofitCallBack: RetrofitCallback<NewLmisOrderModule>
    ) {

        val searchPatientRequestModel = SearchPatientRequestModel()

        if (InputPutFieldInput.length > 10) {
            searchPatientRequestModel.mobile = ""
            searchPatientRequestModel.pin = InputPutFieldInput
        } else {
            searchPatientRequestModel.mobile = InputPutFieldInput
            searchPatientRequestModel.pin = ""
        }
        searchPatientRequestModel.pageNo = currentPage
        searchPatientRequestModel.paginationSize = pageSize
        searchPatientRequestModel.sortField = "modified_date"
        searchPatientRequestModel.sortOrder = "DESC"

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.searchOutPatientLmisOrder(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,
            searchPatientRequestModel
        )!!.enqueue(
            RetrofitMainCallback(patientSearchNextRetrofitCallBack)
        )
    }

    fun getFavourites(
        emrWorkFlowRetrofitCallBack: RetrofitCallback<FavouritesResponseModel>,
        labid: Int?
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getLmisFavourites(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            labid!!,
            AppConstants.FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))

        /*if(department_UUID!=0){

            apiService?.getLmisFavouritesDept(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,facility_id!!, department_UUID!!,
                AppConstants.FAV_TYPE_ID_LAB
            )?.enqueue(RetrofitMainCallback(emrWorkFlowRetrofitCallBack))

        }
        else{



        }*/


        return
    }

    fun getTemplete(templeteRetrofitCallBack: RetrofitCallback<TempleResponseModel>, labid: Int?) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getLmisTemplete(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            labid!!,
            AppConstants.FAV_TYPE_ID_LAB
        )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))


        return
    }

    fun getAllDepartment(
        facilityID: Int?,
        favAddAllDepartmentCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("facilityBased", true)
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

        apiService?.getFavddAllADepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }

    fun getuserDepartment(
        doctorUUId: Int?,
        favAddAllDepartmentCallBack: RetrofitCallback<FavAddAllDepatResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("doctor_uuid", doctorUUId)
            jsonBody.put("is_lab", true)
            jsonBody.put("paginationSize", 100000)
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

        apiService?.getUserDepartment(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddAllDepartmentCallBack))
        return

    }


    fun getMethod(
        tablename: String,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<ResponseTestMethod>
    ) {

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
            jsonBody.put("table_name", tablename)
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
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

    }

    fun getLocationAPI(stateRetrofitCallback: RetrofitCallback<LocationMasterResponseModel>) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val jsonBody = JSONObject()
        try {

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

        apiService?.getLocation(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(stateRetrofitCallback))

        return
    }

    fun getTextMethod1(
        search: String,
        ResponseTestMethodRetrofitCallback: RetrofitCallback<LabTestSpinnerResponseModel>
    ) {

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }


        val req: LabTechSearch = LabTechSearch()

        if (labUuid != 0) {
            req.lab_uuid = labUuid.toString()
        }
        req.sortField = "uuid"
        req.sortOrder = "DESC"
        req.search = search
        req.is_default = false

        if (otherDepartment != "") {

            otherDepartment = otherDepartment.replace("[", "")

            otherDepartment = otherDepartment.replace("]", "")

            val dept = Utils.stringToIntArray(otherDepartment)

            req.other_department_uuids = dept


            progress.value = 0
            val aiiceApplication = HmisApplication.get(getApplication())
            val apiService = aiiceApplication.getRetrofitService()

            apiService?.getLabTestSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, true,
                req
            )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))

        } else {

            try {
                if (labUuid != 0) {

                    jsonBody.put("lab_uuid", labUuid)

                } else {

                    jsonBody.put("lab_uuid", null)

                }
                jsonBody.put("sortField", "uuid")
                jsonBody.put("sortOrder", "DESC")
                jsonBody.put("search", search)
                jsonBody.put("is_default", false)


                if (otherDepartment != "") {
                    val dept = Utils.stringToIntArray(otherDepartment)

                    jsonBody.put("other_department_uuids", dept)

                } else {


                }


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

            apiService?.getLabTestSpinner(
                AppConstants.ACCEPT_LANGUAGE_EN,
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!, facility_id!!, true,
                body
            )?.enqueue(RetrofitMainCallback(ResponseTestMethodRetrofitCallback))


        }


    }


    fun getReference(
        labTypeRetrofitCallback: RetrofitCallback<GetReferenceResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()

        try {
            jsonBody.put("table_name", "order_priority")
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
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
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        apiService?.getLabType(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(labTypeRetrofitCallback))
        return

    }

    fun getPrevLabCallback(
        patientId: Int?,
        facilityid: Int?,
        historyRadiologyRetrofitCallback: RetrofitCallback<PrevLabLmisResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_id", patientId)
            jsonBody.put("lab_master_type_uuid", AppConstants?.LAB_TESTMASTER_UUID)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        progress.value = 0
        val hmisApplication = HmisApplication.get(getApplication())
        val apiService = hmisApplication.getRetrofitService()
        apiService?.getPrevLmisLab(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityid!!, body
        )?.enqueue(RetrofitMainCallback(historyRadiologyRetrofitCallback))
    }


    fun deleteTemplate(
        facility_id: Int?,
        template_uuid: Int?,
        deleteRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("template_uuid", template_uuid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }

    fun getTemplateDetails(
        templateId: Int?,
        facilityUuid: Int?,
        LabID: Int?,
        getTemplateRetrofitCallback: RetrofitCallback<ResponseLabGetTemplateDetails>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        if (department_UUID != 0) {
            apiService?.getLastTemplate(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facilityUuid!!,
                templateId!!,
                AppConstants.FAV_TYPE_ID_LAB,
                department_UUID!!
            )?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        } else {
            apiService?.getLmisLastTemplate(
                AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
                userDataStoreBean?.uuid!!,
                facilityUuid!!,
                templateId!!,
                AppConstants.FAV_TYPE_ID_LAB,
                LabID!!
            )?.enqueue(RetrofitMainCallback(getTemplateRetrofitCallback))
        }

        return
    }

    fun labInsert(
        facility_id: Int?,
        EmrRequestData: RequestLmisNewOrder,
            configFinalRetrofitCallBack: RetrofitCallback<SimpleResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.lmisEmrpost(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            EmrRequestData
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

    }


    fun updateLab(
        facility_id: Int?,
        labModifiyRequest: LabModifiyRequest,
        configFinalRetrofitCallBack: RetrofitCallback<LabModifiyResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.lmisLabUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            labModifiyRequest
        )?.enqueue(RetrofitMainCallback(configFinalRetrofitCallBack))
        return

    }


    fun getComplaintSearchResult(
        facility_id: Int?,
        name: String?,
        complaintSearchRetrofitCallBack: RetrofitCallback<ResponseLmisListview>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("sortField", "uuid")
            jsonBody.put("sortOrder", "DESC")
            jsonBody.put("search", name)
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
        apiService?.getLmisSearchResult(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(complaintSearchRetrofitCallBack))
        return
    }

    fun createEncounter(
        patientUuid: Int,
        encounterType: Int,
        doctorId: Int, departmentID: Int?,
        createEncounterCallback: RetrofitCallback<CreateEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val createEncounterRequestModel = CreateEncounterRequestModel()

        val encounter = Encounter()
        encounter.admission_request_uuid = 0
        encounter.admission_uuid = 0
        encounter.appointment_uuid = 0
        encounter.department_uuid = departmentID
        encounter.discharge_type_uuid = 0
        encounter.encounter_identifier = 0
        encounter.encounter_priority_uuid = 0
        encounter.encounter_status_uuid = 0
        encounter.encounter_type_uuid = encounterType
        encounter.facility_uuid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        encounter.patient_uuid = patientUuid

        createEncounterRequestModel.encounter = encounter

        val encounterDoctor = EncounterDoctor()
        encounterDoctor.department_uuid = departmentID
        encounterDoctor.dept_visit_type_uuid = encounterType
        encounterDoctor.doctor_uuid = doctorId
        encounterDoctor.doctor_visit_type_uuid = encounterType
        encounterDoctor.patient_uuid = patientUuid
        encounterDoctor.session_type_uuid = 0
        encounterDoctor.speciality_uuid = 0
        encounterDoctor.sub_deparment_uuid = 0
        encounterDoctor.visit_type_uuid = encounterType

        createEncounterRequestModel.encounterDoctor = encounterDoctor

        apiService?.LmiscreateEncounter(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            facility_id!!,
            userDataStoreBean?.uuid!!,
            createEncounterRequestModel

        )!!.enqueue(
            RetrofitMainCallback(createEncounterCallback)
        )
    }

    fun getDoctorName(
        facilityId: Int,
        doctorNameCallback: RetrofitCallback<DoctorNameResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", facilityId)
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
        val userDataStoreBean = userDetailsRoomRepository!!.getUserDetails()

        apiService?.getDoctorName(
            "en",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facilityId, body

        )?.enqueue(RetrofitMainCallback(doctorNameCallback))
    }

    fun getEncounter(
        facility_id: Int?,
        patientUuid: Int,
        doctorId: Int,
        encounterType: Int, departmentID: Int?,
        fetchEncounterRetrofitCallBack: RetrofitCallback<FectchEncounterResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            department_UUID
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        apiService?.getLmisEncounters(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            facility_id!!,
            userDataStoreBean?.uuid!!,
            patientUuid,
            doctorId,
            departmentID!!,
            encounterType

        )!!.enqueue(
            RetrofitMainCallback(fetchEncounterRetrofitCallBack)
        )
    }



    fun getWardId(
        patientId: Int,
        wardIdRetrofitCallback: RetrofitCallback<GetWardIdResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getWardIdByPatientId(
            AppConstants.LANGUAGE,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            patientId!!
        )?.enqueue(RetrofitMainCallback(wardIdRetrofitCallback))
        return

    }

    fun getToLocationTest(
        labToLoctionTestRetrofitCallback: RetrofitCallback<GetToLocationTestResponse>,
        facilityId: Int,
        strAutoId: Int?,
        id: Int,
        wardUUid: Int
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("test_master_uuid", id)
            jsonBody.put("from_department_uuid", strAutoId)
            jsonBody.put("profile_uuid", null)


            if (encountertype == AppConstants.TYPE_IN_PATIENT) {

                jsonBody.put("ward_uuid", wardUUid)


            }


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
        apiService?.getToLocationLabTest(
            AppConstants.ACCEPT_LANGUAGE_EN,
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_id!!, body
        )?.enqueue(RetrofitMainCallback(labToLoctionTestRetrofitCallback))
        return

    }


    //  Fav


/*
    fun getDepartmentList(
        facilityID: Int?,
        FavdepartmentRetrofitCallBack: RetrofitCallback<FavAddResponseModel>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("uuid", department_UUID)
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

        apiService?.getFavDepartmentList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!,
            body
        )?.enqueue(RetrofitMainCallback(FavdepartmentRetrofitCallBack))
        return
    }
*/

    fun getTestName(
        name: String,
        favAddTestNameCallBack: RetrofitCallback<FavAddTestNameResponse>
    ) {

        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("search", name)
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

        apiService?.getAutocommitText(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!,
            body
        )?.enqueue(RetrofitMainCallback(favAddTestNameCallBack))
        return
    }

    fun getADDFavourite(
        facilityID: Int?, requestbody: RequestLabFavModel,
        emrposFavtRetrofitCallback: RetrofitCallback<LabFavManageResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFavddAll(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityID!!, requestbody
        )?.enqueue(RetrofitMainCallback(emrposFavtRetrofitCallback))
        return

    }

    fun getAddListFav(
        facilityUuid: Int?,
        favouriteMasterID: Int?,
        emrposListDataFavtRetrofitCallback: RetrofitCallback<FavAddListResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()

        apiService?.getFavddAllList(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!, favouriteMasterID!!, 0
        )?.enqueue(RetrofitMainCallback(emrposListDataFavtRetrofitCallback))
        return

    }

    fun getEditFavourite(
        facilityUuid: Int?,
        testMasterName: String?,
        favouriteId: Int?,
        deparmentUuid: Int?,
        favouriteDisplayOrder: String?,
        isactive: Boolean,
        emrposListDataFavtEditRetrofitCallback: RetrofitCallback<FavEditResponse>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("departmentId", deparmentUuid)
            jsonBody.put("favourite_display_order", favouriteDisplayOrder)
            jsonBody.put("testname", testMasterName)
            jsonBody.put("favourite_id", favouriteId)
            jsonBody.put("is_active", isactive)
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

        apiService?.labEditFav(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!, body
        )?.enqueue(RetrofitMainCallback(emrposListDataFavtEditRetrofitCallback))
        return

    }

    /*
     Delete
      */
    fun deleteFavourite(
        facility_id: Int?,
        favouriteId: Int?,
        deleteRetrofitCallback: RetrofitCallback<SimpleResponseModel>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        val jsonBody = JSONObject()
        try {
            jsonBody.put("favouriteId", favouriteId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )


        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.deleteRows(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facility_id!!, body
        )?.enqueue(RetrofitMainCallback(deleteRetrofitCallback))
        return
    }


    // Template

    fun labTemplateDetails(facilityUuid: Int?, requestTemplateAddDetails: RequestTemplateAddDetails, emrlabTemplateAddRetrofitCallback: RetrofitCallback<ReponseTemplateadd>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.createTemplate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!,
            requestTemplateAddDetails
        )?.enqueue(RetrofitMainCallback(emrlabTemplateAddRetrofitCallback))
        return



    }


    fun labUpdateTemplateDetails(facilityUuid: Int?, requestTemplateUpdateDetails: UpdateRequestModule, UpdateemrlabTemplateAddRetrofitCallback: RetrofitCallback<SimpleResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        apiService?.getTemplateUpdate(
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!, facilityUuid!!,
            requestTemplateUpdateDetails
        )?.enqueue(RetrofitMainCallback(UpdateemrlabTemplateAddRetrofitCallback))
        return



    }
    fun getTemplete(Labuuid:Int,templeteRetrofitCallBack: RetrofitCallback<TempleResponseModel>) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }
        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        if(department_UUID!=0){
            apiService?.getTemplete(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!, department_UUID!!,facility_id!!,
                AppConstants.FAV_TYPE_ID_LAB
            )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        }
        else{
            apiService?.getLmisTemplete(AppConstants.BEARER_AUTH + userDataStoreBean?.access_token, userDataStoreBean?.uuid!!, facility_id!!,Labuuid!!,
                AppConstants.FAV_TYPE_ID_LAB
            )?.enqueue(RetrofitMainCallback(templeteRetrofitCallBack))
        }

        return
    }


}