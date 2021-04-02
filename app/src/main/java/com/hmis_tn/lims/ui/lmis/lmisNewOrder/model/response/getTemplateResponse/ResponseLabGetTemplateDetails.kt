package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTemplateResponse

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTemplateResponse.ResponseContentLabGetDetails

data class ResponseLabGetTemplateDetails(
    var req: String? = "",
    var responseContent: ResponseContentLabGetDetails? = ResponseContentLabGetDetails(),
    var statusCode: Int? = 0


)