package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.templateRequest

data class RequestTemplateAddDetails(
    var details: List<Detail?>? = listOf(),
    var headers: Headers? = Headers()
)