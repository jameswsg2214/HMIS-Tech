package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList

data class TempleResponseModel(
    var req: String? = "",
    var responseContents: TempleteResponseContents? = TempleteResponseContents(),
    var statusCode: Int? = 0
)