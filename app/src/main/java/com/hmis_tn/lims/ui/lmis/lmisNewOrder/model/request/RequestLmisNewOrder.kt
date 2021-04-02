package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request

data class RequestLmisNewOrder(
    var details: List<Detail?>? = listOf(),
    var header: Header? = Header()
)