package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.templateResponse

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.templateResponse.Detail
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.templateResponse.Headers

data class ResponseContentTemplateResponse(
    var details: List<Detail?>? = listOf(),
    var headers: Headers? = Headers()
)