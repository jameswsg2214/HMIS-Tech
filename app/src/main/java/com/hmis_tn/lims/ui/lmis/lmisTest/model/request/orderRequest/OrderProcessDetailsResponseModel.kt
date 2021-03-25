package com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest


data class OrderProcessDetailsResponseModel(
    var responseContents: OrderProcessDetails = OrderProcessDetails(),
    var req: OrderReq = OrderReq(),
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)