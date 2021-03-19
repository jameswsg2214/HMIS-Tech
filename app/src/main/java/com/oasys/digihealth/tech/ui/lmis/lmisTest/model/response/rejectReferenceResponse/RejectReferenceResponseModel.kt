package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.rejectReferenceResponse

data class RejectReferenceResponseModel(
    var responseContents: List<RejectReference> = listOf(),
    var req: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)