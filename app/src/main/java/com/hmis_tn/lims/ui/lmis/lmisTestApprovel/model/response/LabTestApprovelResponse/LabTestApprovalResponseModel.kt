package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestApprovelResponse

import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.labTestResponse.DiseaseResultData
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.labTestResponse.OrderStatusCount
import com.hmis_tn.lims.ui.login.model.Req

data class LabTestApprovalResponseModel(
    val responseContents: List<LabTestApprovalresponseContent>? = listOf(),
    val disease_result_data: List<DiseaseResultData?>? = listOf(),
    val message: String? = "",
    val order_status_count: List<OrderStatusCount?>? = listOf(),
    val req: Req? = Req(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)