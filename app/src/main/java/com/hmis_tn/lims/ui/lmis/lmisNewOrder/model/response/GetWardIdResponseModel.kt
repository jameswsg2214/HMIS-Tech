package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response

data class GetWardIdResponseModel(
    var message: String? = null,
    var statusCode: Int? = null,
    var ward_uuid: Int? = null
)