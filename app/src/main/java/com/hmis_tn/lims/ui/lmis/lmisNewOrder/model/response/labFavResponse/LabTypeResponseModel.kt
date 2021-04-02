package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.labFavResponse

data class LabTypeResponseModel(
    var req: String? = "",
    var responseContents: List<LabTypeResponseContent?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)