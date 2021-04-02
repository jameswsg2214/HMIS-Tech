package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTemplate

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTemplate.ResponseContentLabGetDetails

data class ResponseLabGetTemplateDetails(
    var req: String? = "",
    var responseContent: ResponseContentLabGetDetails? = ResponseContentLabGetDetails(),
    var statusCode: Int? = 0


)