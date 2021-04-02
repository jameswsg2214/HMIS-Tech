package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request

data class SearchPatientRequestModel(
    var mobile: String? = null,
    var pageNo: Int? = null,
    var paginationSize: Int? = null,
    var pin: String? = "",
    var sortField: String? = null,
    var sortOrder: String? = null,
    var admission_status_uuid: String = "",
    var department_uuid: String? = "",
    var facility_uuid: String? = "",
    var registeredDate: String? = ""

)

