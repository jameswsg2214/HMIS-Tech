package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.labTestResponse

data class LabTestResponseModel(
    val responseContents: List<LabTestresponseContent?>? = listOf(),
    val disease_result_data: List<DiseaseResultData?>? = listOf(),
    val message: String? = "",
    val order_status_count: List<OrderStatusCount?>? = listOf(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)