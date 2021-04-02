package com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model

data class OrderStatusSpinnerResponseModel(
    val responseContents: List<OrderStatusSpinnerresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)