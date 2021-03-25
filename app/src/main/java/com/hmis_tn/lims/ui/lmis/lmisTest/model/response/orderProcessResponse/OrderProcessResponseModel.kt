package com.hmis_tn.lims.ui.lmis.lmisTest.model.response.orderProcessResponse


data class OrderProcessResponseModel(
    var responseContents: OrderProcess = OrderProcess(),
    var msg: String = "",
    var statusCode: Int = 0
)