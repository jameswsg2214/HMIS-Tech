package com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse

import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.OrderProcessDetail

data class LabApprovelResultReq(
    var OrderProcessDetails: List<OrderProcessDetail> = listOf()
)