package com.hmis_tn.lims.ui.registeration.opRegistration.model.response.suffixResponse

data class GetAllResponseModel(
    var responseContents: List<GetAllResponse> = listOf(),
    var req: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)