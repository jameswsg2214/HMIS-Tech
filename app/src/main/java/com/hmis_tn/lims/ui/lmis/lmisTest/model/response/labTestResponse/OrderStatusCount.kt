package com.hmis_tn.lims.ui.lmis.lmisTest.model.response.labTestResponse

data class OrderStatusCount(
    val order_count: Int? = 0,
    val order_status_name: String? = "",
    val order_status_uuid: Int? = 0
)