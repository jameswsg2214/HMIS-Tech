package com.hmis_tn.lims.ui.lmis.lmisResultDispatch.request

data class RequestDispatchSearch(
    var doctor_first_name: Int? = 0,
    var fromDate: String? = "",
    var order_by_name: Int? = 0,
    var order_number: String? = "",
    var pageNo: Int? = 0,
    var paginationSize: Int? = 0,
    var patient_first_name: String? = "",
    var search: String? = "",
    var sortField: String? = "",
    var sortOrder: String? = "",
    var test_name: Int? = 0,
    var toDate: String? = "",
    var uhid: String? = ""
)