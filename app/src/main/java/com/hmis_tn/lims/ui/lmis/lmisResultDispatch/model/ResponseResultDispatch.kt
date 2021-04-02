package com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model

data class ResponseResultDispatch(
    var msg: String? = "",
    var req: Req? = Req(),
    var responseContents: List<ResponseContentsResultDispatch?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)