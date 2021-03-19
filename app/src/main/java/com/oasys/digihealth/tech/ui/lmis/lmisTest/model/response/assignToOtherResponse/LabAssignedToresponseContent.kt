package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.assignToOtherResponse

import com.oasys.digihealth.tech.ui.login.model.institution_response.FacilityType

data class LabAssignedToresponseContent(
    val approval_user_uuid: Any? = Any(),
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val description: String? = "",
    val email: String? = "",
    val facility_level_uuid: Int? = 0,
    val facility_type: FacilityType? = FacilityType(),
    val facility_type_uuid: Int? = 0,
    val fax: String? = "",
    val health_office_uuid: Int? = 0,
    val hud_uuid: Int? = 0,
    val image_url: Any? = Any(),
    val is_active: Boolean? = false,
    val is_facility_model: Boolean? = false,
    val is_lab_center: Boolean? = false,
    val json_array: Any? = Any(),
    val language_uuid: Int? = 0,
    val mobile: String? = "",
    val modified_by: Any? = 0,
    val modified_date: String? = "",
    val nabh_logo: Any? = Any(),
    val name: String? = "Assigned From",
    val parent_facility_uuid: Int? = 0,
    val phone: String? = "",
    val revision: Int? = 0,
    val speciality_uuid: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0
)