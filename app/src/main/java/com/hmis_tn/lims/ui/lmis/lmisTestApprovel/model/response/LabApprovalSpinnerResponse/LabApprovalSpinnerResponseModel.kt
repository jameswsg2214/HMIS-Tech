package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalSpinnerResponse

data class LabApprovalSpinnerResponseModel(
    val req: String = "",
    val responseContents: List<LabApprovalSpinnerResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)