package com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model

data class Req(
    var fromDate: String? = "",
    var pageNo: Int? = 0,
    var paginationSize: Int? = 0,
    var search: String? = "",
    var sortField: String? = "",
    var sortOrder: String? = "",
    var toDate: String? = ""
)