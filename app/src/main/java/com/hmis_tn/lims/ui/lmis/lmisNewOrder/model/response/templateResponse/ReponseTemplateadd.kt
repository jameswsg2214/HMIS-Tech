package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.templateResponse

data class ReponseTemplateadd(
    var code: Int? = 0,
    var message: String? = "",
    var responseContent: ResponseContentTemplateResponse? = ResponseContentTemplateResponse()
)