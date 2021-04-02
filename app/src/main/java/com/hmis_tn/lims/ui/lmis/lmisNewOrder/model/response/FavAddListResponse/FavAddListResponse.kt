package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.FavAddListResponse

data class FavAddListResponse(
    var code: Int? = 0,
    var message: String? = "",
    var responseContentLength: Int? = 0,
    var responseContents: ResponseContentsfav? = ResponseContentsfav()
)