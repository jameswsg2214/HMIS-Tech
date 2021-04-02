package com.hmis_tn.lims.ui.lmis.lmisResultDispatch.request

data class RequestResultdiapatch(
    var pageNo: Int? = 0,
    var paginationSize: Int? = 0,
    var search: String? = "",
    var sortField: String? = "",
    var sortOrder: String? = ""
)