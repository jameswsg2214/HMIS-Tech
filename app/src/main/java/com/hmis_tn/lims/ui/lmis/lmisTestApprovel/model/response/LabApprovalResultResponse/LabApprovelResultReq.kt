package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse

import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest.OrderProcessDetail

data class LabApprovelResultReq(
    var OrderProcessDetails: List<OrderProcessDetail> = listOf()
)