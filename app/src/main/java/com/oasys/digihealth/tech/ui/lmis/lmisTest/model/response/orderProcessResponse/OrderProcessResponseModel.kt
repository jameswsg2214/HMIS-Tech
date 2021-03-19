package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.response.orderProcessResponse


data class OrderProcessResponseModel(
    var responseContents: OrderProcess = OrderProcess(),
    var msg: String = "",
    var statusCode: Int = 0
)