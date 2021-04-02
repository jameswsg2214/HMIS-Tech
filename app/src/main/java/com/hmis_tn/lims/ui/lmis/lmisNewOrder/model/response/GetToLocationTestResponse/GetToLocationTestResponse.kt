package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.GetToLocationTestResponse

data class GetToLocationTestResponse(
    var responseContents: ToLocation = ToLocation(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)