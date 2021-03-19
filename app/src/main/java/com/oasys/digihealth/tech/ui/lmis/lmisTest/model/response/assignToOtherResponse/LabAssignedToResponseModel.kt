package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.assignToOtherResponse

data class LabAssignedToResponseModel(
    val responseContents: List<LabAssignedToresponseContent?>? = listOf(),
    val req: String? = "",
    val status: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)