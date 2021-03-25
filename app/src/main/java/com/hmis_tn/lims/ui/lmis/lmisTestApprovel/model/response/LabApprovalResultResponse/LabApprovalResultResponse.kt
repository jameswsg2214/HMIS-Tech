package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse

data class LabApprovalResultResponse(
    val req: LabApprovelResultReq = LabApprovelResultReq(),
    val responseContents: LabApprovalResultResponseContents = LabApprovalResultResponseContents(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)