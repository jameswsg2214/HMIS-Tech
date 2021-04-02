package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response

data class ResponseLmisListview(
    var message: String? = "",
    var responseContents: List<ResponseContentslmislistview?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)