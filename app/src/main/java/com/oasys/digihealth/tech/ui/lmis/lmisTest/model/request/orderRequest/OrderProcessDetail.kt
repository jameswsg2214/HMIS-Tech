package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest

data class OrderProcessDetail(
    var Id: Int = 0,
    var auth_status_uuid: Int? = null,
    var order_status_uuid: Int = 0,
    var to_location_uuid: Int = 0
)