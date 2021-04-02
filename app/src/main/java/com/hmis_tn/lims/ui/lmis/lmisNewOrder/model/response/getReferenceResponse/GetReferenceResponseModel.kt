package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getReferenceResponse

data class GetReferenceResponseModel(
    var responseContents: List<GetReference> = listOf(),
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)