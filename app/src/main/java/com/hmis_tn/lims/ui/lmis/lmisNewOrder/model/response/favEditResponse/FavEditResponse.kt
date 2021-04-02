package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.favEditResponse

data class FavEditResponse(
    var code: Int? = 0,
    var message: String? = "",
    var requestContent: RequestContentfaveditresponse? = RequestContentfaveditresponse()
)